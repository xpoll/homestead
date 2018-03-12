package cn.blmdz.home.model.base;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@lombok.Data
public class SignData {
    // active 活跃天
    private Integer baseActive;
    private List<Data> vipActives;//VIP加成
    private Data vipDefaultActive;//VIP空值默认加成
    private List<Data> levelActives;//等级加成
    private Data levelDefaultActive;//等级空值默认加成
    private Data continueActive;//连续登陆加成
    
    // gold 金币
    private Integer baseGold;
    private List<Data> vipGolds;//VIP加成
    private Data vipDefaultGold;//VIP空值默认加成
    private List<Data> levelGolds;//等级加成
    private Data levelDefaultGold;//等级空值默认加成
    private Data continueGold;//连续登陆加成
    
    @lombok.Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Data {
        private Integer type; // 1 比例 2 累加
        private Integer key;
        private Integer value;
    }
}
