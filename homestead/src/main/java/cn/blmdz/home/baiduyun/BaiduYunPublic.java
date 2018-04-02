package cn.blmdz.home.baiduyun;

import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;

import cn.blmdz.home.enums.EnumsError;
import cn.blmdz.home.exception.WebJspException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaiduYunPublic implements BaiduyunService {

    /**
     * 文件/文件夹 列表
     *
     * @throws Exception
     */
    private List<Long> fidLists(Long uk, Long shardId, String appId, String BAIDUID, String key, JSONObject obj,
            List<Long> fid_lists, CloseableHttpClient httpclient) throws Exception {
        String url = BaiduyunConstant.list + "?uk=" + uk + "&shareid=" + shardId
                + "&order=other&desc=1&showempty=0&web=1&page=1&num=1000&dir=" + obj.getString("path") + "&t="
                + Math.random() + "&channel=chunlei&web=1&app_id=" + appId + "&bdstoken=null&logid="
                + BaiduyunConstant.logid(BAIDUID) + "&clienttype=0";
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Referer", BaiduyunConstant.commonurl + "?surl=" + key);
        CloseableHttpResponse response = httpclient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        String resp = null;

        if (entity == null)
            return fid_lists;
        resp = EntityUtils.toString(entity);

        log.debug("baiduyun encryption list response: {}", resp);

        JSONObject jsonObj = JSONObject.parseObject(resp);
        if (!Objects.equal(jsonObj.getInteger("errno"), Integer.valueOf(0)))
            return fid_lists;
        JSONArray array = jsonObj.getJSONArray("list");
        for (int i = 0; i < array.size(); i++) {
            fid_lists.add(array.getJSONObject(i).getLong("fs_id"));
            if (Objects.equal(array.getJSONObject(i).getInteger("isdir"), Integer.valueOf(1))) { // 1文件夹0文件
                fid_lists = fidLists(uk, shardId, appId, BAIDUID, key, array.getJSONObject(i), fid_lists, httpclient);
            }
        }

        return fid_lists;
    }

    @Override
    public List<BaiduyunFileInfo> getFileInfo(String id, String key, String pwd) {
        CookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        ;
        try {

            // 单个文件公开分享
            key = key.replace(BaiduyunConstant.commonurl, "");
            String url = BaiduyunConstant.commonurl + key;

            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String resp = null;
            if (entity == null)
                throw new WebJspException("commonurl返回为空。");
            resp = EntityUtils.toString(entity);

            log.debug("baiduyun encryption commonurl response: {}", resp);

            JSONObject jsonObj = null;
            List<Long> fid_lists = Lists.newArrayList();

            try {
                String data = resp.split("yunData.setData\\(")[1].split("\\);")[0];
                log.debug("baiduyun encryption yunData: {}", data);
                jsonObj = JSONObject.parseObject(data);
            } catch (Exception e) {
                throw new WebJspException("解析页面有误。");
            }

            url = BaiduyunConstant.sharedownload + "?sign=" + jsonObj.getString("sign") + "&timestamp="
                    + jsonObj.getString("timestamp");

            HttpPost httpPost = new HttpPost(url);
            List<NameValuePair> nvps = Lists.newArrayList();
            nvps.add(new BasicNameValuePair("encrypt", "0"));
            nvps.add(new BasicNameValuePair("product", "share"));
            nvps.add(new BasicNameValuePair("uk", jsonObj.getString("uk")));
            nvps.add(new BasicNameValuePair("primaryid", jsonObj.getString("shareid")));
            try {
                JSONArray array = jsonObj.getJSONObject("file_list").getJSONArray("list");
                for (int i = 0; i < array.size(); i++) {
                    fid_lists.add(array.getJSONObject(i).getLong("fs_id"));
                }
            } catch (Exception e) {
                throw new WebJspException("获取分享失败或用户取消分享了。");
            }
            nvps.add(new BasicNameValuePair("fid_list", JSON.toJSONString(fid_lists)));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            response = httpclient.execute(httpPost);
            entity = response.getEntity();
            if (entity == null)
                throw new WebJspException("sharedownload返回为空。");
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
                throw new WebJspException("悲剧，验证码，系统暂时没有研究验证码识别功能。");
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
