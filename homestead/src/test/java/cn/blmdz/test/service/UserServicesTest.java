package cn.blmdz.test.service;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import cn.blmdz.home.HomesteadApplication;
import cn.blmdz.home.base.BaseUser;
import cn.blmdz.home.base.Constants;
import cn.blmdz.home.enums.EnumsGender;
import cn.blmdz.home.enums.third.ThirdChannel;
import cn.blmdz.home.model.valid.LoginValid;
import cn.blmdz.home.model.valid.RegisterValid;
import cn.blmdz.home.model.valid.UserInfoValid;
import cn.blmdz.home.model.vo.UserInfoVo;
import cn.blmdz.home.services.service.UserService;
import cn.blmdz.home.util.CalculateUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = HomesteadApplication.class)
@WebAppConfiguration
//@Transactional
//@Rollback
@ActiveProfiles("local")
public class UserServicesTest {

    @Autowired
    UserService userService;
    
    
	@Test
	public void test() throws Exception {
		
		RegisterValid register = new RegisterValid();
		register.setName("18237149226");
		register.setPwd("blmdz521");
		register.setChannel(ThirdChannel.MOBILE);
		BaseUser baseUser = userService.registered(register);

        LoginValid login = new LoginValid();
        login.setChannel(register.getChannel());
        login.setName(register.getName());
        login.setPwd(register.getPwd());
	    baseUser = userService.login(login);
	    
	    System.out.println(baseUser);
	    UserInfoVo vo = userService.findByid(baseUser.getId());
	    
		System.out.println(vo);
		UserInfoValid info = new UserInfoValid();
		info.setNick("雨~轩辕紫嫣");
		info.setGender(EnumsGender.MALE.value());
		info.setCity(CalculateUtil.city().get(0));
		info.setBirthday(Constants.SDF_Y.parse(Constants.SDF_Y.format(new Date())));
		info.setSchool("商专");
		info.setDescription("你才懒呢！");
		userService.updateUserInfo(baseUser.getId(), info);
	}
}
