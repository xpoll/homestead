package cn.blmdz.test.alipay;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayFundTransToaccountTransferModel;
import com.alipay.api.domain.AlipayMarketingCardOpenModel;
import com.alipay.api.domain.AlipayMarketingCardUpdateModel;
import com.alipay.api.domain.CardUserInfo;
import com.alipay.api.domain.McardNotifyMessage;
import com.alipay.api.domain.MerchantCard;
import com.alipay.api.domain.MerchantMenber;
import com.alipay.api.internal.util.AlipayLogger;

import cn.blmdz.home.HomesteadApplication;
import cn.blmdz.home.properties.OtherProperties;
import cn.blmdz.home.sdk.AlipaySDK;


/**
 * <pre>
 * 0. 卡模板图标
 * 1. 获取应用授权
 * 2. 查询应用授权信息
 * 3. 创建会员卡模板
 * 4. 查询会员卡模板 
 * 5. 设置表单信息
 * 6. 获取会员卡领卡投放链接
 * 7. 更新会员卡模板-update
 * 8. 设置表单信息-update
 * 9. 获取会员卡领卡投放链接-update
 * </pre>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = HomesteadApplication.class)
@WebAppConfiguration
@Transactional
@Rollback
@ActiveProfiles("local")
public class ConfigAlipay {
	public final static String image_url = "C:\\bg.png";

	// -------------------卡模板基本配置
	public final static String name = "熙小浅";
	public final static String color = "rgb(230,0,18)";
	public final static String prefix = "qxx";
    public final static String writeOffType = "barcode";
//    public final static String writeOffType = "none";
    public final static String shops = "2017111500077000000046425247,2017111500077000000046464829";

	public final static String sign = "qxx_alipay";
	public final static String template_id = "20171113000000000578231000300753";
	public final static String logo_id = "XR-tC_4jSmO-hE_UrNhp-wAAACMAAQED";
	public final static String bg_id = "FfviK_x7S6yESp2zyAIZawAAACMAAQED";

	@Autowired
	AlipaySDK sdk;
	@Autowired
    private OtherProperties properties;
    
	@Test
	public void main() throws AlipayApiException {
		AlipayLogger.setJDKDebugEnabled(true);
//		// 卡模板图标
//		sdk.upload(new File(image_url));
//		// 创建会员卡模板
//		AlipayMarketingCardTemplateCreateModel createModel = ModelFamily.alipayMarketingCardTemplateCreateModel(prefix, writeOffType, name, logo_id, color, bg_id, sign, url);
//		sdk.createTemplate(createModel);
//		// 查询会员卡模板  
//		sdk.templateInfo(template_id);
//		// 更新会员卡模板
//		AlipayMarketingCardTemplateModifyModel modifyModel = ModelAlipay.alipayMarketingCardTemplateModifyModel(template_id, prefix, writeOffType, name, logo_id, color, bg_id, sign, properties.getThird().getDomain(), shops, true, properties.getThird().getAlipay().getAppId());
//		sdk.modifyTemplate(modifyModel);
//		// 设置表单信息
//		sdk.formTemplate(ModelAlipay.alipayMarketingCardFormtemplateSetModel(template_id));
//		// 获取会员卡领卡投放链接 
//		AlipayMarketingCardActivateurlApplyResponse res = sdk.cardLink(ModelAlipay.alipayMarketingCardActivateurlApplyModel(template_id, url));
//		System.out.println(URLDecoder.decode(res.getApplyCardUrl()));
//		// 获取用户授权
//		sdk.userToken("54059fe4707e4f16bb84ea7bf71fNX87");
//		// 查询用户授权信息
//		sdk.userInfo("composeB3cdc9875dacb4891808ab4d228dacB87");
//		// 查询会员表单信息
//		sdk.memberCardForm("20170829018940520502652207754", template_id, "composeBe959c6769403498bb069f9ce286e5X75", auth_token);
		// 会员卡开卡
        Calendar max = Calendar.getInstance();
        AlipayMarketingCardOpenModel model = new AlipayMarketingCardOpenModel();
        model.setOutSerialNo("20171113121212222000006");
        model.setCardTemplateId(template_id);
        
        CardUserInfo cardUserInfo = new CardUserInfo();
        cardUserInfo.setUserUniIdType("UID");// 默认
        cardUserInfo.setUserUniId("2088702372638754");
        model.setCardUserInfo(cardUserInfo);
        MerchantCard cardExtInfo = new MerchantCard();
        cardExtInfo.setOpenDate(max.getTime());
        max.set(Calendar.YEAR, max.get(Calendar.YEAR) + 100);// 有效期默认100年
        
        cardExtInfo.setValidDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(max.getTime()));// 默认格式
        cardExtInfo.setExternalCardNo("8821210004000206994");
        cardExtInfo.setLevel("VIP");
        cardExtInfo.setPoint("10000");
        cardExtInfo.setBalance("888");
        
        model.setCardExtInfo(cardExtInfo);
        
        MerchantMenber memberExtInfo = new MerchantMenber();
        memberExtInfo.setName("杨");
        memberExtInfo.setGende("MALE");// 默认格式
        memberExtInfo.setCell("18224524752");
        model.setMemberExtInfo(memberExtInfo);
        
//		sdk.memberCardOpen(model, "composeBdffbfb36f840414492dd4809be132X75");
		
		AlipayMarketingCardUpdateModel um = new AlipayMarketingCardUpdateModel();
		um.setTargetCardNo("qxx0000003575");
		um.setTargetCardNoType("BIZ_CARD");
		um.setOccurTime(new Date());
		um.setCardInfo(cardExtInfo);
		
		
		List<McardNotifyMessage> notifyMessages = new ArrayList<>();
        McardNotifyMessage mcardNotifyMessage = new McardNotifyMessage();
        mcardNotifyMessage.setMessageType("POINT_UPDATE");
        mcardNotifyMessage.setChangeReason("由于你的消费");
        mcardNotifyMessage.setExtInfo("{}");
        notifyMessages.add(mcardNotifyMessage);
        mcardNotifyMessage = new McardNotifyMessage();
        mcardNotifyMessage.setMessageType("BALANCE_UPDATE");
        mcardNotifyMessage.setChangeReason("由于你的消费");
        mcardNotifyMessage.setExtInfo("{}");
        notifyMessages.add(mcardNotifyMessage);
        mcardNotifyMessage = new McardNotifyMessage();
        mcardNotifyMessage.setMessageType("LEVEL_UPDATE");
        mcardNotifyMessage.setExtInfo("{}");
        notifyMessages.add(mcardNotifyMessage);
		
		um.setNotifyMessages(notifyMessages);
//		sdk.memberCardUpdate(um);
//		// 会员卡查询
//		sdk.memberCardInfo("family0003366523");
//		// 会员卡删除
//		sdk.memberCardDelete("family0003366523");
		
        
        AlipayFundTransToaccountTransferModel alipayFundTransToaccountTransferModel = new AlipayFundTransToaccountTransferModel();
        alipayFundTransToaccountTransferModel.setOutBizNo(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));
        alipayFundTransToaccountTransferModel.setPayeeType("ALIPAY_USERID");
        alipayFundTransToaccountTransferModel.setPayeeAccount("2088702372638754");
        alipayFundTransToaccountTransferModel.setPayeeRealName("杨永宗");
        alipayFundTransToaccountTransferModel.setAmount("0.1");
        alipayFundTransToaccountTransferModel.setPayerShowName("杨大仙");
        alipayFundTransToaccountTransferModel.setPayerRealName("杨永宗");
        alipayFundTransToaccountTransferModel.setRemark("我在测试哦");
        
        sdk.fundTransToaccountTransfer(alipayFundTransToaccountTransferModel);
	}
}
