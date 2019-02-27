package cn.blmdz.test.pdf;

import java.math.BigDecimal;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Objects;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author yongzongyang
 * @date 2018年3月29日
 */
public class BaiduYunEncryption {
    public static final long kk = 1024;

    public static CloseableHttpClient httpclient;
    public static CookieStore cookieStore;
    public static String commonurl = "https://pan.baidu.com/s/1";
    public static String verify = "https://pan.baidu.com/share/verify";
    public static String init = "https://pan.baidu.com/share/init";
    public static String sharedownload = "https://pan.baidu.com/api/sharedownload";
    public static String list = "https://pan.baidu.com/share/list";
//    public static String key = "dEWWnYD";
//    public static String pwd = "cfhk";
  public static String key = "https://pan.baidu.com/s/1YYrBBLmVtg_a5KtgJUNcBg";
  public static String pwd = "eu27";

    static {
        cookieStore = new BasicCookieStore();
        httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
    }

    /**
     * 文件/文件夹 列表
     * @throws Exception 
     */
    public static List<Long> fidLists(Long uk, Long shardId, String appId, String BAIDUID, JSONObject obj, List<Long> fid_lists) throws Exception {
        String url = list + "?uk="
                + uk + "&shareid="
                + shardId + "&order=other&desc=1&showempty=0&web=1&page=1&num=1000&dir="
                + obj.getString("path") + "&t="
                + Math.random() + "&channel=chunlei&web=1&app_id="
                + appId + "&bdstoken=null&logid="
                + logid(BAIDUID) + "&clienttype=0";
        System.out.println(obj.getString("path"));
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Referer", commonurl + "?surl=" + key);
        CloseableHttpResponse response = httpclient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        String resp = null;

        if (entity == null) return fid_lists;
        resp = EntityUtils.toString(entity);
        
        System.out.println(resp);
        
        JSONObject jsonObj = JSONObject.parseObject(resp);
        if (!Objects.equal(jsonObj.getInteger("errno"), Integer.valueOf(0))) return fid_lists;
        JSONArray array = jsonObj.getJSONArray("list");
        for (int i = 0; i < array.size(); i++) {
            fid_lists.add(array.getJSONObject(i).getLong("fs_id"));
            if (Objects.equal(array.getJSONObject(i).getInteger("isdir"), Integer.valueOf(1))) { // 1文件夹 0 文件
                fid_lists = fidLists(uk, shardId, appId, BAIDUID, array.getJSONObject(i), fid_lists);
            }
        }
        
        return fid_lists;
    }
    public static void main(String[] args) throws Exception {
//        if (true) return;

        key = key.replace(commonurl, "");

        String url = verify + "?surl=" + key + "&t=" + System.currentTimeMillis()
                + "&channel=chunlei&web=1&clienttype=0";
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Referer", init + "?surl=" + key);
        List<NameValuePair> nvps = Lists.newArrayList();
        nvps.add(new BasicNameValuePair("pwd", pwd));
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
        CloseableHttpResponse response = httpclient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        String resp = null;

        if (entity == null)
            return;
        resp = EntityUtils.toString(entity);
//        System.out.println(resp);

        JSONObject jsonObj = JSONObject.parseObject(resp);
        if (Objects.equal(jsonObj.getInteger("errno"), Integer.valueOf(0))) {
            System.out.println("密码验证成功。");
        } else {
            System.out.println("密码验证失败。");
            return;
        }
        url = commonurl + key;

        HttpGet httpGet = new HttpGet(url);
        response = httpclient.execute(httpGet);
        entity = response.getEntity();

        if (entity == null)
            return;
        resp = EntityUtils.toString(entity);

        List<Long> fid_lists = Lists.newArrayList();
        String BDCLND = "BDCLND";
        String BAIDUID = "BAIDUID";
        String appId = "250528";

        try {
            System.err.println(resp.split("yunData.setData\\(")[1].split("\\);")[0]);
            jsonObj = JSONObject.parseObject(resp.split("yunData.setData\\(")[1].split("\\);")[0]);
        } catch (Exception e) {
            System.out.println("解析页面有误。");
            return;
        }
        Long shardId = jsonObj.getLong("shareid");
        Long uk = jsonObj.getLong("uk");
        try {

            for (Cookie cookie : cookieStore.getCookies()) {
                if (Objects.equal(BDCLND, cookie.getName())) {
                    BDCLND = URLDecoder.decode(cookie.getValue(), "UTF-8");
                }
                if (Objects.equal(BAIDUID, cookie.getName())) {
                    BAIDUID = cookie.getValue();
                }
            }
            
            JSONArray array = jsonObj.getJSONObject("file_list").getJSONArray("list");
            appId = array.getJSONObject(0).getString("app_id");
            for (int i = 0; i < array.size(); i++) {
                fid_lists.add(array.getJSONObject(i).getLong("fs_id"));
                if (Objects.equal(array.getJSONObject(i).getInteger("isdir"), Integer.valueOf(1))) { // 1文件夹 0 文件
                    fidLists(uk, shardId, appId, BAIDUID, array.getJSONObject(i), fid_lists);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("获取分享失败或用户取消分享了。");
            return;
        }

        url = sharedownload + "?sign=" + jsonObj.getString("sign") + "&timestamp=" + jsonObj.getString("timestamp")
                + "&channel=chunlei&web=1&app_id=" + appId + "&bdstoken=" + jsonObj.getString("bdstoken") + "&logid="
                + logid(BAIDUID) + "&clienttype=0";

        httpPost = new HttpPost(url);
        httpPost.setHeader("Referer", commonurl + key);
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

        if (entity == null)
            return;
        resp = EntityUtils.toString(entity);
        System.out.println(resp);

        jsonObj = JSONObject.parseObject(resp);
        if (Objects.equal(jsonObj.getInteger("errno"), Integer.valueOf(0))) {
            JSONArray array = jsonObj.getJSONArray("list");
            List<FileInfo> fileInfos = Lists.newArrayList();
            for (int i = 0; i < array.size(); i ++) {
                FileInfo info = new FileInfo();
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
            
            System.out.println("------------------华丽的分割线------------------");
            System.out.println(JSON.toJSONString(fileInfos));

        } else if (Objects.equal(jsonObj.getInteger("errno"), Integer.valueOf(-20))) {
            System.out.println("悲剧，验证码");
        }

    }
    
    public static String logid(String key) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        String js = "var u = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/~！@#￥%……&', l = String.fromCharCode, d = function(e) {if (e.length < 2) {var n = e.charCodeAt(0);return 128 > n ? e : 2048 > n ? l(192 | n >>> 6) + l(128 | 63 & n) : l(224 | n >>> 12 & 15) + l(128 | n >>> 6 & 63) + l(128 | 63 & n)}var n = 65536 + 1024 * (e.charCodeAt(0) - 55296) + (e.charCodeAt(1) - 56320);return l(240 | n >>> 18 & 7) + l(128 | n >>> 12 & 63) + l(128 | n >>> 6 & 63) + l(128 | 63 & n)}, f = /[\\uD800-\\uDBFF][\\uDC00-\\uDFFFF]|[^\\x00-\\x7F]/g, g = function(e) {return (e + '' + Math.random()).replace(f, d)}, h = function(e) {var n = [0, 2, 1][e.length % 3], t = e.charCodeAt(0) << 16 | (e.length > 1 ? e.charCodeAt(1) : 0) << 8 | (e.length > 2 ? e.charCodeAt(2) : 0), o = [u.charAt(t >>> 18), u.charAt(t >>> 12 & 63), n >= 2 ? '=' : u.charAt(t >>> 6 & 63), n >= 1 ? '=' : u.charAt(63 & t)];return o.join('')}, m = function(e) {return e.replace(/[\\s\\S]{1,3}/g, h)}, p = function() {return m(g((new Date).getTime()))}, w = function(e, n) {return n ? p(String(e)).replace(/[+\\/]/g, function(e) {return '+' == e ? '-' : '_'}).replace(/=/g, '') : p(String(e))};";
        try {
            engine.eval(js);
            key = (String) ((Invocable) engine).invokeFunction("w", key);
            return key;
        } catch (Exception e) {
            System.err.println("js 解析 logid 失败。");
            return null;
        }
    }

}

@Data
class FileInfo {
    private Long fsId;
    private Boolean dir = true;// 1文件夹 0 文件
    private String name;
    private String path;
    @Getter(AccessLevel.NONE)
    private Long size;
    @Setter(AccessLevel.NONE)
    private String sizeShow;
    private String link;
    
    public void setSize(Long size) {
        if (size == null) size = 0L;
        this.size = size;
        if (size < BaiduYunEncryption.kk) sizeShow = size + "B";
        else if (size <= (BaiduYunEncryption.kk * BaiduYunEncryption.kk)) sizeShow = new BigDecimal(size).divide(new BigDecimal(BaiduYunEncryption.kk)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "KB";
        else if (size <= (BaiduYunEncryption.kk * BaiduYunEncryption.kk * BaiduYunEncryption.kk)) sizeShow = new BigDecimal(size).divide(new BigDecimal(BaiduYunEncryption.kk * BaiduYunEncryption.kk)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "MB";
        else sizeShow = new BigDecimal(size).divide(new BigDecimal(BaiduYunEncryption.kk * BaiduYunEncryption.kk * BaiduYunEncryption.kk)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "GB";
    }
}
