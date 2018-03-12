package cn.blmdz.home.enums;

/**
 * 性别
 */
public enum EnumsGender {
    
    UNKNOWN(0, "未知"),
    MALE(1, "男"),
    FEMALE(2, "女");
    
    private final int value;
    private final String desc;

    public final int value() {
        return value;
    }
    
    public final String desc() {
        return desc;
    }
    
    EnumsGender(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static EnumsGender tran(Integer code) {
        for (EnumsGender item : EnumsGender.values()) {
            if (item.value == code) return item;
        }
        return null;
    }
}
