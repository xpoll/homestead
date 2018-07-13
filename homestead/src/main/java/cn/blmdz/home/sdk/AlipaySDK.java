package cn.blmdz.home.sdk;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.FileItem;
import com.alipay.api.domain.AlipayMarketingCardActivateformQueryModel;
import com.alipay.api.domain.AlipayMarketingCardActivateurlApplyModel;
import com.alipay.api.domain.AlipayMarketingCardConsumeSyncModel;
import com.alipay.api.domain.AlipayMarketingCardDeleteModel;
import com.alipay.api.domain.AlipayMarketingCardOpenModel;
import com.alipay.api.domain.AlipayMarketingCardQueryModel;
import com.alipay.api.domain.AlipayMarketingCardTemplateCreateModel;
import com.alipay.api.domain.AlipayMarketingCardTemplateModifyModel;
import com.alipay.api.domain.AlipayMarketingCardTemplateQueryModel;
import com.alipay.api.domain.AlipayMarketingCardUpdateModel;
import com.alipay.api.domain.KoubeiMarketingCampaignActivityQueryModel;
import com.alipay.api.request.AlipayMarketingCardActivateformQueryRequest;
import com.alipay.api.request.AlipayMarketingCardActivateurlApplyRequest;
import com.alipay.api.request.AlipayMarketingCardConsumeSyncRequest;
import com.alipay.api.request.AlipayMarketingCardDeleteRequest;
import com.alipay.api.request.AlipayMarketingCardFormtemplateSetRequest;
import com.alipay.api.request.AlipayMarketingCardOpenRequest;
import com.alipay.api.request.AlipayMarketingCardQueryRequest;
import com.alipay.api.request.AlipayMarketingCardTemplateCreateRequest;
import com.alipay.api.request.AlipayMarketingCardTemplateModifyRequest;
import com.alipay.api.request.AlipayMarketingCardTemplateQueryRequest;
import com.alipay.api.request.AlipayMarketingCardUpdateRequest;
import com.alipay.api.request.AlipayOfflineMaterialImageUploadRequest;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.request.KoubeiMarketingCampaignActivityQueryRequest;
import com.alipay.api.response.AlipayMarketingCardActivateformQueryResponse;
import com.alipay.api.response.AlipayMarketingCardActivateurlApplyResponse;
import com.alipay.api.response.AlipayMarketingCardConsumeSyncResponse;
import com.alipay.api.response.AlipayMarketingCardDeleteResponse;
import com.alipay.api.response.AlipayMarketingCardFormtemplateSetResponse;
import com.alipay.api.response.AlipayMarketingCardOpenResponse;
import com.alipay.api.response.AlipayMarketingCardQueryResponse;
import com.alipay.api.response.AlipayMarketingCardTemplateCreateResponse;
import com.alipay.api.response.AlipayMarketingCardTemplateModifyResponse;
import com.alipay.api.response.AlipayMarketingCardTemplateQueryResponse;
import com.alipay.api.response.AlipayMarketingCardUpdateResponse;
import com.alipay.api.response.AlipayOfflineMaterialImageUploadResponse;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.alipay.api.response.KoubeiMarketingCampaignActivityQueryResponse;

