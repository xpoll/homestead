package cn.blmdz.home.services.service;

import java.util.List;

import cn.blmdz.home.base.BaseVo;
import cn.blmdz.home.enums.EnumsWeekly;
import cn.blmdz.home.model.Weekly;

/**
 * 凤凰周刊
 * @author yongzongyang
 * @date 2018年4月8日
 */
public interface WeeklyService {

    /**
     * 查找类目
     * @return
     */
    List<BaseVo<Integer, String>> category();
    
    
    /**
     * 按照类型查找
     * @param enums
     * @return
     */
    List<Weekly> findByType(EnumsWeekly enums);
}
