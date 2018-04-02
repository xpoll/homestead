package cn.blmdz.home.baiduyun;

import java.util.List;

/**
 * 百度云实际链接下载地址
 * @author yongzongyang
 * @date 2018年4月2日
 */
public interface BaiduyunService {

    /**
     * 文件信息获取
     * @param id Cookie BAIDUID
     * @param key key
     * @param pwd pwd
     * @return
     */
    List<BaiduyunFileInfo> getFileInfo(String id, String key, String pwd, Boolean encryption);
}
