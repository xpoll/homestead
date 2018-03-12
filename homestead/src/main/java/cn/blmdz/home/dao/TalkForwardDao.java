package cn.blmdz.home.dao;

import java.util.List;

import com.github.pagehelper.Page;

import cn.blmdz.home.model.TalkForward;

public interface TalkForwardDao extends BaseDao<TalkForward> {
	
    /**
     * 上节点
     * @param id
     * @return
     */
    List<TalkForward> findUpperNode(Long id);

    /**
     * 下节点
     * @param id
     * @return
     */
    List<TalkForward> findUnderNode(Long id);
    
    /**
     * 根据条件分页查找
     * @return
     */
    Page<TalkForward> pageBySelect(Long upperId);
    
    /**
     * 上节点数量统计
     * @param id
     * @return
     */
    Integer countUpperNode(Long id);
    
    /**
     * 下节点数量统计
     * @param id
     * @return
     */
    Integer countUnderNode(Long id);
}