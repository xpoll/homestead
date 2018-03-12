package cn.blmdz.home.dao;

import cn.blmdz.home.model.TalkReplySubscribe;

public interface TalkReplySubscribeDao extends BaseDao<TalkReplySubscribe> {
	
    /**
     * 根据条件查找
     * @param talkReplySubscribe
     * @return
     */
    TalkReplySubscribe findBySelect(TalkReplySubscribe talkReplySubscribe);
}