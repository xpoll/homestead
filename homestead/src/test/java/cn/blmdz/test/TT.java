package cn.blmdz.test;

import java.util.HashMap;
import java.util.Map;

import com.github.kevinsawicki.http.HttpRequest;

public class TT {

    public static void main(String[] args) {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("event_code", "TREATE_PAY");
        requestMap.put("user_id", "2088002378529873");
        HttpRequest request = HttpRequest.get("http://m.maxxipoint.com/regist/third/update/alipay/notify.do", requestMap, false);
        System.out.println(request.code());
        System.out.println(request.body());
    }
}
