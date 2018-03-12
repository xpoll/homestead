package cn.blmdz.home.event;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.Subscribe;

import cn.blmdz.home.services.service.TalkService;

@Component
public class EventAsyncTalkListerner {

    @Autowired
    private AsyncEventBus eventBus;
    @Autowired
    private TalkService talkService;

    @PostConstruct
    public void register() {
        eventBus.register(this);
    }
    
    @PreDestroy
    public void unregister() {
    	eventBus.unregister(this);
    }

    
    @Subscribe
    @AllowConcurrentEvents
    public void loginEvent(TalkForwardEvent event) throws InterruptedException {
        talkService.forwardEvent(event);
    }
}
