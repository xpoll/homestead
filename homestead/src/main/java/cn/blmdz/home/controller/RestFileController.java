package cn.blmdz.home.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import cn.blmdz.home.base.BasePage;
import cn.blmdz.home.base.BaseUser;
import cn.blmdz.home.base.Response;
import cn.blmdz.home.exception.WebJspException;
import cn.blmdz.home.model.vo.UserImageVo;
import cn.blmdz.home.services.helper.FileHelper;
import cn.blmdz.home.services.service.UserFileService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value="/api/file")
public class RestFileController {
    
    @Autowired
    private FileHelper fileHelper;
    @Autowired
    private UserFileService userFileService;

    /**
     * 单文件上传
     */
    @RequestMapping(method=RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<UserImageVo> file(MultipartHttpServletRequest request) {

        BaseUser baseUser = (BaseUser) request.getSession().getAttribute("user");
        log.info("current user: {}", baseUser);
        
        if (baseUser == null) return Response.<UserImageVo>build(null).message("请登录后再上传图片");
        
        Map<String, MultipartFile> maps = request.getFileMap();
        if (maps.keySet().isEmpty() || !maps.containsKey("file")) return Response.<UserImageVo>build(null).message("上传图片不能为空");

        MultipartFile file = request.getFile("file");
        if(!FileHelper.ALLOWED_CONTENT_TYPES.contains(file.getContentType())){
            throw new WebJspException("图片格式不正确");
        }
        
        return Response.build(fileHelper.upload(baseUser.getId(), file));
    }
    
    @RequestMapping(value="page", method=RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<BasePage<Long, UserImageVo>> page(HttpServletRequest request, @Valid @RequestBody BasePage<Long, UserImageVo> search) {

        BaseUser baseUser = (BaseUser) request.getSession().getAttribute("user");
        log.info("current user: {}", baseUser);
        return Response.build(userFileService.pageFileByUser(search));
    }
    
    @RequestMapping(value="/del/{id}", method=RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<Boolean> delete(HttpServletRequest request, @PathVariable("id") Long id) {

        BaseUser baseUser = (BaseUser) request.getSession().getAttribute("user");
        log.info("current user: {}, id: {}", baseUser, id);
        
        fileHelper.delete(baseUser.getId(), id);
        return Response.ok();
    }
}
