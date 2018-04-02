package cn.blmdz.home.baiduyun;

import java.util.List;

import com.alibaba.fastjson.JSON;

public class BaiduyunMainTest {

    public static void main(String[] args) {
//        String key = "https://pan.baidu.com/s/1UJ1Gjay9m3lG9xoqrLGNzA";
//        String pwd = "gexh";
//        BaiduYunEncryption ccc = new BaiduYunEncryption();
//        List<BaiduyunFileInfo> list = ccc.getFileInfo(null, key, pwd);
//        System.out.println(JSON.toJSONString(list));
        
        String key = "https://pan.baidu.com/s/14fo5FWeNfpa8CXZk8v9EUA";
        BaiduYunServiceImpl ccc = new BaiduYunServiceImpl();
        List<BaiduyunFileInfo> list = ccc.getFileInfo(null, key, null, false);
        System.out.println(JSON.toJSONString(list));
    }
}
