package cn.blmdz.home.model.search;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class TalkSearch implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 这里不是
     * EnumsPrivacy
     * 而是用作 0陌生人/1好友/2自己 查看
     */
    @JsonIgnore
    private Integer privacy = 0;
    
    /**
     * 被查看人userId
     */
    @NotNull(message = "请求数据错误")
    private Long userId;
    
    /**
     * 查看人userId
     */
    private Long lookUserId;
    
}
