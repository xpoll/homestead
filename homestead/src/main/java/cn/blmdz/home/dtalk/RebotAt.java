package cn.blmdz.home.dtalk;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RebotAt {
    private List<String> atMobiles; // 手机号
    private Boolean isAtAll = Boolean.FALSE.booleanValue();
}
