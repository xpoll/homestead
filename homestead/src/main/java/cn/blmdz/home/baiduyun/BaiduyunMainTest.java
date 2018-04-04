package cn.blmdz.home.baiduyun;

import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Sets;

public class BaiduyunMainTest {

    public static void main(String[] args) {
//        String key = "https://pan.baidu.com/s/1UJ1Gjay9m3lG9xoqrLGNzA";
//        String pwd = "gexh";
//        BaiduYunEncryption ccc = new BaiduYunEncryption();
//        List<BaiduyunFileInfo> list = ccc.getFileInfo(null, key, pwd);
//        System.out.println(JSON.toJSONString(list));
        
        String key = "https://pan.baidu.com/s/1bEsfZF4hQuobxLG8USUq7w";
        BaiduYunServiceImpl ccc = new BaiduYunServiceImpl();
        Set<Long> set = Sets.newHashSet();
        List<BaiduyunFileInfo> list = ccc.getFileInfo(null, key, null, false, set, true);
        System.out.println(JSON.toJSONString(list));
    }
}
