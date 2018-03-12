package cn.blmdz.home.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.blmdz.home.base.Constants;
import lombok.Data;

/**
 * 基础数据
 */
@Data
public class BaseData {
    private Long id;//ID
    private Integer type;//数据类型
    private String dataJson;//基础数据json值
    @JsonFormat(pattern = Constants.YH, timezone = Constants.TIMEZONE)
    private Date updateTime;//创建时间
    @JsonFormat(pattern = Constants.YH, timezone = Constants.TIMEZONE)
    private Date createTime;//更新时间
}
