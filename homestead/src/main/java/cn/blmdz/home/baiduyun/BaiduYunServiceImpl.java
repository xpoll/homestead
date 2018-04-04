package cn.blmdz.home.baiduyun;

import java.net.URLDecoder;
import java.util.List;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;

import cn.blmdz.home.enums.EnumsError;
import cn.blmdz.home.exception.WebJspException;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author yongzongyang
 * @date 2018年3月29日
 */
@Slf4j
@Service
public class BaiduYunServiceImpl implements BaiduyunService {
    
    @Override
    public List<BaiduyunFileInfo> getFileInfo(String id, String key, String pwd, Boolean encryption, Set<Long> fsids, Boolean filterFoloder) {

        CookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        String url = null;
        HttpPost httpPost = null;
        HttpGet httpGet = null;
        List<NameValuePair> nvps = null;
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        String resp = null;
        JSONObject jsonObj = null;
        Long shardId = null;
        Long uk = null;
        List<Long> fid_lists = Lists.newArrayList();
        String BDCLND = BaiduyunConstant.BDCLND;
        String BAIDUID = BaiduyunConstant.BAIDUID;
        String appId = BaiduyunConstant.appId;
        try {

            key = key.replace(BaiduyunConstant.commonurl, "");
            // 加密需要先登录
            if (encryption) {
	            url = BaiduyunConstant.verify + "?surl=" + key + "&t=" + System.currentTimeMillis() + "&channel=chunlei&web=1&clienttype=0";
	            httpPost = new HttpPost(url);
	            httpPost.setHeader("Referer", BaiduyunConstant.init + "?surl=" + key);
	            nvps = Lists.newArrayList();
	            nvps.add(new BasicNameValuePair("pwd", pwd));
	            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
	            response = httpclient.execute(httpPost);
	            entity = response.getEntity();
	
	            if (entity == null) throw new WebJspException("verify返回为空。");
	            resp = EntityUtils.toString(entity);
	            log.debug("baiduyun encryption verify response: {}", resp);
	
	            jsonObj = JSONObject.parseObject(resp);
	            if (Objects.equal(jsonObj.getInteger("errno"), Integer.valueOf(0))) {
	                log.debug("baiduyun encryption pwd check {}", true);
	            } else {
	                log.debug("baiduyun encryption pwd check {}", false);
	                throw new WebJspException("密码不正确。");
	            }
        	}
            url = BaiduyunConstant.commonurl + key;

            httpGet = new HttpGet(url);
            response = httpclient.execute(httpGet);
            entity = response.getEntity();

            if (entity == null) throw new WebJspException("commonurl返回为空。");
            resp = EntityUtils.toString(entity);
            log.debug("baiduyun encryption commonurl response: {}", resp);

            try {
                String data = resp.split("yunData.setData\\(")[1].split("\\);")[0];
                log.debug("baiduyun encryption yunData: {}", data);
                jsonObj = JSONObject.parseObject(data);
                shardId = jsonObj.getLong("shareid");
                uk = jsonObj.getLong("uk");
            } catch (Exception e) {
                throw new WebJspException("解析页面有误。");
            }
            for (Cookie cookie : cookieStore.getCookies()) {
            	if (Objects.equal(BDCLND, cookie.getName())) {
            		BDCLND = URLDecoder.decode(cookie.getValue(), "UTF-8");
            	}
            	if (Objects.equal(BAIDUID, cookie.getName())) {
            		BAIDUID = cookie.getValue();
            	}
            }
            try {
                JSONArray array = jsonObj.getJSONObject("file_list").getJSONArray("list");
                appId = array.getJSONObject(0).getString("app_id");
                for (int i = 0; i < array.size(); i++) {
                    if (Objects.equal(array.getJSONObject(i).getInteger("isdir"), Integer.valueOf(1))) { // 1文件夹0文件
                    	BaiduyunConstant.fidLists(uk, shardId, appId, BAIDUID, key, array.getJSONObject(i), fid_lists, httpclient, filterFoloder);
                    } else {
                        fid_lists.add(array.getJSONObject(i).getLong("fs_id"));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new WebJspException("获取分享失败或用户取消分享了。");
            }
            fid_lists.removeAll(fsids); // 移除过滤列表

            url = BaiduyunConstant.sharedownload + "?sign=" + jsonObj.getString("sign") + "&timestamp="
                    + jsonObj.getString("timestamp") + "&channel=chunlei&web=1&app_id=" + appId + "&bdstoken="
                    + jsonObj.getString("bdstoken") + "&logid=" + BaiduyunConstant.logid(BAIDUID) + "&clienttype=0";
            // 非加密只需要sign和timestamp两个参数就行，但多余参数不影响
            httpPost = new HttpPost(url);
            httpPost.setHeader("Referer", BaiduyunConstant.commonurl + key);
            nvps = Lists.newArrayList();
            nvps.add(new BasicNameValuePair("encrypt", "0"));
            nvps.add(new BasicNameValuePair("product", "share"));
            nvps.add(new BasicNameValuePair("uk", String.valueOf(uk)));
            nvps.add(new BasicNameValuePair("primaryid", String.valueOf(shardId)));
            nvps.add(new BasicNameValuePair("extra", "{\"sekey\":\"" + BDCLND + "\"}"));
            nvps.add(new BasicNameValuePair("fid_list", JSON.toJSONString(fid_lists)));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            response = httpclient.execute(httpPost);
            entity = response.getEntity();

            if (entity == null) throw new WebJspException("sharedownload返回为空。");
            resp = EntityUtils.toString(entity);
            log.debug("baiduyun encryption sharedownload response: {}", resp);

            jsonObj = JSONObject.parseObject(resp);
            if (Objects.equal(jsonObj.getInteger("errno"), Integer.valueOf(0))) {
                JSONArray array = jsonObj.getJSONArray("list");
                List<BaiduyunFileInfo> fileInfos = Lists.newArrayList();
                for (int i = 0; i < array.size(); i++) {
                    BaiduyunFileInfo info = new BaiduyunFileInfo();
                    info.setFsId(array.getJSONObject(i).getLong("fs_id"));
                    info.setName(array.getJSONObject(i).getString("server_filename"));
                    info.setPath(array.getJSONObject(i).getString("path"));
                    if (Objects.equal(array.getJSONObject(i).getInteger("isdir"), Integer.valueOf(0))) {
                        info.setDir(false);
                        info.setSize(array.getJSONObject(i).getLong("size"));
                        info.setLink(array.getJSONObject(i).getString("dlink"));
                    }
                    fileInfos.add(info);
                }

                return fileInfos;

            } else if (Objects.equal(jsonObj.getInteger("errno"), Integer.valueOf(-20))) {
                throw new WebJspException("悲剧，请求次数过多，出现验证码，系统暂时没有研究验证码识别功能。");
            } else {
                throw new WebJspException("悲剧，返回系统未处理的错误。代码为" + jsonObj.getInteger("errno"));
            }

        } catch (WebJspException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new WebJspException(EnumsError.ERROR_999999);
        }

    }

}
