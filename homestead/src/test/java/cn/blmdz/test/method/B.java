package cn.blmdz.test.method;

import java.io.Serializable;

import lombok.Data;

@Data
public class B implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String name;
	private int age;
}
