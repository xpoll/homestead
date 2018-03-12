package cn.blmdz.home.event;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.eventbus.AsyncEventBus;

@Component
public class EventAsyncListerner {

    @Autowired
    private AsyncEventBus eventBus;

    @PostConstruct
    public void register() {
        eventBus.register(this);
    }
    
    @PreDestroy
    public void unregister() {
    	eventBus.unregister(this);
    }
    
}
