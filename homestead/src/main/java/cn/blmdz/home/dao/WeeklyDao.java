package cn.blmdz.home.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.blmdz.home.model.Weekly;

public interface WeeklyDao extends BaseDao<Weekly> {

    List<Weekly> findByType(@Param("type") Integer type);
}
