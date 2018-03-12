package cn.blmdz.test.json;

import lombok.Getter;

public enum StatusEnums {
	
	TYPE_1("1", "状态1"),
	TYPE_2("2", "状态2"),
	
	;
	@Getter
	private String code;
	@Getter
	private String desc;
	
	StatusEnums(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	
	public static StatusEnums trans(String code) {
		for (StatusEnums item : StatusEnums.values()) {
			if (item.getCode().equals(code))
				return item;
		}
		return null;
	}

}
