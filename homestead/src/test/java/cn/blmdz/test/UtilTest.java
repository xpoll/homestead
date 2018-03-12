package cn.blmdz.test;

import java.util.List;

import org.assertj.core.util.Lists;

import cn.blmdz.home.model.base.SignData;
import cn.blmdz.home.model.base.SignData.Data;
import cn.blmdz.home.util.JsonMapper;

public class UtilTest {

	public static void main(String[] args) {
//		System.out.println(CalculateUtil.constellation(new Date()));
//
//		System.out.println(CalculateUtil.activeDay(5));
//		
//		System.out.println(CalculateUtil.level(10));
//
//        System.out.println("s12123123co".matches(Constants.COMMUNITY));
//        System.out.println("s12123123c@_- o".matches(Constants.PASSWORD));
        SignData sign = new SignData();
        sign.setBaseActive(10);
        sign.setBaseGold(800);
        
        List<Data> list = null;
        Data data = null;
        
        list = Lists.newArrayList();
        data = new Data(2, 1, 1); list.add(data);
        data = new Data(2, 2, 1); list.add(data);
        data = new Data(2, 3, 2); list.add(data);
        data = new Data(2, 4, 3); list.add(data);
        data = new Data(2, 5, 4); list.add(data);
        data = new Data(2, 6, 5); list.add(data);
        data = new Data(2, 7, 6); list.add(data);
        data = new Data(2, 8, 7); list.add(data);
        data = new Data(2, 9, 8); list.add(data);
        data = new Data(2, 10, 9); list.add(data);
        data = new Data(2, 11, 10); list.add(data);
        data = new Data(2, 12, 11); list.add(data);
        data = new Data(2, 13, 12); list.add(data);
        sign.setVipActives(list);
        sign.setVipDefaultActive(new Data(1, 0, 0));
        sign.setLevelDefaultActive(new Data(1, 0, 0));
        sign.setContinueActive(new Data(2, 0, 2));

        list = Lists.newArrayList();
        data = new Data(1, 1, 10); list.add(data);
        data = new Data(1, 2, 10); list.add(data);
        data = new Data(1, 3, 10); list.add(data);
        data = new Data(1, 4, 20); list.add(data);
        data = new Data(1, 5, 20); list.add(data);
        data = new Data(1, 6, 30); list.add(data);
        data = new Data(1, 7, 40); list.add(data);
        data = new Data(1, 8, 50); list.add(data);
        data = new Data(1, 9, 60); list.add(data);
        data = new Data(1, 10, 70); list.add(data);
        data = new Data(1, 11, 80); list.add(data);
        data = new Data(1, 12, 90); list.add(data);
        data = new Data(1, 13, 100); list.add(data);
        sign.setVipGolds(list);
        sign.setVipDefaultGold(new Data(1, 0, 0));
        sign.setLevelDefaultGold(new Data(1, 0, 0));
        sign.setContinueGold(new Data(1, 0, 20));
        
        System.out.println(JsonMapper.nonEmptyMapper().toJson(sign));
        
        
	}

}
