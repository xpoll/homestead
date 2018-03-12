package cn.blmdz.home.properties;

import lombok.Data;

/**
 * 对应字段
 * <table>
 *  <tr><th>渠道</th><th>字段</th></tr>
 *  <tr><td>Alipay</td><td>appId privateKey publicKey linkCard</td></tr>
 *  <tr><td>Baidu</td><td>appKey appSecret redirectUrl</td></tr></tr>
 *  <tr><td>Sina</td><td>appKey appSecret redirectUrl</td></tr></tr>
 *  <tr><td>Sechat</td><td>appId appSecret redirectUrl</td></tr></tr>
 * </table>
 */
@Data
public class CommonProperties {
    private String appId;
    private String privateKey;
    private String publicKey;
    private String linkCard;

    private String appKey;
    private String appSecret;
    private String redirectUrl; // redirectUrl += domain
}