import cn.blmdz.home.exception.WebJspException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlipaySDK {

	private AlipayClient client;

	public AlipaySDK(AlipayClient alipayClient) {
		this.client = alipayClient;
	}

	/**
	 * 图片上传 alipay.offline.material.image.upload
	 */
	public AlipayOfflineMaterialImageUploadResponse upload(File file) {
		log.info("-----------------图片上传-------------------");
		try {
			AlipayOfflineMaterialImageUploadRequest request = new AlipayOfflineMaterialImageUploadRequest();
			request.setImageName(file.getName().substring(0, file.getName().lastIndexOf(".")));
			request.setImageType(file.getName().substring(file.getName().lastIndexOf(".") + 1));
			request.setImageContent(new FileItem(file));
			AlipayOfflineMaterialImageUploadResponse response = client.execute(request);
			log.info(response.getBody());
			if (response.isSuccess()) {
				log.info("success.");
			} else {
				log.error("error.");
			}
			return response;
		} catch (AlipayApiException e) {
			throw new WebJspException(e.getCause());
		}
	}
	
	/**
	 * 获取用户授权 alipay.system.oauth.token
	 */
	public AlipaySystemOauthTokenResponse userToken(String code) {
		log.info("-----------------获取用户授权-------------------");
		try {
			AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
			request.setGrantType("authorization_code");
			request.setCode(code);
			AlipaySystemOauthTokenResponse response = client.execute(request);
			log.info(response.getBody());
			if (response.isSuccess()) {
				log.info("success.");
			} else {
				log.error("error.");
			}
			return response;
		} catch (AlipayApiException e) {
			throw new WebJspException(e.getCause());
		}
	}

	/**
	 * 刷新用户授权 alipay.system.oauth.token
	 */
	public AlipaySystemOauthTokenResponse refreshUser(String refreshToken) {
		log.info("-----------------刷新用户授权-------------------");
		try {
			AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
			request.setGrantType("refresh_token");
			request.setRefreshToken(refreshToken);
			AlipaySystemOauthTokenResponse response = client.execute(request);
			log.info(response.getBody());
			if (response.isSuccess()) {
				log.info("success.");
			} else {
				log.error("error.");
			}
			return response;
		} catch (AlipayApiException e) {
			throw new WebJspException(e.getCause());
		}
	}
	

	/**
	 * 查询用户授权信息 alipay.user.info.share
	 */
	public AlipayUserInfoShareResponse userInfo(String authToken) {
		log.info("-----------------查询用户授权信息-------------------");
		try {
			AlipayUserInfoShareRequest request = new AlipayUserInfoShareRequest();
			AlipayUserInfoShareResponse response = client.execute(request, authToken);
			log.info(response.getBody());
			if (response.isSuccess()) {
				log.info("success.");
			} else {
				log.error("error.");
			}
			return response;
		} catch (AlipayApiException e) {
			throw new WebJspException(e.getCause());
		}
	}
	

    /**
     * 创建会员卡模板 alipay.marketing.card.template.create
     * 
     * @api <a href=
     *      "https://doc.open.alipay.com/docs/api.htm?docType=4&apiId=1192">
     *      会员卡模板创建</a>
     * @param model
     * @param authToken
     * @return
     * 
     */
    public AlipayMarketingCardTemplateCreateResponse createTemplate(AlipayMarketingCardTemplateCreateModel model) {
        log.info("-----------------创建会员卡模板-------------------");
        try {
            AlipayMarketingCardTemplateCreateRequest request = new AlipayMarketingCardTemplateCreateRequest();
            request.setBizModel(model);
            AlipayMarketingCardTemplateCreateResponse response = client.execute(request);
            log.info(response.getBody());
            if (response.isSuccess()) {
                log.info("success.");
            } else {
                log.error("error.");
            }
            return response;
        } catch (AlipayApiException e) {
            throw new WebJspException(e.getCause());
        }
    }

    /**
     * 修改会员卡模板 alipay.marketing.card.template.modify
     * 
     * @api <a href=
     *      "https://doc.open.alipay.com/docs/api.htm?docType=4&apiId=1190">
     *      会员卡模板修改</a>
     * @param model
     * @param authToken
     * @return
     * 
     */
    public AlipayMarketingCardTemplateModifyResponse modifyTemplate(AlipayMarketingCardTemplateModifyModel model) {
        log.info("-----------------修改会员卡模板-------------------");
        try {
            AlipayMarketingCardTemplateModifyRequest request = new AlipayMarketingCardTemplateModifyRequest();
            request.setNotifyUrl("http://blmdz.cn/third/test");
            request.setBizModel(model);
            AlipayMarketingCardTemplateModifyResponse response = client.execute(request);
            log.info(response.getBody());
            if (response.isSuccess()) {
                log.info("success.");
            } else {
                log.error("error.");
            }
            return response;
        } catch (AlipayApiException e) {
            throw new WebJspException(e.getCause());
        }
    }

    /**
     * 查询会员卡模板 alipay.marketing.card.template.query
     * 
     * @api <a href=
     *      "https://doc.open.alipay.com/docs/api.htm?docType=4&apiId=1191">
     *      会员卡模板查询接口</a>
     * @param templateId
     * @param authToken
     * @return
     * 
     */
    public AlipayMarketingCardTemplateQueryResponse templateInfo(String templateId)
            {

        log.info("-----------------查询会员卡模板-------------------");
        try {
            AlipayMarketingCardTemplateQueryRequest request = new AlipayMarketingCardTemplateQueryRequest();
            AlipayMarketingCardTemplateQueryModel model = new AlipayMarketingCardTemplateQueryModel();
            model.setTemplateId(templateId);
            request.setBizModel(model);
            AlipayMarketingCardTemplateQueryResponse response = client.execute(request);
            log.info(response.getBody());
            if (response.isSuccess()) {
                log.info("success.");
            } else {
                log.error("error.");
            }
            return response;
        } catch (AlipayApiException e) {
            throw new WebJspException(e.getCause());
        }
    }

    /**
     * 设置表单信息 alipay.marketing.card.formtemplate.set
     * 
     * @api 见PDF
     * @param model
     * @param authToken
     * @return
     * 
     */
    public AlipayMarketingCardFormtemplateSetResponse formTemplate(String bizContent) {
        log.info("-----------------设置表单信息-------------------");
        try {
            AlipayMarketingCardFormtemplateSetRequest request = new AlipayMarketingCardFormtemplateSetRequest();
            request.setBizContent(bizContent);
            AlipayMarketingCardFormtemplateSetResponse response = client.execute(request);
            log.info(response.getBody());
            if (response.isSuccess()) {
                log.info("success.");
            } else {
                log.error("error.");
            }
            return response;
        } catch (AlipayApiException e) {
            throw new WebJspException(e.getCause());
        }
    }

    /**
     * 获取会员卡领卡投放链接 alipay.marketing.card.activateurl.apply
     * 
     * @api 见PDF
     * @param model
     * @param authToken
     * @return
     * 
     */
    public AlipayMarketingCardActivateurlApplyResponse cardLink(AlipayMarketingCardActivateurlApplyModel model) {
        log.info("-----------------获取会员卡领卡投放链接-------------------");
        try {
            AlipayMarketingCardActivateurlApplyRequest request = new AlipayMarketingCardActivateurlApplyRequest();
            request.setBizModel(model);
            AlipayMarketingCardActivateurlApplyResponse response = client.execute(request);
            log.info(response.getBody());
            if (response.isSuccess()) {
                log.info("success.");
            } else {
                log.error("error.");
            }
            return response;
        } catch (AlipayApiException e) {
            throw new WebJspException(e.getCause());
        }
    }

    /**
     * 查询会员表单信息 alipay.marketing.card.activateform.query
     * 
     * @api 见PDF
     * @param requestId
     * @param templateId
     * @param authToken
     * 
     */
    public AlipayMarketingCardActivateformQueryResponse memberCardForm(String requestId, String templateId,
            String accessToken) {
        log.info("-----------------查询会员表单信息-------------------");
        try {
            AlipayMarketingCardActivateformQueryRequest request = new AlipayMarketingCardActivateformQueryRequest();
            AlipayMarketingCardActivateformQueryModel model = new AlipayMarketingCardActivateformQueryModel();
            model.setBizType("MEMBER_CARD");
            model.setRequestId(requestId);
            model.setTemplateId(templateId);
            request.setBizModel(model);
            AlipayMarketingCardActivateformQueryResponse response = client.execute(request, accessToken, null);
            log.info(response.getBody());
            if (response.isSuccess()) {
                log.info("success.");
            } else {
                log.error("error.");
            }
            return response;
        } catch (AlipayApiException e) {
            throw new WebJspException(e.getCause());
        }
    }

    /**
     * 会员卡开卡 alipay.marketing.card.open
     * 
     * @api <a href=
     *      "https://doc.open.alipay.com/docs/api.htm?docType=4&apiId=1165">
     *      会员卡开卡</a>
     * 
     * 
     */
    public AlipayMarketingCardOpenResponse memberCardOpen(AlipayMarketingCardOpenModel model, String accessToken)
            {
        log.info("-----------------会员卡开卡-------------------");
        try {
            AlipayMarketingCardOpenRequest request = new AlipayMarketingCardOpenRequest();
            request.setBizModel(model);
            AlipayMarketingCardOpenResponse response = client.execute(request, accessToken, null);
            log.info(response.getBody());
            if (response.isSuccess()) {
                log.info("success.");
            } else {
                log.error("error.");
            }
            return response;
        } catch (AlipayApiException e) {
            throw new WebJspException(e.getCause());
        }
    }

    /**
     * 会员卡更新 alipay.marketing.card.update
     * 
     * @api <a href=
     *      "https://doc.open.alipay.com/docs/api.htm?docType=4&apiId=1164">
     *      会员卡更新</a>
     * 
     * 
     */
    public AlipayMarketingCardUpdateResponse memberCardUpdate(AlipayMarketingCardUpdateModel model) {
        log.info("-----------------会员卡更新-------------------");
        try {
            AlipayMarketingCardUpdateRequest request = new AlipayMarketingCardUpdateRequest();
            request.setBizModel(model);
            AlipayMarketingCardUpdateResponse response = client.execute(request);
            log.info(response.getBody());
            if (response.isSuccess()) {
                log.info("success.");
            } else {
                log.error("error.");
            }
            return response;
        } catch (AlipayApiException e) {
            throw new WebJspException(e.getCause());
        }

    }

    /**
     * 会员卡删除 alipay.marketing.card.delete
     * 
     * @api <a href=
     *      "https://doc.open.alipay.com/docs/api.htm?docType=4&apiId=1166">
     *      会员卡删除</a>
     * 
     * 
     */
    public AlipayMarketingCardDeleteResponse memberCardDelete(String targetCardNo) {
        log.info("-----------------会员卡删除-------------------");
        try {
            AlipayMarketingCardDeleteRequest request = new AlipayMarketingCardDeleteRequest();
            AlipayMarketingCardDeleteModel model = new AlipayMarketingCardDeleteModel();
            model.setOutSerialNo(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));
            model.setTargetCardNo(targetCardNo);
            model.setTargetCardNoType("BIZ_CARD");
            model.setReasonCode("USER_UNBUND");
            request.setBizModel(model);
            AlipayMarketingCardDeleteResponse response = client.execute(request);
            log.info(response.getBody());
            if (response.isSuccess()) {
                log.info("success.");
            } else {
                log.error("error.");
            }
            return response;
        } catch (AlipayApiException e) {
            throw new WebJspException(e.getCause());
        }

    }

    /**
     * 会员卡查询 alipay.marketing.card.query
     * 
     * @api <a href=
     *      "https://doc.open.alipay.com/docs/api.htm?docType=4&apiId=1163">
     *      会员卡查询</a>
     * 
     * 
     */
    public AlipayMarketingCardQueryResponse memberCardInfo(String targetCardNo) {
        log.info("-----------------会员卡查询-------------------");
        try {
            AlipayMarketingCardQueryRequest request = new AlipayMarketingCardQueryRequest();
            AlipayMarketingCardQueryModel model = new AlipayMarketingCardQueryModel();
            model.setTargetCardNo(targetCardNo);
            model.setTargetCardNoType("BIZ_CARD");
            request.setBizModel(model);
            AlipayMarketingCardQueryResponse response = client.execute(request);
            log.info(response.getBody());
            if (response.isSuccess()) {
                log.info("success.");
            } else {
                log.error("error.");
            }
            return response;
        } catch (AlipayApiException e) {
            throw new WebJspException(e.getCause());
        }

    }

    /**
     * 会员卡消费记录同步 alipay.marketing.card.consume.sync
     * 
     * @api <a href=
     *      "https://doc.open.alipay.com/docs/api.htm?docType=4&apiId=1211">
     *      会员卡消费记录同步</a>
     * 
     * 
     */
    @Deprecated
    public AlipayMarketingCardConsumeSyncResponse memberCardConsume() {
        log.info("-----------------会员卡消费记录同步-------------------");
        try {
            AlipayMarketingCardConsumeSyncRequest request = new AlipayMarketingCardConsumeSyncRequest();
            AlipayMarketingCardConsumeSyncModel model = new AlipayMarketingCardConsumeSyncModel();
            request.setBizModel(model);
            AlipayMarketingCardConsumeSyncResponse response = client.execute(request);
            log.info(response.getBody());
            if (response.isSuccess()) {
                log.info("success.");
            } else {
                log.error("error.");
            }
            return response;
        } catch (AlipayApiException e) {
            throw new WebJspException(e.getCause());
        }
    }
    
    /**
     * 活动详情查询 koubei.marketing.campaign.activity.query
     * 
     * @api <a href=
     *      "https://docs.open.alipay.com/api_5/koubei.marketing.campaign.activity.query/">
     *      活动详情查询</a>
     *      
     *      https://e.alipay.com/main.htm#/marketing-activity/buygive/create?_k=08ni45
     *      
     */
    public KoubeiMarketingCampaignActivityQueryResponse activityQuery(KoubeiMarketingCampaignActivityQueryModel model) {
        
        log.info("-----------------活动详情查询-------------------");
        try {
            KoubeiMarketingCampaignActivityQueryRequest request = new KoubeiMarketingCampaignActivityQueryRequest();
            request.setBizModel(model);
            KoubeiMarketingCampaignActivityQueryResponse response = client.execute(request, null, null);
            log.info(response.getBody());
            if (response.isSuccess()) {
                log.info("success.");
            } else {
                log.error("error.");
            }
            return response;
        } catch (AlipayApiException e) {
            throw new WebJspException(e.getCause());
        }
    
    }
}
