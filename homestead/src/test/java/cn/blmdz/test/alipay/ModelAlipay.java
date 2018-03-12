package cn.blmdz.test.alipay;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alipay.api.domain.AlipayMarketingCardActivateurlApplyModel;
import com.alipay.api.domain.AlipayMarketingCardTemplateCreateModel;
import com.alipay.api.domain.AlipayMarketingCardTemplateModifyModel;
import com.alipay.api.domain.MoreInfoDTO;
import com.alipay.api.domain.TemplateBenefitInfoDTO;
import com.alipay.api.domain.TemplateCardLevelConfDTO;
import com.alipay.api.domain.TemplateColumnInfoDTO;
import com.alipay.api.domain.TemplateFieldRuleDTO;
import com.alipay.api.domain.TemplateStyleInfoDTO;

import cn.blmdz.home.util.JsonMapper;

/**
 * model封装
 * @author yongzongyang
 */
public class ModelAlipay {
	
	/**
	 * 会员卡模板创建 TODO
	 * 有些在修改模板里
	 * 
	 * @param prefix 卡前缀
	 * @param writeOffType 卡类型
	 * @param name 卡名字
	 * @param logo_id logo图ID
	 * @param color 背景色
	 * @param bg_id 背景图ID
	 * @param sign 标志
	 * @param url 服务器域名
	 */
	public static AlipayMarketingCardTemplateCreateModel alipayMarketingCardTemplateCreateModel(
			String prefix,
			String writeOffType,
			String name,
			String logo_id,
			String color,
			String bg_id,
			String sign,
			String url
			) {
		AlipayMarketingCardTemplateCreateModel model = new AlipayMarketingCardTemplateCreateModel();
		model.setRequestId(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));
		model.setCardType("OUT_MEMBER_CARD");
		model.setBizNoPrefix(prefix);
		model.setBizNoSuffixLen("10");
		model.setWriteOffType(writeOffType);
		
		//模板样式信息--必须
		TemplateStyleInfoDTO templateStyleInfo = new TemplateStyleInfoDTO();
		templateStyleInfo.setCardShowName(name);
		templateStyleInfo.setLogoId(logo_id); 
		templateStyleInfo.setColor(color);
		templateStyleInfo.setBackgroundId(bg_id); 
		templateStyleInfo.setBgColor(color);
		templateStyleInfo.setSlogan("会员权益享不停");
		templateStyleInfo.setSloganImgId("1T8Pp00AT7eo9NoAJkMR3AAAACMAAQEC");
		templateStyleInfo.setBrandName("魔寒居");
		model.setTemplateStyleInfo(templateStyleInfo);
		
		
		List<TemplateBenefitInfoDTO> benefitInfoList = new ArrayList<>();
		TemplateBenefitInfoDTO templateBenefitInfoDTO = new TemplateBenefitInfoDTO();
		templateBenefitInfoDTO.setTitle("优惠啊");
		List<String> benefitDesc = new ArrayList<>();
        benefitDesc.add("优惠啊1");
        benefitDesc.add("优惠啊优惠啊2");
        benefitDesc.add("优惠啊优惠啊优惠啊3");
		templateBenefitInfoDTO.setBenefitDesc(benefitDesc);
		Calendar max = Calendar.getInstance();
		templateBenefitInfoDTO.setStartDate(max.getTime());
		max.set(Calendar.YEAR, max.get(Calendar.YEAR) + 1);
		templateBenefitInfoDTO.setEndDate(max.getTime());
		model.setTemplateBenefitInfo(benefitInfoList);

		
		//栏位信息--必须
		List<TemplateColumnInfoDTO> columnInfoList = new ArrayList<>();

        TemplateColumnInfoDTO templateColumnInfoDTO = null;
        MoreInfoDTO moreInfo = null;

        templateColumnInfoDTO = new TemplateColumnInfoDTO();
        templateColumnInfoDTO.setCode("QXX_HOME");
        moreInfo = new MoreInfoDTO();
//      moreInfo.setParams("{\"third\":\"alipay\"}");
        moreInfo.setTitle("千酌一梦醉独殇");
        moreInfo.setUrl(url);
        templateColumnInfoDTO.setMoreInfo(moreInfo);
        templateColumnInfoDTO.setTitle("首页");
        templateColumnInfoDTO.setOperateType("openWeb");
        templateColumnInfoDTO.setValue("进入首页");
        columnInfoList.add(templateColumnInfoDTO);
		
