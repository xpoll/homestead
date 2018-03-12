package cn.blmdz.home.services.cache;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.MoreObjects;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import cn.blmdz.home.dao.TalkDao;
import cn.blmdz.home.dao.TalkForwardDao;
import cn.blmdz.home.dao.TalkLikeDao;
import cn.blmdz.home.dao.TalkReplyDao;
import cn.blmdz.home.model.Talk;
import cn.blmdz.home.model.TalkForward;

/**
 * TALK缓存
 */
@Component
public class TalkCache {
    
    @Autowired
    private TalkDao talkDao;
    @Autowired
    private TalkForwardDao talkForwardDao;
    @Autowired
    private TalkReplyDao talkReplyDao;
    @Autowired
    private TalkLikeDao talkLikeDao;
    /**
     * 0 转发
     * 1 评论
     * 2 赞
     */
    private final LoadingCache<Long, Integer> forwardCache;
    private final LoadingCache<Long, Integer> replyCache;
    private final LoadingCache<Long, Integer> likeCache;
    private final LoadingCache<Long, Talk> talkCache;
    
    
    
    public TalkCache() {
    	forwardCache = CacheBuilder.newBuilder()
                .expireAfterWrite(1, TimeUnit.DAYS)
                .build(new CacheLoader<Long, Integer>(){
                    @Override
                    public Integer load(Long key) throws Exception {
                    	Integer a = MoreObjects.firstNonNull(talkForwardDao.countUpperNode(key), 0);
                        return a;
                    }
                });
    	replyCache = CacheBuilder.newBuilder()
                .expireAfterWrite(1, TimeUnit.DAYS)
                .build(new CacheLoader<Long, Integer>(){
                    @Override
                    public Integer load(Long key) throws Exception {
                        return MoreObjects.firstNonNull(talkReplyDao.countTalkReply(key), 0);
                    }
                });
    	likeCache = CacheBuilder.newBuilder()
                .expireAfterWrite(1, TimeUnit.DAYS)
                .build(new CacheLoader<Long, Integer>(){
                    @Override
                    public Integer load(Long key) throws Exception {
                        return MoreObjects.firstNonNull(talkLikeDao.countTalkLike(key), 0);
                    }
                });
        talkCache = CacheBuilder.newBuilder()
                .expireAfterWrite(15, TimeUnit.MINUTES)
                .build(new CacheLoader<Long, Talk>(){
                    @Override
                    public Talk load(Long key) throws Exception {
                        return talkDao.findById(key);
                    }
                });
    }

    /**
     * 查看TALK 被操作数量
     * @param talkId
     * @return
     * 0 转发
     * 1 评论
     * 2 赞
     */
    public Integer[] talkCount(Long talkId) {
        if (talkId == null) return new Integer[]{0, 0, 0};
        return new Integer[]{
                forwardCache.getUnchecked(talkId),
                replyCache.getUnchecked(talkId),
                likeCache.getUnchecked(talkId),
        };
    }
    
    public Talk getTalk(Long talkId) {
        if (talkId == null) return null;
        return talkCache.getUnchecked(talkId);
    }

    
    public void refreshForward(Long talkId) {
    	forwardCache.refresh(talkId);
    	List<TalkForward> lists = talkForwardDao.findUnderNode(talkId);
    	for (TalkForward talkForward : lists) {
    		forwardCache.refresh(talkForward.getBtalkId());
		}
    	forwardCache.refresh(talkId);
    }
    public void refreshReply(Long talkId) {
    	replyCache.refresh(talkId);
    }
    public void refreshLike(Long talkId) {
    	likeCache.refresh(talkId);
    }
    
}
