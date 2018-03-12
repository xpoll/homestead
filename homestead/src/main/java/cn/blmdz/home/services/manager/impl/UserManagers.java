package cn.blmdz.home.services.manager.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.blmdz.home.dao.UserAccountDao;
import cn.blmdz.home.dao.UserDao;
import cn.blmdz.home.exception.WebJspException;
import cn.blmdz.home.model.User;
import cn.blmdz.home.model.UserAccount;
import cn.blmdz.home.model.vo.SignVo;
import cn.blmdz.home.services.cache.BaseDataCache;
import cn.blmdz.home.services.manager.UserManager;
import cn.blmdz.home.util.CalculateUtil;

@Component
public class UserManagers implements UserManager {

	@Autowired
	private UserDao userDao;
	@Autowired
	private UserAccountDao userAccountDao;
	@Autowired
	private BaseDataCache baseDataCache;
	
	@Override
	public SignVo sign(Long userId) {

		User user = userDao.findById(userId);
		if (!CalculateUtil.sign(user.getSignDate())) throw new WebJspException("已签过到了");
		
		Boolean signContinue = CalculateUtil.signContinue(user.getSignDate());
		
        Integer addActiveDay = baseDataCache.signBaseActive()
                + baseDataCache.signLevelActive(user.getLevel())
                + baseDataCache.signVipActive(user.getVip())
                + (signContinue ? baseDataCache.signContinueActive() : 0);
        Integer addGold = baseDataCache.signBaseGold()
                + baseDataCache.signLevelGold(user.getLevel())
                + baseDataCache.signVipGold(user.getVip())
                + (signContinue ? baseDataCache.signContinueGold() : 0);
        
        SignVo vo = new SignVo();
        vo.setAddActiveDay(addActiveDay);
        vo.setAddGold(addGold);
        
        UserAccount userAccount = userAccountDao.findByUserId(userId);
		user.setActiveDay(user.getActiveDay() + addActiveDay);
		userAccount.setGold(userAccount.getGold() + addGold);
		user.setSignDate(new Date());
		
		Integer level = CalculateUtil.level(user.getActiveDay());
		vo.setUpgrade(!user.getLevel().equals(level));
		user.setLevel(level);
		
        vo.setActiveDay(user.getActiveDay());
        vo.setLevel(user.getLevel());
        
		userDao.update(user);
		userAccountDao.update(userAccount);
		return vo;
	}

}
