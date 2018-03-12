package cn.blmdz.home.enums;

/**
 * 通用状态
 * @author yangyz
 */
public enum EnumsStatus {
    
    /**
     * 不可用
     */
    NOT_ACTIVATE(false),
    
    /**
     * 可用
     */
    NORMAL(true);
	
	private final boolean value;
	
    public final boolean value() {
        return value;
    }
	
	EnumsStatus(boolean value) {
		this.value = value;
	}
}
