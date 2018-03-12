package cn.blmdz.home.enums;

public enum EnumsError {

	ERROR_000000("success"),
	ERROR_999998("数据为空"),
	ERROR_999999("系统异常"),

	;
	private String desc;

	public String desc() {
		return desc;
	}

	EnumsError(String desc) {
		this.desc = desc;
	}
}
