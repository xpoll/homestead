package cn.blmdz.home.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.blmdz.home.base.BaseVo;
import cn.blmdz.home.base.Response;
import cn.blmdz.home.enums.EnumsWeekly;
import cn.blmdz.home.model.Weekly;
import cn.blmdz.home.services.service.WeeklyService;

@RestController
@RequestMapping(value="/api/weekly")
public class RestWeeklyController {

	@Autowired
	private WeeklyService weeklyService;
	
//	@Autowired
//	private QuartzPushService quartzPushService;
//
//    /**
//     * 执行
//     */
//    @RequestMapping(value="/doing", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
//    public void start() {
//        try {
//            quartzPushService.doing();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 类目
     */
    @RequestMapping(value="", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
    public Response<List<BaseVo<Integer, String>>> category() {
        return Response.build(weeklyService.category());
    }
	

	/**
	 * 周刊列表
	 */
	@RequestMapping(value="/list", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public Response<List<Weekly>> weeklys(@RequestParam("type") Integer type) {
		return Response.build(weeklyService.findByType(EnumsWeekly.from(type)));
	}
}
