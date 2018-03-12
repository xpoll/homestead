package cn.blmdz.home.model.third;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import cn.blmdz.home.enums.third.AlipayMarketingCardActivateformEnums;
import cn.blmdz.home.util.JsonMapper;
import lombok.Data;

/**
 * 阿里领卡用户表单信息
 * 
 * @author yongzongyang
 * @date 2017年11月13日
 */
@Data
public class AlipayUserInfo {
    
    private String mobile;
    private String gender;
    private String name;
    private String birthday;
    private String mail;
    
    public AlipayUserInfo init(String infos) {
        List<Map<String, String>> list = JsonMapper.nonDefaultMapper().fromJson(infos, JsonMapper.nonDefaultMapper().createCollectionType(List.class, Map.class));
        Map<String, String> maps = Maps.newConcurrentMap();
        for (Map<String, String> map : list) {
            maps.putAll(map);
        }
        this.mobile = maps.get(AlipayMarketingCardActivateformEnums.OPEN_FORM_FIELD_MOBILE);
        this.gender = "男".equals(maps.get(AlipayMarketingCardActivateformEnums.OPEN_FORM_FIELD_GENDER)) ? "MALE" : "FEMALE";
        this.name = maps.get(AlipayMarketingCardActivateformEnums.OPEN_FORM_FIELD_NAME);
        this.birthday = maps.get(AlipayMarketingCardActivateformEnums.OPEN_FORM_FIELD_BIRTHDAY_WITH_YEAR);
        this.mail = maps.get(AlipayMarketingCardActivateformEnums.OPEN_FORM_FIELD_EMAIL);
        return this;
    }
}
