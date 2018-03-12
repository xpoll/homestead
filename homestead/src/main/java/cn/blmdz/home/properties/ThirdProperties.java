package cn.blmdz.home.properties;

import lombok.Data;

@Data
public class ThirdProperties {
    private String domain; // for redirectUrl
    private CommonProperties alipay;
    private CommonProperties baidu;
    private CommonProperties sina;
    private CommonProperties wechat;
}