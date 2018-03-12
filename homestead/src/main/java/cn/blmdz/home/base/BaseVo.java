package cn.blmdz.home.base;

import java.io.Serializable;

import cn.blmdz.home.enums.EnumsError;
import cn.blmdz.home.exception.WebJspException;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BaseVo<K, V> implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private K key;
	private V value;
	
	public void setKey(K key) {
		if (key == null)
			throw new WebJspException(EnumsError.ERROR_999999);
		this.key = key;
	}
	public void setValue(V value) {
		if (value == null)
			throw new WebJspException(EnumsError.ERROR_999999);
		this.value = value;
	}
	
	public static <K, V> BaseVo<K, V> put(K key, V value) {
		return new BaseVo<K, V>(key, value);
	}
	
	protected BaseVo(K key, V value) {
		super();
		this.key = key;
		this.value = value;
	}
}