		model.setColumnInfoList(columnInfoList);
		
		//字段规则列表--必须
		List<TemplateFieldRuleDTO> fieldRuleList = new ArrayList<>();
		TemplateFieldRuleDTO templateFieldRuleDTO = new TemplateFieldRuleDTO();
		templateFieldRuleDTO.setFieldName("ValidDate");
		templateFieldRuleDTO.setRuleName("ASSIGN_FROM_REQUEST");
		templateFieldRuleDTO.setRuleValue("ValidDate");
		fieldRuleList.add(templateFieldRuleDTO);
		model.setFieldRuleList(fieldRuleList);

		return model;
	}
	

	/**
	 * 会员卡模板更新 TODO
	 * 
	 * @param templateId 模板ID
	 * @param prefix 卡前缀
	 * @param writeOffType 卡类型
	 * @param name 卡名字
	 * @param logo_id logo图ID
	 * @param color 背景色
	 * @param bg_id 背景图ID
	 * @param sign 标志
	 * @param url 服务器域名
	 * @param shops 店铺号，逗号隔开
	 * @param shop 店铺会员卡展示为true
	 */
	public static AlipayMarketingCardTemplateModifyModel alipayMarketingCardTemplateModifyModel(
			String templateId,
			String prefix,
			String writeOffType,
			String name,
			String logo_id,
			String color,
			String bg_id,
			String sign,
			String url
			) {
		AlipayMarketingCardTemplateModifyModel model = new AlipayMarketingCardTemplateModifyModel();
		model.setRequestId(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));
		model.setBizNoPrefix(prefix);
		model.setWriteOffType(writeOffType);
		model.setTemplateId(templateId);
		
		//模板样式信息--必须
		TemplateStyleInfoDTO templateStyleInfo = new TemplateStyleInfoDTO();
		templateStyleInfo.setCardShowName(name);
		templateStyleInfo.setLogoId(logo_id); 
		templateStyleInfo.setColor(color);
		templateStyleInfo.setBackgroundId(bg_id); 
		templateStyleInfo.setBgColor(color);
        templateStyleInfo.setSlogan("会员权益享不停");
        templateStyleInfo.setSloganImgId("1T8Pp00AT7eo9NoAJkMR3AAAACMAAQEC");
        templateStyleInfo.setBrandName("魔寒居");
        model.setTemplateStyleInfo(templateStyleInfo);
        
        List<TemplateBenefitInfoDTO> benefitInfoList = new ArrayList<>();
        TemplateBenefitInfoDTO templateBenefitInfoDTO = new TemplateBenefitInfoDTO();
        templateBenefitInfoDTO.setTitle("优惠啊");
        List<String> benefitDesc = new ArrayList<>();
        benefitDesc.add("优惠啊1");
        benefitDesc.add("优惠啊优惠啊2");
        benefitDesc.add("优惠啊优惠啊优惠啊3");
        templateBenefitInfoDTO.setBenefitDesc(benefitDesc);
        Calendar max = Calendar.getInstance();
        templateBenefitInfoDTO.setStartDate(max.getTime());
        max.set(Calendar.YEAR, max.get(Calendar.YEAR) + 1);
        templateBenefitInfoDTO.setEndDate(max.getTime());
        benefitInfoList.add(templateBenefitInfoDTO);
        model.setTemplateBenefitInfo(benefitInfoList);
		
		//栏位信息--必须
		List<TemplateColumnInfoDTO> columnInfoList = new ArrayList<>();
		TemplateColumnInfoDTO templateColumnInfoDTO = null;
		MoreInfoDTO moreInfo = null;

        templateColumnInfoDTO = new TemplateColumnInfoDTO();
        templateColumnInfoDTO.setCode("QXX_HOME");
        moreInfo = new MoreInfoDTO();
