package cn.blmdz.home.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.blmdz.home.baiduyun.BaiduyunFileInfo;
import cn.blmdz.home.baiduyun.BaiduyunRequestVo;
import cn.blmdz.home.baiduyun.BaiduyunService;
import cn.blmdz.home.base.Response;
import cn.blmdz.home.exception.WebJspException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value="/api/s")
public class RestBaiduYunController {
    
    @Autowired
    private BaiduyunService baiduyunService;

    @RequestMapping(method=RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<List<BaiduyunFileInfo>> tran(@Valid @RequestBody BaiduyunRequestVo vo) {
    	
    	log.info("baiduyun vo :{}", vo);
    	if (vo.getEncryption() && StringUtils.isBlank(vo.getPwd())) throw new WebJspException("请输入密码");
        
        return Response.build(baiduyunService.getFileInfo(vo.getId(), vo.getKey(), vo.getPwd(), vo.getEncryption()));
    }
    
}
