package cn.blmdz.home.model.msg;

import java.io.Serializable;

import cn.blmdz.home.util.JsonMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseMsg implements Serializable{
    
    private static final long serialVersionUID = 1L;

    /**
     * SocketType
     */
    private Integer type;
    
    /**
     * 内容
     */
    private String msg;
    
    @Override
    public String toString() {
        return JsonMapper.nonEmptyMapper().toJson(this);
    }
}
