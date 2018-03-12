package cn.blmdz.home.base;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.github.pagehelper.Page;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

/**
 * 分页总类
 * @author lm
 * @date 2016年12月7日 下午11:44:19
 * @param <T>
 */
@Data
public class BasePage<S, T> {

	/** 每页多少个 */
	private int size = 10;
	
	/** 第几页 */
	private int num = 1;
	
	/** total */
	private long total;
	
	/** 返回数据 */
	private List<T> data;
	
	/** 设置数据 */
	@JsonIgnore
	@Setter(AccessLevel.NONE)
	private Page<T> page;
	
	/** 搜索 */
	@JsonProperty(access = Access.WRITE_ONLY)
	private S mode;
	
	/** 排序 */
	@JsonProperty(access = Access.WRITE_ONLY)
	private String order;
	
	public void setPage(Page<T> page) {
		this.total = page.getTotal();
		this.data = page;
	}
}
