package cn.blmdz.home.baiduyun;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

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
}
