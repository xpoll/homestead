package cn.blmdz.home.services.cache;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import cn.blmdz.home.dao.UserDao;
import cn.blmdz.home.model.User;

@Component
public class UserCache {

    @Autowired
    private UserDao userDao;
    
    private final LoadingCache<Long, User> userCache;
    
    public UserCache() {
        userCache = CacheBuilder.newBuilder()
                .expireAfterWrite(15, TimeUnit.MINUTES)
                .build(new CacheLoader<Long, User>(){
                    @Override
                    public User load(Long key) throws Exception {
                        return userDao.findById(key);
                    }
                });
    }
    
    public User getUser(Long id) {
        if (id == null) return null;
        return userCache.getUnchecked(id);
    }
    
    public void refresh(Long id) {
        userCache.refresh(id);
    }
}
