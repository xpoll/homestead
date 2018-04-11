package cn.blmdz.home.enums;

public enum EnumsWeekly {

    W_DEFAULT(0, "默认分类"),
    W_001(1, "中国周刊"),
    W_002(2, "中国新闻周刊"),
    W_003(3, "英语岛"),
    W_004(4, "为了孩子"),
    W_005(5, "摄影之友"),
    W_006(6, "南风窗"),
    W_007(7, "南方人物"),
    W_008(8, "南都周刊"),
    W_009(9, "瞭望东方"),
    W_010(10, "Vista看天下"),
    W_011(11, "读者"),
    W_012(12, "电脑报"),
    W_013(13, "IT经理世界"),
    W_014(14, "财经天下"),
    ;
    
    private final int value;
    private final String desc;

    public final int value() {
        return value;
    }
    
    public final String desc() {
        return desc;
    }

    public static EnumsWeekly from(Integer value) {
        for (EnumsWeekly item : values()) {
            if (Integer.valueOf(item.value).equals(value)) {
                return item;
            }
        }
        return W_DEFAULT;
    }
    
    EnumsWeekly(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
