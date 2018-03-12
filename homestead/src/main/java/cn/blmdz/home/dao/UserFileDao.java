package cn.blmdz.home.dao;

import com.github.pagehelper.Page;

import cn.blmdz.home.model.UserFile;

public interface UserFileDao extends BaseDao<UserFile> {

    /**
     * 根据条件分页查找
     * @return
     */
    Page<UserFile> pageBySelect(Long userId);
    
    /**
     * 根据摘要查找
     * @param md5
     * @return
     */
    UserFile findByMD5(String md5);
    
    /**
     * 根据摘要查找数量
     * @param md5
     * @return
     */
    Integer countByMD5(String md5);
}