package cn.blmdz.home.dao;

import cn.blmdz.home.model.BaseData;

public interface BaseDataDao extends BaseDao<BaseData> {
    
    /**
     * 通过type查找
     * @param type
     * @return
     */
    BaseData findByType(Integer type);
	
}