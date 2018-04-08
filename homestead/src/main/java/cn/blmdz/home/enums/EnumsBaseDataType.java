package cn.blmdz.home.enums;

public enum EnumsBaseDataType {
    
    SIGN(1, "签到"),
    F_H_Z_K(2, "凤凰周刊-历史去重"),
    F_H_Z_K_KV(3, "凤凰周刊-拉取key-value"),
    F_H_Z_K_C(4, "凤凰周刊-栏目");
    
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