//      moreInfo.setParams("{\"third\":\"alipay\"}");
        moreInfo.setTitle("首页");
        moreInfo.setUrl(url);
        templateColumnInfoDTO.setMoreInfo(moreInfo);
        templateColumnInfoDTO.setTitle("首页");
        templateColumnInfoDTO.setOperateType("openWeb");
        templateColumnInfoDTO.setValue("进入首页");
        columnInfoList.add(templateColumnInfoDTO);
        
        templateColumnInfoDTO = new TemplateColumnInfoDTO();
        templateColumnInfoDTO.setCode("QXX_MORE");
        moreInfo = new MoreInfoDTO();
        moreInfo.setTitle("叮咚");
        List<String> descs = new ArrayList<>();
        descs.add("叮咚。。");
        descs.add("叮咚叮咚。。");
        descs.add("叮咚叮咚叮咚。。");
        descs.add("叮咚叮咚叮咚叮咚。。");
        moreInfo.setDescs(descs);
        templateColumnInfoDTO.setMoreInfo(moreInfo);
        templateColumnInfoDTO.setTitle("千酌一梦醉独殇");
        templateColumnInfoDTO.setOperateType("openNative");
        templateColumnInfoDTO.setValue("更多");
        columnInfoList.add(templateColumnInfoDTO);
        
        templateColumnInfoDTO = new TemplateColumnInfoDTO();
        templateColumnInfoDTO.setCode("QXX_AUTHOR");
        templateColumnInfoDTO.setTitle("作者：");
        templateColumnInfoDTO.setOperateType("staticinfo");
        templateColumnInfoDTO.setValue("木逸萧");
        columnInfoList.add(templateColumnInfoDTO);
        
        templateColumnInfoDTO = new TemplateColumnInfoDTO();
        templateColumnInfoDTO.setCode("BALANCE");
        templateColumnInfoDTO.setTitle("余额");
        templateColumnInfoDTO.setOperateType("staticinfo");
        templateColumnInfoDTO.setValue("");
        columnInfoList.add(templateColumnInfoDTO);
        
        templateColumnInfoDTO = new TemplateColumnInfoDTO();
        templateColumnInfoDTO.setCode("POINT");
        templateColumnInfoDTO.setTitle("积分");
        templateColumnInfoDTO.setOperateType("staticinfo");
        templateColumnInfoDTO.setValue("");
        columnInfoList.add(templateColumnInfoDTO);
        
        templateColumnInfoDTO = new TemplateColumnInfoDTO();
        templateColumnInfoDTO.setCode("LEVEL");
        templateColumnInfoDTO.setTitle("等级");
        templateColumnInfoDTO.setOperateType("staticinfo");
        templateColumnInfoDTO.setValue("");
        columnInfoList.add(templateColumnInfoDTO);

		model.setColumnInfoList(columnInfoList);
		
		//字段规则列表--必须
		List<TemplateFieldRuleDTO> fieldRuleList = new ArrayList<>();
		TemplateFieldRuleDTO templateFieldRuleDTO = new TemplateFieldRuleDTO();
		templateFieldRuleDTO.setFieldName("ValidDate");
		templateFieldRuleDTO.setRuleName("ASSIGN_FROM_REQUEST");
		templateFieldRuleDTO.setRuleValue("ValidDate");
		fieldRuleList.add(templateFieldRuleDTO);
        
        templateFieldRuleDTO = new TemplateFieldRuleDTO();
        templateFieldRuleDTO.setFieldName("Balance");
        templateFieldRuleDTO.setRuleName("ASSIGN_FROM_REQUEST");
        templateFieldRuleDTO.setRuleValue("Balance");
        fieldRuleList.add(templateFieldRuleDTO);

        templateFieldRuleDTO = new TemplateFieldRuleDTO();
        templateFieldRuleDTO.setFieldName("Point");
        templateFieldRuleDTO.setRuleName("ASSIGN_FROM_REQUEST");
        templateFieldRuleDTO.setRuleValue("Point");
        fieldRuleList.add(templateFieldRuleDTO);
        
        templateFieldRuleDTO = new TemplateFieldRuleDTO();
        templateFieldRuleDTO.setFieldName("Level");
        templateFieldRuleDTO.setRuleName("ASSIGN_FROM_REQUEST");
        templateFieldRuleDTO.setRuleValue("Level");
        fieldRuleList.add(templateFieldRuleDTO);
        
		model.setFieldRuleList(fieldRuleList);
		
		List<TemplateCardLevelConfDTO> cardLevelConf = new ArrayList<>();
		TemplateCardLevelConfDTO templateCardLevelConfDTO = new TemplateCardLevelConfDTO();
        templateCardLevelConfDTO.setLevel("VIP");
        templateCardLevelConfDTO.setLevelShowName("高级会员");
        templateCardLevelConfDTO.setLevelIcon(logo_id);
        templateCardLevelConfDTO.setLevelDesc("高级会员高级会员高级会员");
        cardLevelConf.add(templateCardLevelConfDTO);
        templateCardLevelConfDTO = new TemplateCardLevelConfDTO();
        templateCardLevelConfDTO.setLevel("NORMAL");
        templateCardLevelConfDTO.setLevelShowName("普通会员");
        templateCardLevelConfDTO.setLevelIcon(logo_id);
        templateCardLevelConfDTO.setLevelDesc("普通会员普通会员普通会员");
        cardLevelConf.add(templateCardLevelConfDTO);
		model.setCardLevelConf(cardLevelConf);
		
