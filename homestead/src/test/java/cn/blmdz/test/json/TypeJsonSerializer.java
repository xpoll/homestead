package cn.blmdz.test.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class TypeJsonSerializer<T> extends JsonSerializer<Object> {

	@Override
	public void serialize(Object obj, JsonGenerator jg, SerializerProvider sp)
			throws IOException, JsonProcessingException {
		if ("1".equals(obj))
			jg.writeString("类型1");
	}

}
