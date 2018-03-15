package cn.blmdz.home.services.service.impl;

import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.MoreObjects;

import cn.blmdz.home.base.BaseUser;
import cn.blmdz.home.base.Constants;
import cn.blmdz.home.dao.UserAccountDao;
import cn.blmdz.home.dao.UserDao;
import cn.blmdz.home.dao.UserExtraDao;
import cn.blmdz.home.dao.UserThirdDao;
import cn.blmdz.home.enums.EnumsGender;
import cn.blmdz.home.enums.third.ThirdChannel;
import cn.blmdz.home.exception.WebJspException;
import cn.blmdz.home.model.User;
import cn.blmdz.home.model.UserAccount;
import cn.blmdz.home.model.UserExtra;
import cn.blmdz.home.model.UserThird;
import cn.blmdz.home.model.valid.LoginValid;
import cn.blmdz.home.model.valid.RegisterValid;
import cn.blmdz.home.model.valid.UserInfoValid;
import cn.blmdz.home.model.vo.UserInfoVo;
import cn.blmdz.home.services.service.UserService;
import cn.blmdz.home.util.CalculateUtil;
import cn.blmdz.home.util.EncryptUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
	private UserDao userDao;
    @Autowired
    private UserAccountDao userAccountDao;
    @Autowired
    private UserThirdDao userThirdDao;
    @Autowired
	private UserExtraDao userExtraDao;
	
	private String avatar = "http://www.i8wan.com/images/boy/p_boy.gif";

	@Override
	@Transactional
	public BaseUser registered(RegisterValid register) {
		log.info("{} registered.", register);
		UserThird userThird= userThirdDao.findByUsername(register.getChannel().value(), register.getName());
		if (userThird != null) throw new WebJspException("账号已存在");
		
		User user = new User();
		user.setNick(MoreObjects.firstNonNull(register.getNick(), register.getName()));
		user.setAvatar(MoreObjects.firstNonNull(register.getAvatar(), avatar));
		user.setVip(0);
		user.setLevel(1);
		user.setActiveDay(10);
		user.setGender(MoreObjects.firstNonNull(register.getGender(), EnumsGender.UNKNOWN.value()));
		user.setLoginTime(new Date());
		user.setSignDate(new Date());
		userDao.create(user);
		
		UserAccount userAccount = new UserAccount();
		userAccount.setUserId(user.getId());
		userAccount.setGold(0L);
		userAccount.setDiamond(0L);
		userAccountDao.create(userAccount);
		
		userThird = new UserThird();
		userThird.setUserId(user.getId());
		userThird.setChannelId(register.getChannel().value());
		userThird.setUsername(register.getName());
		if (register.getChannel().value() == ThirdChannel.COMMUNITY.value() || register.getChannel().value() == ThirdChannel.MOBILE.value() || register.getChannel().value() == ThirdChannel.EMAIL.value()) {
		    userThird.setPassword(EncryptUtil.encrypt(register.getPwd()));
		}
		userThird.setExtraJson(Constants.DEFAULT_JSON);
		userThirdDao.create(userThird);
		
		UserExtra userExtra = new UserExtra();
		userExtra.setUserId(user.getId());
		userExtra.setSchool(Constants.DEFAULT_EMPTY);
		userExtra.setBirthday(new Date());
		userExtra.setConstellation(CalculateUtil.constellation(userExtra.getBirthday()));
		userExtra.setCity(Constants.DEFAULT_EMPTY);
		userExtra.setDescription(Constants.DEFAULT_DESCRIPTION);
		userExtraDao.create(userExtra);
		
		BaseUser baseUser = new BaseUser();
		BeanUtils.copyProperties(user, baseUser);
		return baseUser;
	}

	@Override
	public BaseUser login(LoginValid login) {
		UserThird userThird= userThirdDao.findByUsername(login.getChannel().value(), login.getName());
		if (userThird == null) throw new WebJspException("用户不存在");
		if (!EncryptUtil.match(login.getPwd(), userThird.getPassword())) throw new WebJspException("密码不正确");
		User user = userDao.findById(userThird.getUserId());
		BaseUser baseUser = new BaseUser();
		BeanUtils.copyProperties(user, baseUser);
		return baseUser;
	}

	@Override
	public BaseUser recognition(RegisterValid register) {
	    UserThird userThird= userThirdDao.findByUsername(register.getChannel().value(), register.getName());
	    if (userThird != null) {
	        User user = userDao.findById(userThird.getUserId());
	        BaseUser baseUser = new BaseUser();
	        BeanUtils.copyProperties(user, baseUser);
	        return baseUser;
	    }
		return registered(register);
	}

    @Override
    public BaseUser getUser(Long userId) {
        if (userId == null) return null;
        User user = userDao.findById(userId);
        BaseUser baseUser = new BaseUser();
        BeanUtils.copyProperties(user, baseUser);
        return baseUser;
    }

	@Override
	public UserInfoVo findByid(Long id) {
	    User user = userDao.findById(id);
	    UserExtra extra = userExtraDao.findByUserId(id);
        UserInfoVo vo = new UserInfoVo();
        BeanUtils.copyProperties(extra, vo);
        BeanUtils.copyProperties(user, vo);
        vo.setNextActiveDay(CalculateUtil.activeDay(vo.getLevel() + 1));
        vo.setBackActiveDay(CalculateUtil.activeDay(vo.getLevel() - 1));
		return vo;
	}

    @Override
    public void updateUserInfo(Long id, UserInfoValid info) {
        User user = userDao.findById(id);
        UserExtra extra = userExtraDao.findByUserId(id);
        User updateUser = new User();
        updateUser.setId(user.getId());
        if (!user.getNick().equals(info.getNick())
                || !user.getGender().equals(info.getGender())) {
            updateUser.setNick(info.getNick());
            updateUser.setGender(info.getGender());
            userDao.update(updateUser);
        }
        UserExtra updateExtra = new UserExtra();
        updateExtra.setId(extra.getId());
        if (!extra.getSchool().equals(info.getSchool())
                || !extra.getBirthday().equals(info.getBirthday())
                || !extra.getCity().equals(info.getCity())
                || !extra.getDescription().equals(info.getDescription())) {
            updateExtra.setSchool(info.getSchool());
            updateExtra.setBirthday(info.getBirthday());
            updateExtra.setCity(info.getCity());
            updateExtra.setDescription(info.getDescription());
            updateExtra.setConstellation(CalculateUtil.constellation(info.getBirthday()));
            userExtraDao.update(updateExtra);
        }
    }

	@Override
	public void loginEvent(Long id) {
        User user = new User();
        user.setId(id);
        user.setLoginTime(new Date());
        userDao.update(user);
	}
}