//        if (shop) {
//            List<String> shopIds = new ArrayList<>();
//            for (int i = 0; i < shops.split(",").length; i++) {
//                shopIds.add(shops.split(",")[i].trim());
//            }
//            model.setShopIds(shopIds);
//            
//            List<PubChannelDTO> pubChannels = new ArrayList<>();
//            PubChannelDTO pubChannelDTO = new PubChannelDTO();
//            pubChannelDTO.setPubChannel("SHOP_DETAIL");
//            pubChannels.add(pubChannelDTO);
//            model.setPubChannels(pubChannels);
//            
//            TemplateOpenCardConfDTO templateOpenCardConfDTO = new TemplateOpenCardConfDTO();
//            templateOpenCardConfDTO.setOpenCardSourceType("MER");
//            templateOpenCardConfDTO.setSourceAppId(my_app_id);
//            templateOpenCardConfDTO.setOpenCardUrl(领卡链接);
//            model.setOpenCardConf(templateOpenCardConfDTO);
//        }
		
		return model;
	}
	
	/**
	 * 会员表单 TODO
	 */
	public static String alipayMarketingCardFormtemplateSetModel(String templateId) {

		List<String> list = new ArrayList<>();
		list.add("OPEN_FORM_FIELD_MOBILE");// 手机号
		list.add("OPEN_FORM_FIELD_GENDER");// 性别
		list.add("OPEN_FORM_FIELD_NAME");// 姓名
		list.add("OPEN_FORM_FIELD_BIRTHDAY_WITH_YEAR");// 生日
//		list.add("OPEN_FORM_FIELD_EMAIL");// 邮箱
//		list.add("OPEN_FORM_FIELD_BIRTHDAY");// 生日
//		list.add("OPEN_FORM_FIELD_IDCARD");// 身份证
//		list.add("OPEN_FORM_FIELD_ADDRESS");// 地址
//		list.add("OPEN_FORM_FIELD_CITY");// 城市
//		list.add("OPEN_FORM_FIELD_IS_STUDENT");// 是否学生认证
//		list.add("OPEN_FORM_FIELD_MEMBER_GRADE");// 会员等级

		Map<String, Object> required = new HashMap<>();
		required.put("common_fields", list);
		
		Map<String, Object> fields = new HashMap<>();
		fields.put("required", required);
		
		Map<String, Object> maps = new HashMap<>();
		maps.put("template_id", templateId);
		maps.put("fields", fields);
		
		return JsonMapper.nonDefaultMapper().toJson(maps);
	}
	
	/**
	 * 领卡链接 TODO
	 */
	public static AlipayMarketingCardActivateurlApplyModel alipayMarketingCardActivateurlApplyModel(String templateId, String url) {
		AlipayMarketingCardActivateurlApplyModel model = new AlipayMarketingCardActivateurlApplyModel();
		model.setTemplateId(templateId);
		model.setOutString("alipay");
		model.setCallback(url);
		return model;
	}
}






























