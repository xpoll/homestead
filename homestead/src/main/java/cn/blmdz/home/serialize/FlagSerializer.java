package cn.blmdz.home.serialize;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
public class FlagSerializer extends JsonSerializer<Object> {

	@Override
	public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
			throws IOException, JsonProcessingException {
		if (value instanceof Boolean) {
			Boolean flag = (Boolean) value;
	    	if (flag)
	    		gen.writeString("是");
	    	else
	    		gen.writeString("否");
		}
	}

}
