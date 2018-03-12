package cn.blmdz.home.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.eventbus.EventBus;

import cn.blmdz.home.base.BaseUser;
import cn.blmdz.home.base.Response;
import cn.blmdz.home.captcha.Captcha;
import cn.blmdz.home.enums.third.ThirdChannel;
import cn.blmdz.home.event.LoginEvent;
import cn.blmdz.home.exception.WebJspException;
import cn.blmdz.home.model.valid.LoginValid;
import cn.blmdz.home.model.valid.RegisterValid;
import cn.blmdz.home.model.vo.SignVo;
import cn.blmdz.home.model.vo.UserInfoVo;
import cn.blmdz.home.services.manager.UserManager;
import cn.blmdz.home.services.service.UserService;
import cn.blmdz.home.util.CalculateUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value="/api/user")
public class RestUserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserManager userManager;

    @Autowired
    private EventBus eventBus;
    
    /**
     * 当前用户
     */
    @RequestMapping(method=RequestMethod.GET)
    public Response<BaseUser> user(HttpServletRequest request) {
    	BaseUser baseUser = (BaseUser) request.getSession().getAttribute("user");
        log.info("current user: {}", baseUser);
    	return Response.build(baseUser);
    }
    
    /**
     * 登陆
     */
    @RequestMapping(value="login", method=RequestMethod.POST)
    public Response<Boolean> login(@RequestBody @Valid LoginValid login, HttpServletRequest request) {
    	login.setChannel(CalculateUtil.channel(login.getName().trim().toLowerCase()));
    	login.setName(login.getName().trim());
        BaseUser baseUser = userService.login(login);
        log.info("login user: {}", baseUser);
        
        request.getSession().setAttribute("user", baseUser);
        eventBus.post(new LoginEvent(baseUser.getId()));
        
        return Response.ok();
    }

    /**
     * 注册
     */
    @RequestMapping(value="registered", method=RequestMethod.POST)
    public Response<BaseUser> registered(@RequestBody @Valid RegisterValid register, HttpServletRequest request) {
        if (!register.getCode().equalsIgnoreCase((String)request.getSession().getAttribute(Captcha.TOKEN))) throw new WebJspException("验证码不正确");
        
        ThirdChannel channel = CalculateUtil.channel(register.getName().trim().toLowerCase());
        if (channel == null) throw new WebJspException("账号格式不正确");
        register.setChannel(channel);
        register.setName(register.getName().trim());
        BaseUser baseUser = userService.registered(register);
        log.info("login user: {}", baseUser);
        
        request.getSession().setAttribute("user", baseUser);
        eventBus.post(new LoginEvent(baseUser.getId()));
        
        return Response.build(baseUser);
    }
    
    /**
     * 当前用户基本信息
     */
    @RequestMapping(value="info", method=RequestMethod.GET)
    public Response<UserInfoVo> info(HttpServletRequest request) {
        BaseUser baseUser = (BaseUser) request.getSession().getAttribute("user");
        if (baseUser == null) return Response.build(null);
        
        return Response.build(userService.findByid(baseUser.getId()));
    }
    
    /**
     * 用户基本信息
     */
    @RequestMapping(value="base", method=RequestMethod.GET)
    public Response<BaseUser> info(HttpServletRequest request, @RequestParam("userId") Long userId) {
        if (userId == null) return Response.build(null);
        
        return Response.build(userService.getUser(userId));
    }

    /**
     * 用户签到
     */
    @RequestMapping(value="sign", method=RequestMethod.GET)
    public Response<SignVo> sign(HttpServletRequest request) {
        BaseUser baseUser = (BaseUser) request.getSession().getAttribute("user");
        if (baseUser == null) return Response.build(null);
        
        SignVo vo = userManager.sign(baseUser.getId());
        
        baseUser.setLevel(vo.getLevel());
        baseUser.setSignDate(new Date());
        request.getSession().setAttribute("user", baseUser);
        
        return Response.build(vo);
        
    }
    
}
