package cn.blmdz.home.dao;

import org.apache.ibatis.annotations.Param;

import cn.blmdz.home.model.TalkLike;

public interface TalkLikeDao extends BaseDao<TalkLike> {
	
    /**
     * 查询用户TALK赞数据
     * @param userId
     * @param talkId
     * @return
     */
    TalkLike findTalkLike(@Param("userId") Long userId, @Param("talkId") Long talkId);
    
    /**
     * 统计TALK被赞数量
     * @param talkId
     * @return
     */
    Integer countTalkLike(@Param("talkId") Long talkId);
}