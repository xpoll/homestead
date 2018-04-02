package cn.blmdz.home.baiduyun;

import java.math.BigDecimal;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class BaiduyunFileInfo {
    private Long fsId;
    private Boolean dir = true;// 1文件夹 0 文件
    private String name;
    private String path;
    @Getter(AccessLevel.NONE)
    private Long size;
    @Setter(AccessLevel.NONE)
    private String sizeShow;
    private String link;
    
    public void setSize(Long size) {
        if (size == null) size = 0L;
        this.size = size;
        if (size < BaiduyunConstant.kk) sizeShow = size + "B";
        else if (size <= (BaiduyunConstant.kk * BaiduyunConstant.kk)) sizeShow = new BigDecimal(size).divide(new BigDecimal(BaiduyunConstant.kk)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "KB";
        else if (size <= (BaiduyunConstant.kk * BaiduyunConstant.kk * BaiduyunConstant.kk)) sizeShow = new BigDecimal(size).divide(new BigDecimal(BaiduyunConstant.kk * BaiduyunConstant.kk)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "MB";
        else sizeShow = new BigDecimal(size).divide(new BigDecimal(BaiduyunConstant.kk * BaiduyunConstant.kk * BaiduyunConstant.kk)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "GB";
    }
}
