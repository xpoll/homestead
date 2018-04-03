package cn.blmdz.home.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.blmdz.home.base.BasePage;
import cn.blmdz.home.base.BaseUser;
import cn.blmdz.home.base.Response;
import cn.blmdz.home.model.search.TalkSearch;
import cn.blmdz.home.model.valid.TalkReplyValid;
import cn.blmdz.home.model.valid.TalkValid;
import cn.blmdz.home.model.vo.TalkVo;
import cn.blmdz.home.services.cache.TalkCache;
import cn.blmdz.home.services.manager.TalkManager;
import cn.blmdz.home.services.service.TalkService;

@RestController
@RequestMapping(value="/api/talk")
public class RestTalkController {

    @Autowired
    private TalkService talkService;
    @Autowired
    private TalkManager talkManager;

    @Autowired
    private TalkCache talkCache;
    

    @RequestMapping(value="issue", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
    public Response<Boolean> issue(HttpServletRequest request, @Valid @RequestBody TalkValid talkValid) {
        BaseUser baseUser = (BaseUser) request.getSession().getAttribute("user");
        if (baseUser == null) return Response.build(null);
        
        talkService.issue(baseUser.getId(), talkValid);
        
        return Response.ok();
    }
    
    @RequestMapping(value="reply", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
    public Response<Boolean> reply(HttpServletRequest request, @Valid @RequestBody TalkReplyValid talkReplyValid) {

        BaseUser baseUser = (BaseUser) request.getSession().getAttribute("user");
        if (baseUser == null) return Response.build(null);
        
        if (talkReplyValid.getSuperId() == null) talkReplyValid.setReplyToId(null);
        if (talkReplyValid.getReplyToId() == null) talkReplyValid.setSuperId(null);
        talkService.reply(baseUser.getId(), talkReplyValid);

        talkCache.refreshReply(talkReplyValid.getTalkId());
        return Response.ok();
        
    }
    
    @RequestMapping(value="like", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
    public Response<Boolean> like(HttpServletRequest request, @RequestParam("talkId") Long talkId) {

        if (talkId == null) return Response.build(null);
        BaseUser baseUser = (BaseUser) request.getSession().getAttribute("user");
        if (baseUser == null) return Response.build(null);
        
        talkService.like(baseUser.getId(), talkId);
        
        talkCache.refreshLike(talkId);
        return Response.ok();
        
    }
    
    @RequestMapping(value="unlike", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
    public Response<Boolean> unlike(HttpServletRequest request, @RequestParam("talkId") Long talkId) {

        if (talkId == null) return Response.build(null);
        BaseUser baseUser = (BaseUser) request.getSession().getAttribute("user");
        if (baseUser == null) return Response.build(null);
        
        talkService.unlike(baseUser.getId(), talkId);

        talkCache.refreshLike(talkId);
        return Response.ok();
    }

    @RequestMapping(value="first", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
    public Response<TalkVo> firstTalk(HttpServletRequest request, @RequestParam("userId") Long userId) {

        if (userId == null) return Response.build(null);
        BaseUser baseUser = (BaseUser) request.getSession().getAttribute("user");
        if (baseUser == null) return Response.build(null);
        
        TalkSearch talkSearch = new TalkSearch();
        talkSearch.setLookUserId(baseUser.getId());
        talkSearch.setUserId(userId);
        
        return Response.build(talkManager.firstTalkByUserId(talkSearch));
    }

    @RequestMapping(value="page", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
    public Response<BasePage<TalkSearch, TalkVo>> pageTalk(HttpServletRequest request, @Valid @RequestBody BasePage<TalkSearch, TalkVo> search) {

        BaseUser baseUser = (BaseUser) request.getSession().getAttribute("user");
        if (baseUser == null) return Response.build(null);
        
        search.getMode().setLookUserId(baseUser.getId());
        
        return Response.build(talkManager.pageTalkByUserId(search));
    }


//    @RequestMapping(value="reply/{talkId}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
//    public Response<List<TalkReply>> pageTalkReply(HttpServletRequest request, @PathVariable("talkId") Long talkId) {
//
//        return Response.build(talkService.talkReplyBySelect(talkId));
//    }
}
