package cn.blmdz.home.services.cache;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.google.common.base.MoreObjects;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import cn.blmdz.home.base.Constants;
import cn.blmdz.home.dao.BaseDataDao;
import cn.blmdz.home.enums.EnumsBaseDataType;
import cn.blmdz.home.model.BaseData;
import cn.blmdz.home.model.base.SignData;
import cn.blmdz.home.model.base.SignData.Data;
import cn.blmdz.home.util.JsonMapper;

@Component
public class BaseDataCache {

	@Autowired
	private BaseDataDao baseDataDao;
	private final LoadingCache<Integer, String> baseDateCache;
	
	public BaseDataCache() {
	    baseDateCache = CacheBuilder.newBuilder()
				.expireAfterWrite(1, TimeUnit.DAYS)
				.build(new CacheLoader<Integer, String>(){
					@Override
					public String load(Integer key) throws Exception {
					    BaseData baseData = baseDataDao.findByType(key);
					    if (baseData == null) return Constants.DEFAULT_JSON;
					    return MoreObjects.firstNonNull(baseData.getDataJson(), Constants.DEFAULT_JSON);
					}
				});
	}
	
	/**
	 * 签到基础活跃天
	 */
	public Integer signBaseActive() {
	    return sign().getBaseActive();
	}

    
    /**
     * 签到基础金币
     */
	public Integer signBaseGold() {
	    return sign().getBaseGold();
	}

    /**
     * 连续签到活跃天加成
     */
    public Integer signContinueActive() {
        SignData sign = sign();
        
        return sign(sign.getContinueActive().getType(), sign.getBaseActive(), sign.getContinueActive().getValue());
    }

	/**
	 * 签到活跃天VIP加成
	 */
	public Integer signVipActive(Integer vip) {
        SignData sign = sign();
        if (sign == null) return 0;
        
        if (!CollectionUtils.isEmpty(sign.getVipActives()))
            for (Data data : sign.getVipActives()) {
                if (data.getKey() != vip) continue;
                return sign(data.getType(), sign.getBaseActive(), data.getValue());
            }
        return sign(sign.getVipDefaultActive().getType(), sign.getBaseActive(), sign.getVipDefaultActive().getValue());
    }

	/**
	 * 签到活跃天等级加成
	 */
	public Integer signLevelActive(Integer level) {
        SignData sign = sign();
        if (sign == null) return 0;
        
        if (!CollectionUtils.isEmpty(sign.getLevelActives()))
            for (Data data : sign.getLevelActives()) {
                if (data.getKey() != level) continue;
                return sign(data.getType(), sign.getBaseActive(), data.getValue());
            }
        return sign(sign.getLevelDefaultActive().getType(), sign.getBaseActive(), sign.getLevelDefaultActive().getValue());
    }
    
    /**
     * 连续签到金币加成
     */
    public Integer signContinueGold() {
        SignData sign = sign();
        
        return sign(sign.getContinueGold().getType(), sign.getBaseGold(), sign.getContinueGold().getValue());
    }

	/**
	 * 签到金币VIP加成
	 */
	public Integer signVipGold(Integer vip) {
        SignData sign = sign();
        if (sign == null) return 0;

        if (!CollectionUtils.isEmpty(sign.getVipGolds()))
            for (Data data : sign.getVipGolds()) {
                if (data.getKey() != vip) continue;
                return sign(data.getType(), sign.getBaseGold(), data.getValue());
            }
        return sign(sign.getVipDefaultGold().getType(), sign.getBaseGold(), sign.getVipDefaultGold().getValue());
    }

	/**
	 * 签到金币等级加成
	 */
	public Integer signLevelGold(Integer level) {
	    SignData sign = sign();
        if (sign == null) return 0;

        if (!CollectionUtils.isEmpty(sign.getLevelGolds()))
    	    for (Data data : sign.getLevelGolds()) {
    	        if (data.getKey() != level) continue;
    	        return sign(data.getType(), sign.getBaseGold(), data.getValue());
    	    }
        return sign(sign.getLevelDefaultGold().getType(), sign.getBaseGold(), sign.getLevelDefaultGold().getValue());
	}
    
    /**
     * 签到比例累加计算
     */
    private Integer sign(Integer type, Integer base, Integer value) {
        
        if (type == 1) return (int) (base * value * 0.01);
        else if (type == 2) return value;
        
        return 0;
    }
	
	private SignData sign() {
	    return JsonMapper.nonEmptyMapper().fromJson(baseDate(EnumsBaseDataType.SIGN), SignData.class);
	}
	
	private String baseDate(EnumsBaseDataType type) {
	    return baseDateCache.getUnchecked(type.value());
	}
	
	public void refresh() {
	    for (EnumsBaseDataType item : EnumsBaseDataType.values()) {
	        baseDateCache.refresh(item.value());
        }
	}
}
