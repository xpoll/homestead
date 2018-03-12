package cn.blmdz.home.dao;

import java.util.List;

import cn.blmdz.home.model.TalkReply;

public interface TalkReplyDao extends BaseDao<TalkReply> {
	
    /**
     * 回复/评论个数 superId = null
     * @param talkId
     * @return
     */
    Integer countTalkReply(Long talkId);
    
    /**
     * TALK所有回复/评论
     * @param talkId
     * @return
     */
    List<TalkReply> listBySelect(Long talkId);
}