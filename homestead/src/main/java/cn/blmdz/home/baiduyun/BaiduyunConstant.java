package cn.blmdz.home.baiduyun;

import java.net.URLEncoder;
import java.util.List;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Objects;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaiduyunConstant {
    public static final long kk = 1024;
    public static final String commonurl = "https://pan.baidu.com/s/1";
    public static final String verify = "https://pan.baidu.com/share/verify";
    public static final String init = "https://pan.baidu.com/share/init";
    public static final String sharedownload = "https://pan.baidu.com/api/sharedownload";
    public static final String list = "https://pan.baidu.com/share/list";
    public static final String BDCLND = "BDCLND";
    public static final String BAIDUID = "BAIDUID";
    public static final String appId = "250528";

    public static String logid(String key) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        String js = "var u = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/~！@#￥%……&', l = String.fromCharCode, d = function(e) {if (e.length < 2) {var n = e.charCodeAt(0);return 128 > n ? e : 2048 > n ? l(192 | n >>> 6) + l(128 | 63 & n) : l(224 | n >>> 12 & 15) + l(128 | n >>> 6 & 63) + l(128 | 63 & n)}var n = 65536 + 1024 * (e.charCodeAt(0) - 55296) + (e.charCodeAt(1) - 56320);return l(240 | n >>> 18 & 7) + l(128 | n >>> 12 & 63) + l(128 | n >>> 6 & 63) + l(128 | 63 & n)}, f = /[\\uD800-\\uDBFF][\\uDC00-\\uDFFFF]|[^\\x00-\\x7F]/g, g = function(e) {return (e + '' + Math.random()).replace(f, d)}, h = function(e) {var n = [0, 2, 1][e.length % 3], t = e.charCodeAt(0) << 16 | (e.length > 1 ? e.charCodeAt(1) : 0) << 8 | (e.length > 2 ? e.charCodeAt(2) : 0), o = [u.charAt(t >>> 18), u.charAt(t >>> 12 & 63), n >= 2 ? '=' : u.charAt(t >>> 6 & 63), n >= 1 ? '=' : u.charAt(63 & t)];return o.join('')}, m = function(e) {return e.replace(/[\\s\\S]{1,3}/g, h)}, p = function() {return m(g((new Date).getTime()))}, w = function(e, n) {return n ? p(String(e)).replace(/[+\\/]/g, function(e) {return '+' == e ? '-' : '_'}).replace(/=/g, '') : p(String(e))};";
        try {
            engine.eval(js);
            key = (String) ((Invocable) engine).invokeFunction("w", key);
            return key;
        } catch (Exception e) {
            log.error("js 解析 logid 失败。");
            return null;
        }
    }

    /**
     * 文件/文件夹 列表
     * 
     * @throws Exception
     */
    public static List<Long> fidLists(Long uk, Long shardId, String appId, String BAIDUID, String key, JSONObject obj,
            List<Long> fid_lists, CloseableHttpClient httpclient, Boolean filterFoloder) throws Exception {
        if (!filterFoloder) fid_lists.add(obj.getLong("fs_id"));
        String url = BaiduyunConstant.list + "?uk=" + uk + "&shareid=" + shardId
                + "&order=other&desc=1&showempty=0&web=1&page=1&num=1000&dir=" + URLEncoder.encode(obj.getString("path"), "UTF-8") + "&t="
                + Math.random() + "&channel=chunlei&web=1&app_id=" + appId + "&bdstoken=null&logid=" + BaiduyunConstant.logid(BAIDUID)
                + "&clienttype=0";
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Referer", BaiduyunConstant.commonurl + "?surl=" + key);
        CloseableHttpResponse response = httpclient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        String resp = null;

        if (entity == null) return fid_lists;
        resp = EntityUtils.toString(entity);
        log.debug("baiduyun encryption list response: {}", resp);

        JSONObject jsonObj = JSONObject.parseObject(resp);
        if (!Objects.equal(jsonObj.getInteger("errno"), Integer.valueOf(0)))
            return fid_lists;
        JSONArray array = jsonObj.getJSONArray("list");
        for (int i = 0; i < array.size(); i++) {
            if (Objects.equal(array.getJSONObject(i).getInteger("isdir"), Integer.valueOf(1))) { // 1文件夹0文件
                fid_lists = fidLists(uk, shardId, appId, BAIDUID, key, array.getJSONObject(i), fid_lists, httpclient, filterFoloder);
            } else {
                fid_lists.add(array.getJSONObject(i).getLong("fs_id"));
            }
        }

        return fid_lists;
    }
}
