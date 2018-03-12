package cn.blmdz.home.services.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.base.Function;
import com.google.common.collect.Lists;

import cn.blmdz.aide.file.util.FUtil;
import cn.blmdz.home.base.BasePage;
import cn.blmdz.home.dao.UserFileDao;
import cn.blmdz.home.model.UserFile;
import cn.blmdz.home.model.vo.UserImageVo;
import cn.blmdz.home.services.service.UserFileService;

@Service
public class UserFileServiceImpl implements UserFileService {

    @Autowired
    private UserFileDao userFileDao;
    @Value("${image.base.url:http://img.blmdz.cn}")
    private String imageBaseUrl;
    
    @Override
    public BasePage<Long, UserImageVo> pageFileByUser(BasePage<Long, UserImageVo> search) {
        PageHelper.startPage(search.getNum(), search.getSize(), search.getOrder());
        Page<UserFile> page = userFileDao.pageBySelect(search.getMode());
        search.setTotal(page.getTotal());
        search.setData(Lists.transform(page, new Function<UserFile, UserImageVo>() {
            @Override
            public UserImageVo apply(UserFile input) {
                input.setPath(FUtil.absolutePath(imageBaseUrl, input.getPath()));
                UserImageVo vo = new UserImageVo();
                BeanUtils.copyProperties(input, vo);
                return vo;
            }
        }));
        return search;
    }
}
