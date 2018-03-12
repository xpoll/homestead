package cn.blmdz.home.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
 * 用户文件夹
 */
@Data
public class UserFolder {
    private Long id;//ID
    private Long userId;//用户ID
    private Long pid;//父节点
    private Boolean hasChildren;//是否有子文件
    private String name;//名称
    @JsonIgnore
    private Boolean status;//状态(0不可用1可用)
    @JsonIgnore
    private Date createTime;//创建时间
    @JsonIgnore
    private Date updateTime;//更新时间
}
