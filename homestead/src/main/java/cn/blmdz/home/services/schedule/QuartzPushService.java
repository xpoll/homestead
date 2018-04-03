package cn.blmdz.home.services.schedule;

import org.springframework.stereotype.Component;

import cn.blmdz.home.baiduyun.BaiduyunService;

@Component
public class QuartzPushService {
    
    private BaiduyunService baiduyunService;

//    @Scheduled(cron = "0/1 * * * * ?")
    public void sab() {
        baiduyunService.getFileInfo(null, "dEWWnYD", "cfhk", true);
        System.out.println(System.currentTimeMillis());
    }
}
