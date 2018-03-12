package cn.blmdz.home.event;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import cn.blmdz.home.services.service.UserService;

@Component
public class EventListerner {

    @Autowired
    private EventBus eventBus;
    @Autowired
    private UserService userService;

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
    public void loginEvent(LoginEvent event) throws InterruptedException {
    	userService.loginEvent(event.getId());
    }

}
