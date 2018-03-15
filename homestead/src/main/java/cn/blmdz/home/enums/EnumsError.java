package cn.blmdz.home.enums;

public enum EnumsError {

	ERROR_000000("success"),
    ERROR_999996("请求method暂不支持"),
    ERROR_999997("缺少必要参数"),
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
