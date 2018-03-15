package cn.blmdz.home.model.third.wechat;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class WechatAppletSessionKeyResponse extends WechatBaseResponse {
    private String session_key;
    private Integer expires_in;
    private String openid;
}
