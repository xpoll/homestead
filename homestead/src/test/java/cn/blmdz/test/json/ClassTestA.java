package cn.blmdz.test.json;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Strings;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
public class ClassTestA implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Setter(AccessLevel.NONE)
	private String type;
	@Setter(AccessLevel.NONE)
	private String status;
	
	@JsonSerialize(using = TypeJsonSerializer.class)
	private String typeName;
	private String statusName;
	

	
	public void setType(String type) {
		this.type = type;
		
		if (Strings.isNullOrEmpty(typeName))
			this.typeName = type;
	}

	public void setStatus(String status) {
		this.status = status;
		this.statusName = StatusEnums.trans(status).getDesc();
	}
	
}
