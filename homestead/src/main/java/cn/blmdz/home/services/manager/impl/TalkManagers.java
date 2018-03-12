package cn.blmdz.home.services.manager.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cn.blmdz.home.base.BasePage;
import cn.blmdz.home.dao.TalkDao;
import cn.blmdz.home.dao.TalkReplyDao;
import cn.blmdz.home.model.Talk;
import cn.blmdz.home.model.TalkReply;
import cn.blmdz.home.model.search.TalkSearch;
import cn.blmdz.home.model.vo.TalkReplyVo;
import cn.blmdz.home.model.vo.TalkReplysVo;
import cn.blmdz.home.model.vo.TalkVo;
import cn.blmdz.home.model.vo.TalkVo.TalkForwardVo;
import cn.blmdz.home.services.cache.TalkCache;
import cn.blmdz.home.services.cache.UserCache;
import cn.blmdz.home.services.manager.TalkManager;

@Component
public class TalkManagers implements TalkManager {

    @Autowired
    private TalkDao talkDao;
    @Autowired
    private TalkReplyDao talkReplyDao;
    @Autowired
    private TalkCache talkCache;
    @Autowired
    private UserCache userCache;
    
    @Override
    public TalkVo firstTalkByUserId(TalkSearch talkSearch) {
        // lookUserId 用于用户好友判断 暂时未用 TODO
        if (talkSearch.getLookUserId() == talkSearch.getUserId()) talkSearch.setPrivacy(2);
        
        Talk talk = talkDao.findFirstTalkByUserId(talkSearch.getUserId(), talkSearch.getPrivacy());
        TalkVo vo = talk(talk, false);
        return vo;
    }

    @Override
    public BasePage<TalkSearch, TalkVo> pageTalkByUserId(BasePage<TalkSearch, TalkVo> search) {
        // lookUserId 用于用户好友判断 暂时未用 TODO
        if (search.getMode().getLookUserId() == search.getMode().getUserId()) search.getMode().setPrivacy(2);
        
        PageHelper.startPage(search.getNum(), search.getSize(), search.getOrder());
        Page<Talk> page = talkDao.pageBySelect(search.getMode());
        Page<TalkVo> pageVo = new Page<>();
        pageVo.setTotal(page.getTotal());
        for (Talk talk : page) {
            pageVo.add(talk(talk, true));
        }
        search.setPage(pageVo);
        return search;
    }

    /**
     * TALKVO 封装
     */
    private TalkVo talk(Talk talk, Boolean reply) {
        if (talk == null) return null;
        
        TalkVo vo = new TalkVo();
        vo.setId(talk.getId());
        vo.setNick(userCache.getUser(talk.getUserId()).getNick());
        vo.setContent(talk.getContent());
        vo.setPrivacy(talk.getPrivacy());
        vo.setOriginal(talk.getOriginal());
        vo.setCreateTime(talk.getCreateTime());
        Integer[] count = talkCache.talkCount(talk.getId());
        vo.setForward(count[0]);
        vo.setReply(count[1]);
        vo.setLike(count[2]);
        
        if (!talk.getOriginal()) {
            Talk original = talkCache.getTalk(talk.getTalkId());
            if (!original.getStatus()) return vo;
            TalkForwardVo talkForwardVo = new TalkForwardVo();
            talkForwardVo.setId(talk.getTalkId());
            talkForwardVo.setUserId(original.getUserId());
            talkForwardVo.setNick(userCache.getUser(original.getUserId()).getNick());
            talkForwardVo.setContent(original.getContent());
            talkForwardVo.setCreateTime(original.getCreateTime());
            vo.setTalkForwardVo(talkForwardVo);
        }
        vo.setReplys(reply ? findReplysByTalkId(talk.getId()) : null);
        return vo;
    }
    
    /**
     * 根据TALK获取评论回复封装类
     * @param talkId
     * @return
     */
    private List<TalkReplyVo> findReplysByTalkId(Long talkId) {

        // 回复排序归类
        List<TalkReply> replys = talkReplyDao.listBySelect(talkId);
        if (CollectionUtils.isEmpty(replys)) return null;
        
        Map<Long, List<TalkReply>> maps = Maps.newTreeMap();// key: TalkReply ID; value: all TalkReply
        for (TalkReply talkReply : replys) {
            Long id = MoreObjects.firstNonNull(talkReply.getSuperId(), talkReply.getId());
            if (maps.containsKey(id)) {
                maps.get(id).add(talkReply);
            } else {
                List<TalkReply> list = Lists.newArrayList();
                list.add(talkReply);
                maps.put(id, list);
            }
        }
        // 组装Vo
        List<TalkReplyVo> talkReplyVos = Lists.newArrayList();
        for (Long id : maps.keySet()) {
            TalkReplyVo talkReplyVo = new TalkReplyVo();
            TalkReply talkReply = maps.get(id).get(maps.get(id).size() - 1);
            BeanUtils.copyProperties(talkReply, talkReplyVo);
            talkReplyVo.setNick(userCache.getUser(talkReplyVo.getUserId()).getNick());
            if (maps.get(id).size() > 1){
                List<TalkReplysVo> talkReplysVos = Lists.newArrayList();
                for (int i = maps.get(id).size() - 2; i > -1; i--) {
                    TalkReplysVo talkReplysVo = new TalkReplysVo();
                    talkReply = maps.get(id).get(i);
                    BeanUtils.copyProperties(talkReply, talkReplysVo);
                    talkReplysVo.setNick(userCache.getUser(talkReplysVo.getUserId()).getNick());
                    talkReplysVo.setReplayToNick(userCache.getUser(talkReplysVo.getReplyToId()).getNick());
                    talkReplysVos.add(talkReplysVo);
                }
                talkReplyVo.setReplys(talkReplysVos);
            }
            talkReplyVos.add(talkReplyVo);
        }
        return talkReplyVos;
    }

}
