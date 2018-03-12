package cn.blmdz.home.enums;

/**
 * 私密
 */
public enum EnumsPrivacy {
    
    PUBLIC(0, "公开"),
    FRIEND(1, "好友可见"),
    SECRET(2, "自己可见");
    
    private final int value;
    private final String desc;

    public final int value() {
        return value;
    }
    
    public final String desc() {
        return desc;
    }
    
    EnumsPrivacy(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static EnumsPrivacy tran(Integer code) {
        for (EnumsPrivacy item : EnumsPrivacy.values()) {
            if (item.value == code) return item;
        }
        return null;
    }
}
