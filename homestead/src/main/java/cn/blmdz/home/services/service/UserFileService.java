package cn.blmdz.home.services.service;

import cn.blmdz.home.base.BasePage;
import cn.blmdz.home.model.vo.UserImageVo;

public interface UserFileService {

    /**
     * 文件分页
     */
    BasePage<Long, UserImageVo> pageFileByUser(BasePage<Long, UserImageVo> search);
}
