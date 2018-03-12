package cn.blmdz.home.enums;

public enum EnumsBaseDataType {
    
    SIGN(1, "签到");
    
    private final int value;
    private final String desc;

    public final int value() {
        return value;
    }
    
    public final String desc() {
        return desc;
    }
    
    EnumsBaseDataType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
