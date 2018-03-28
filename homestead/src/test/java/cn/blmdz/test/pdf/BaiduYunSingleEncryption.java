package cn.blmdz.test.pdf;

import java.net.URLDecoder;
import java.util.List;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

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
import org.assertj.core.util.Lists;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Objects;

public class BaiduYunSingleEncryption {

    public static CloseableHttpClient httpclient;
    public static CookieStore cookieStore;

	static {
	    cookieStore = new BasicCookieStore();
		httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
		//HttpClients.createDefault();
	}

    public static void main(String[] args) throws Exception {
        
        // 单个文件私密分享
        String key = "ISunz5LbjbElW_PX7Ww8rw";
        String pwd = "nzkp";
        
        String url = "https://pan.baidu.com/share/verify?surl=" + key + "&t=" + System.currentTimeMillis() + "&channel=chunlei&web=1&clienttype=0";
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Referer", "https://pan.baidu.com/share/init?surl=" + key);
        List<NameValuePair> nvps = Lists.newArrayList();
        nvps.add(new BasicNameValuePair("pwd", pwd));
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
        CloseableHttpResponse response = httpclient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        String resp = null;
        if (entity == null) return ;
        resp = EntityUtils.toString(entity);
        JSONObject jsonObj = JSONObject.parseObject(resp);
        if (Objects.equal(jsonObj.getString("errno"), "0")) {
            System.out.println("密码验证成功。");
        } else {
            System.out.println("密码验证失败。");
            return;
        }
        url = "https://pan.baidu.com/s/1" + key;
        
        HttpGet httpGet = new HttpGet(url);
        response = httpclient.execute(httpGet);
        entity = response.getEntity();
        if (entity == null) return ;
        resp = EntityUtils.toString(entity);
        String fid_list = null;
        String appId = null;
        try {
            jsonObj = JSONObject.parseObject(resp.split("yunData.setData\\(")[1].split("\\);")[0]);
        } catch (Exception e) {
            System.out.println("解析页面有误。");
            return;
        }
        try {
            fid_list = jsonObj.getJSONObject("file_list").getJSONArray("list").getJSONObject(0).getString("fs_id");
            appId = jsonObj.getJSONObject("file_list").getJSONArray("list").getJSONObject(0).getString("app_id");
        } catch (Exception e) {
            System.out.println("获取分享失败或用户取消分享了。");
            return;
        }

        String BDCLND = "BDCLND";
        String BAIDUID = "BAIDUID";
        
        for (Cookie cookie : cookieStore.getCookies()) {
            if (Objects.equal(BDCLND, cookie.getName())) {
                BDCLND = URLDecoder.decode(cookie.getValue(), "UTF-8");
            }
            if (Objects.equal(BAIDUID, cookie.getName())) {
                BAIDUID = cookie.getValue();
            }
        }
        

        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        String js = "var u = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/~！@#￥%……&', l = String.fromCharCode, d = function(e) {if (e.length < 2) {var n = e.charCodeAt(0);return 128 > n ? e : 2048 > n ? l(192 | n >>> 6) + l(128 | 63 & n) : l(224 | n >>> 12 & 15) + l(128 | n >>> 6 & 63) + l(128 | 63 & n)}var n = 65536 + 1024 * (e.charCodeAt(0) - 55296) + (e.charCodeAt(1) - 56320);return l(240 | n >>> 18 & 7) + l(128 | n >>> 12 & 63) + l(128 | n >>> 6 & 63) + l(128 | 63 & n)}, f = /[\\uD800-\\uDBFF][\\uDC00-\\uDFFFF]|[^\\x00-\\x7F]/g, g = function(e) {return (e + '' + Math.random()).replace(f, d)}, h = function(e) {var n = [0, 2, 1][e.length % 3], t = e.charCodeAt(0) << 16 | (e.length > 1 ? e.charCodeAt(1) : 0) << 8 | (e.length > 2 ? e.charCodeAt(2) : 0), o = [u.charAt(t >>> 18), u.charAt(t >>> 12 & 63), n >= 2 ? '=' : u.charAt(t >>> 6 & 63), n >= 1 ? '=' : u.charAt(63 & t)];return o.join('')}, m = function(e) {return e.replace(/[\\s\\S]{1,3}/g, h)}, p = function() {return m(g((new Date).getTime()))}, w = function(e, n) {return n ? p(String(e)).replace(/[+\\/]/g, function(e) {return '+' == e ? '-' : '_'}).replace(/=/g, '') : p(String(e))};";
        engine.eval(js);
        BAIDUID = (String) ((Invocable) engine).invokeFunction("w", BAIDUID);
        
        url = "https://pan.baidu.com/api/sharedownload?sign=" + jsonObj.getString("sign") + "&timestamp=" + jsonObj.getString("timestamp") + "&channel=chunlei&web=1&app_id=" + appId + "&bdstoken=" + jsonObj.getString("bdstoken") + "&logid=" + BAIDUID + "&clienttype=0";
        
        httpPost = new HttpPost(url);
        httpPost.setHeader("Referer", "https://pan.baidu.com/s/1" + key);
        nvps = Lists.newArrayList();
        nvps.add(new BasicNameValuePair("encrypt", "0"));
        nvps.add(new BasicNameValuePair("product", "share"));
        nvps.add(new BasicNameValuePair("uk", jsonObj.getString("uk")));
        nvps.add(new BasicNameValuePair("primaryid", jsonObj.getString("shareid")));
        nvps.add(new BasicNameValuePair("extra", "{\"sekey\":\"" + BDCLND + "\"}"));
        nvps.add(new BasicNameValuePair("fid_list", "[" + fid_list +"]"));
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
        response = httpclient.execute(httpPost);
        entity = response.getEntity();
        if (entity == null) return ;
        resp = EntityUtils.toString(entity);
        jsonObj = JSONObject.parseObject(resp);
        if (Objects.equal(jsonObj.getString("errno"), "0")) {
            url = jsonObj.getJSONArray("list").getJSONObject(0).getString("dlink");
            System.out.println(url);
        } else if (Objects.equal(jsonObj.getString("errno"), "-20")) {
            System.out.println("悲剧，验证码");
        }
        
    }

}
