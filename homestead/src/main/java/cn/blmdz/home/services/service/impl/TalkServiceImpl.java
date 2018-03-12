package cn.blmdz.home.services.service.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.PageHelper;

import cn.blmdz.home.base.BasePage;
import cn.blmdz.home.dao.TalkDao;
import cn.blmdz.home.dao.TalkForwardDao;
import cn.blmdz.home.dao.TalkLikeDao;
import cn.blmdz.home.dao.TalkReplyDao;
import cn.blmdz.home.dao.TalkReplySubscribeDao;
import cn.blmdz.home.event.TalkForwardEvent;
import cn.blmdz.home.exception.WebJspException;
import cn.blmdz.home.model.Talk;
import cn.blmdz.home.model.TalkForward;
import cn.blmdz.home.model.TalkLike;
import cn.blmdz.home.model.TalkReply;
import cn.blmdz.home.model.TalkReplySubscribe;
import cn.blmdz.home.model.valid.TalkForwardValid;
import cn.blmdz.home.model.valid.TalkReplyValid;
import cn.blmdz.home.model.valid.TalkValid;
import cn.blmdz.home.services.cache.TalkCache;
import cn.blmdz.home.services.service.TalkService;

@Service
public class TalkServiceImpl implements TalkService {
    
    @Autowired
    private TalkDao talkDao;
    @Autowired
    private TalkForwardDao talkForwardDao;
    @Autowired
    private TalkReplyDao talkReplyDao;
    @Autowired
    private TalkReplySubscribeDao talkReplySubscribeDao;
    @Autowired
    private TalkLikeDao talkLikeDao;
    @Autowired
    private TalkCache talkCache;

    @Override
    public void issue(Long userId, TalkValid talkValid) {
        Talk talk = new Talk();
        BeanUtils.copyProperties(talkValid, talk);
        talk.setUserId(userId);
        talk.setTalkId(-1L);
        talkDao.create(talk);
    }

    @Override
    public Long forward(Long userId, TalkForwardValid talkForwardValid) {
        Talk original = talkDao.findById(talkForwardValid.getTalkId());
        if (original == null || !original.getStatus()) throw new WebJspException("TALK不存在或已被删除");
        
        // 创建TALK
        Talk talk = new Talk();
        BeanUtils.copyProperties(talkForwardValid, talk);
        talk.setUserId(userId);
        talk.setTalkId(original.getOriginal() ? original.getId() : original.getTalkId());
        talkDao.create(talk);
        // 创建转发记录在异步通知内
        return talk.getId();
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
    public BasePage<Long, TalkForward> pageTalkForwardBySelect(BasePage<Long, TalkForward> search) {
        PageHelper.startPage(search.getNum(), search.getSize(), search.getOrder());
        search.setPage(talkForwardDao.pageBySelect(search.getMode()));
        return search;
    }

    @Override
    public List<TalkReply> talkReplyBySelect(Long talkId) {
        return talkReplyDao.listBySelect(talkId);
    }

    @Override
    public void forwardEvent(TalkForwardEvent event) {
        Talk talk = talkDao.findById(event.getTalkId());
    	
        // 创建转发记录
        List<TalkForward> unders = talkForwardDao.findUnderNode(event.getBtalkId());
        if (!CollectionUtils.isEmpty(unders)) {
            for (TalkForward under : unders) {
                TalkForward talkForward = new TalkForward();
                talkForward.setBtalkId(under.getBtalkId());
                talkForward.setTalkId(event.getTalkId());
                talkForward.setCreateTime(talk.getCreateTime());
                talkForward.setUpdateTime(talk.getCreateTime());
                talkForwardDao.create(talkForward);
            	talkCache.refreshForward(under.getBtalkId());
            }
        }
        TalkForward talkForward = new TalkForward();
        talkForward.setBtalkId(event.getBtalkId());
        talkForward.setTalkId(event.getTalkId());
        talkForward.setCreateTime(talk.getCreateTime());
        talkForward.setUpdateTime(talk.getCreateTime());
        talkForwardDao.create(talkForward);
    	talkCache.refreshForward(event.getBtalkId());
    }
}
