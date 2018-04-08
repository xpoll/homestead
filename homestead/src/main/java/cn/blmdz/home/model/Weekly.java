package cn.blmdz.home.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import cn.blmdz.home.base.Constants;
import lombok.Data;

/**
 * 周刊
 * @author yongzongyang
 * @date 2018年4月8日
 */
@Data
public class Weekly {

    private Long id;
    private Integer category;// 类目ID
    private Long fsid;// 百度云文件序号
    private String name;// 名称
    private Integer start;// 页码起始
    private Integer end;// 页码结束
    @JsonIgnore
    private Boolean status;//状态(0不可用1可用)
    @JsonFormat(pattern = Constants.YH, timezone = Constants.TIMEZONE)
    private Date createTime;//创建时间
    @JsonFormat(pattern = Constants.YH, timezone = Constants.TIMEZONE)
    private Date updateTime;//更新时间
}
