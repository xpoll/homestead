package cn.blmdz.home.services.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class QuartzPushService {

//    @Scheduled(cron = "0/1 * * * * ?")
    public void sab() {
        System.out.println(System.currentTimeMillis());
    }
}
