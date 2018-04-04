package cn.blmdz.home.services.helper;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Objects;
import java.util.Set;

import javax.imageio.ImageIO;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.Files;

import cn.blmdz.aide.file.FileServer;
import cn.blmdz.aide.file.util.FUtil;
import cn.blmdz.home.dao.UserFileDao;
import cn.blmdz.home.enums.FileType;
import cn.blmdz.home.exception.WebJspException;
import cn.blmdz.home.model.UserFile;
import cn.blmdz.home.model.vo.UserImageVo;
import cn.blmdz.home.util.FileUtil;
import cn.blmdz.home.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FileHelper {

    private final static Set<String> ALLOWED_TYPES = ImmutableSet.of("jpeg", "jpg", "png", "gif");

    public final static Set<String> ALLOWED_CONTENT_TYPES = ImmutableSet.of("image/bmp","image/png","image/gif","image/jpg","image/jpeg","image/pjpeg");

    @Autowired
    private FileServer fileServer;
    @Autowired
    private UserFileDao userFileDao;
    
    @Value("${image.max.size:2097152}")
    private Long imageMaxSize;         //默认2M
    @Value("${image.base.url:http://img.blmdz.cn}")
    private String imageBaseUrl;


    /**
     * 图片文件
     * @param userId    用户id
     * @param file      文件
     * @return          上传文件信息
     */
    public UserImageVo upload(Long userId, MultipartFile file) {
        if(!Objects.equals(FileUtil.fileType(file.getOriginalFilename()) , FileType.IMAGE)) throw new WebJspException("图片格式不正确");
        UserFile image = new UserFile();
        image.setFileType(FileType.IMAGE.value());
        image.setUserId(userId);
        image.setName(file.getOriginalFilename());
        image.setFolderId(0L);

        String ext = Files.getFileExtension(file.getOriginalFilename()).toLowerCase();
        if (!ALLOWED_TYPES.contains(ext)) throw new WebJspException("图片格式不正确");
        
        try {
            byte[] imageData = file.getBytes();
            if (imageData.length > imageMaxSize) {
                log.debug("image size {} ,maxsize {} ,the upload image is to large", imageData.length, imageMaxSize);
                throw new WebJspException("图片过大");
            }
            image.setSize((int)file.getSize());
            image.setExtraJson(imageSize(file.getBytes()));
            
            image = upload(image, file);

            userFileDao.create(image);
            
            image.setId(image.getId());
            image.setPath(FUtil.absolutePath(imageBaseUrl, image.getPath()));
            UserImageVo vo = new UserImageVo();
            BeanUtils.copyProperties(image, vo);
            return vo;
        } catch (Exception e) {
            log.error("failed to process upload image {},cause:{}", file.getOriginalFilename(), Throwables.getStackTraceAsString(e));
            throw new WebJspException("图片上传失败");
        }
    }
    
    /**
     * 计算MD5进行上传或替换
     * @param image
     * @param file
     * @return
     */
    private UserFile upload(UserFile image, MultipartFile file) {
        
        String md5 = getFileMD5(file);
        UserFile find = null;
        if (!Strings.isNullOrEmpty(md5)) {
            find = userFileDao.findByMD5(md5);
        }
        if (find != null && Objects.equals(find.getSize(), image.getSize())) {
            image.setPath(find.getPath());
            image.setMd5(find.getMd5());
        } else {
            
            String filePath = fileServer.write("home" +"/" + FileUtil.newFileName(file.getOriginalFilename()), file);
            boolean isSucceed = !Strings.isNullOrEmpty(filePath);
            if (!isSucceed) {
                log.error("write file(name={}) of user(id={}) to image server failed", file.getOriginalFilename(), image.getUserId());
                throw new WebJspException("图片上传失败");
            }
            image.setMd5(md5);
            image.setPath(filePath);
        }

        return image;
    }

    /**
     * 获取图片的尺寸
     * @param imageData 图片数据
     * @return  返回尺寸
     */
    private String imageSize(byte[] imageData){
        try {
            
            BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(imageData));

            Integer width = originalImage.getWidth();
            Integer height = originalImage.getHeight();

            return JsonMapper.nonEmptyMapper().toJson(ImmutableMap.of("width", width, "height", height));
        }catch(IOException e){
            log.error("Read image size failed, Error code={}", Throwables.getStackTraceAsString(e));
            return "{}";
        }
    }
    
    /**
     * 删除图片
     * @param userId
     * @param id
     */
    public void delete(Long userId, Long id) {
        UserFile file = userFileDao.findById(id);
        if (file == null || !Objects.equals(file.getUserId(), userId)) throw new WebJspException("文件不存在");
        if (Objects.isNull(file.getMd5()) || userFileDao.countByMD5(file.getMd5()) == 1) {
            try {
                fileServer.delete(file.getPath());
            } catch (Exception e) {
                throw new WebJspException("文件删除失败");
            }
        }
        userFileDao.tombstone(id);
    }
    


    // 计算文件的 MD5 值
    private String getFileMD5(MultipartFile file) {
        MessageDigest digest = null;
        InputStream in = null;
        byte buffer[] = new byte[8192];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");// MD5 SHA-1
            in = file.getInputStream();
            while ((len = in.read(buffer)) != -1) digest.update(buffer, 0, len);
            BigInteger bigInt = new BigInteger(1, digest.digest());
            return bigInt.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
