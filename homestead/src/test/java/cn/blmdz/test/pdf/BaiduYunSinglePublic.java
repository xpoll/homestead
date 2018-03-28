package cn.blmdz.test.pdf;

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
import org.assertj.core.util.Lists;

import com.alibaba.fastjson.JSONObject;

public class BaiduYunSinglePublic {

    public static CloseableHttpClient httpclient;
    public static CookieStore cookieStore;

	static {
	    cookieStore = new BasicCookieStore();
		httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
		//HttpClients.createDefault();
	}

    public static void main(String[] args) throws Exception {
        
        // 单个文件公开分享
        String url = "https://pan.baidu.com/s/1gaTnzccAgInFqJlHhauVQg"; // 分享出来的url值
        
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = httpclient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        String resp = null;
        if (entity == null) return ;
        resp = EntityUtils.toString(entity);
        JSONObject jsonObj = null;

        try {
            jsonObj = JSONObject.parseObject(resp.split("yunData.setData\\(")[1].split("\\);")[0]);
        } catch (Exception e) {
            System.out.println("解析页面有误。");
            return;
        }

        url = "https://pan.baidu.com/api/sharedownload?sign=" + jsonObj.getString("sign") + "&timestamp=" + jsonObj.getString("timestamp");
        
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> nvps = Lists.newArrayList();
        nvps.add(new BasicNameValuePair("encrypt", "0"));
        nvps.add(new BasicNameValuePair("product", "share"));
        nvps.add(new BasicNameValuePair("uk", jsonObj.getString("uk")));
        nvps.add(new BasicNameValuePair("primaryid", jsonObj.getString("shareid")));
        String fid_list = null;
        try {
            fid_list = jsonObj.getJSONObject("file_list").getJSONArray("list").getJSONObject(0).getString("fs_id");
        } catch (Exception e) {
            System.out.println("获取分享失败或用户取消分享了。");
            return;
        }
        nvps.add(new BasicNameValuePair("fid_list", "[" + fid_list +"]"));
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
        response = httpclient.execute(httpPost);
        entity = response.getEntity();
        if (entity == null) return ;
        resp = EntityUtils.toString(entity);
        jsonObj = JSONObject.parseObject(resp);
        url = jsonObj.getJSONArray("list").getJSONObject(0).getString("dlink");
        System.out.println(url);
        
    }

}
