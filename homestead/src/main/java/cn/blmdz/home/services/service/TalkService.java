package cn.blmdz.home.services.service;

import java.util.List;

import cn.blmdz.home.model.TalkReply;
import cn.blmdz.home.model.valid.TalkReplyValid;
import cn.blmdz.home.model.valid.TalkValid;

public interface TalkService {

    /**
     * 发布
     * @param userId
     * @param talkValid
     */
    void issue(Long userId, TalkValid talkValid);
    /**
     * 回复/评论
     * @param userId
     * @param talkReplyValid
     */
    void reply(Long userId, TalkReplyValid talkReplyValid);
    /**
     * 点赞
     * @param userId
     * @param talkId
     */
    void like(Long userId, Long talkId);
    /**
     * 取消点赞
     * @param userId
     * @param talkId
     */
    void unlike(Long userId, Long talkId);
    /**
     * 查看评论
     * @param talkId
     * @return
     */
    List<TalkReply> talkReplyBySelect(Long talkId);
}
