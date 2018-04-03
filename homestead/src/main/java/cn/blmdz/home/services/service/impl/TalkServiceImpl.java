package cn.blmdz.home.services.service.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.blmdz.home.dao.TalkDao;
import cn.blmdz.home.dao.TalkLikeDao;
import cn.blmdz.home.dao.TalkReplyDao;
import cn.blmdz.home.dao.TalkReplySubscribeDao;
import cn.blmdz.home.exception.WebJspException;
import cn.blmdz.home.model.Talk;
import cn.blmdz.home.model.TalkLike;
import cn.blmdz.home.model.TalkReply;
import cn.blmdz.home.model.TalkReplySubscribe;
import cn.blmdz.home.model.valid.TalkReplyValid;
import cn.blmdz.home.model.valid.TalkValid;
import cn.blmdz.home.services.service.TalkService;

@Service
public class TalkServiceImpl implements TalkService {
    
    @Autowired
    private TalkDao talkDao;
    @Autowired
    private TalkReplyDao talkReplyDao;
    @Autowired
    private TalkReplySubscribeDao talkReplySubscribeDao;
    @Autowired
    private TalkLikeDao talkLikeDao;

    @Override
    public void issue(Long userId, TalkValid talkValid) {
        Talk talk = new Talk();
        BeanUtils.copyProperties(talkValid, talk);
        talk.setUserId(userId);
        talk.setTalkId(-1L);
        talkDao.create(talk);
    }

    @Override
    @Transactional
    public void reply(Long userId, TalkReplyValid talkReplyValid) {
        Talk talk = talkDao.findById(talkReplyValid.getTalkId());
        if (talk == null || !talk.getStatus()) throw new WebJspException("TALK不存在或已被删除");
        
        if (talkReplyValid.getSuperId() == null) talkReplyValid.setReplyToId(null);
        if (talkReplyValid.getReplyToId() == null) talkReplyValid.setSuperId(null);
        
        TalkReply talkReply = new TalkReply();
        BeanUtils.copyProperties(talkReplyValid, talkReply);
        talkReply.setUserId(userId);
        talkReply.setTalkOwnerId(talk.getUserId());
        talkReplyDao.create(talkReply);
        
        TalkReplySubscribe talkReplySubscribe = new TalkReplySubscribe();
        talkReplySubscribe.setTalkId(talkReplyValid.getTalkId());
        talkReplySubscribe.setUserId(userId);
        talkReplySubscribe.setReplyId(talkReplyValid.getSuperId() == null ? talkReply.getId() : talkReplyValid.getSuperId());
        if (talkReplySubscribeDao.findBySelect(talkReplySubscribe) == null)
            talkReplySubscribeDao.create(talkReplySubscribe);
        // 通知 talkId userId
    }

    @Override
    public void like(Long userId, Long talkId) {
        TalkLike talkLike = talkLikeDao.findTalkLike(userId, talkId);
        if (talkLike == null) {
            talkLike = new TalkLike();
            talkLike.setTalkId(talkId);
            talkLike.setUserId(userId);
            talkLikeDao.create(talkLike);
            return ;
        }
        if (talkLike.getStatus()) return ;
        talkLike.setStatus(true);
        talkLikeDao.update(talkLike);
    }

    @Override
    public void unlike(Long userId, Long talkId) {
        TalkLike talkLike = talkLikeDao.findTalkLike(userId, talkId);
        if (talkLike == null || !talkLike.getStatus()) return;
        talkLikeDao.tombstone(talkLike.getId());
    }

    @Override
    public List<TalkReply> talkReplyBySelect(Long talkId) {
        return talkReplyDao.listBySelect(talkId);
    }
}
