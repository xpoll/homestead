package cn.blmdz.home.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
 * 用户文件
 */
@Data
public class UserFile {
    private Long id;//ID
    private Long userId;//用户ID
    private Integer fileType;//文件类型
    private Long folderId;//文件夹ID
    private String name;//名称
    private String path;//路径
    private Integer size;//文件大小
    private String md5;//文件摘要
    private String extraJson;//其他json数据
    @JsonIgnore
    private Boolean status;//状态(0不可用1可用)
    @JsonIgnore
    private Date createTime;//创建时间
    @JsonIgnore
    private Date updateTime;//更新时间
}
