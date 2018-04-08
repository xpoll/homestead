package cn.blmdz.home.services.service.impl;

import java.util.List;

import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.blmdz.home.base.BaseVo;
import cn.blmdz.home.dao.WeeklyDao;
import cn.blmdz.home.enums.EnumsWeekly;
import cn.blmdz.home.model.Weekly;
import cn.blmdz.home.services.service.WeeklyService;

@Service
public class WeeklyServiceImpl implements WeeklyService {

    @Autowired
    private WeeklyDao weeklyDao;
    
    @Override
    public List<BaseVo<Integer, String>> category() {
        List<BaseVo<Integer, String>> lists = Lists.newArrayList();
        for (EnumsWeekly item : EnumsWeekly.values()) {
            lists.add(BaseVo.put(item.value(), item.desc()));
        }
        return lists;
    }

    @Override
    public List<Weekly> findByType(EnumsWeekly enums) {
        return weeklyDao.findByType(enums.value());
    }

}
