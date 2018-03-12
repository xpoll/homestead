package cn.blmdz.home.dao;

import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.Page;

import cn.blmdz.home.model.Talk;
import cn.blmdz.home.model.search.TalkSearch;

public interface TalkDao extends BaseDao<Talk> {
	
    /**
     * 查看用户最近一条Talk
     * @param userId
     * @param privacy
     * @return
     */
    Talk findFirstTalkByUserId(@Param("userId") Long userId, @Param("privacy") Integer privacy);
    
    /**
     * 根据条件分页查找
     * @return
     */
    Page<Talk> pageBySelect(TalkSearch search);
}