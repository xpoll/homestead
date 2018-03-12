package cn.blmdz.home.model.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * 前端传过来样例{type: 1, toId: 1, msg: xxx}<br>
 * 返回样例
 * <pre>
 * 1. {id: 1, name: xxx, type: 1, msg: xxx}
 * 2. {type: 4, other: {Object(Message)}}
 * </pre>
 * 
 * @author yongzongyang
 * @date 2017年7月19日
 */
@Data
public class WebChat implements Serializable{
	
	private static final long serialVersionUID = 1L;

	/**
	 * 发送人id
	 */
	private Long id;
	
	/**
	 * 暂时加上昵称
	 */
	private String name;
	
	/**
	 * 内容
	 */
	private String msg;
	
	/**
	 * ChatType
	 */
	private Integer type;
	
	/**
	 * toId
	 */
	private Long toId;
	
	private Object other;
}