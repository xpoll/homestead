package cn.blmdz.test.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import cn.blmdz.home.HomesteadApplication;
import cn.blmdz.home.base.BasePage;
import cn.blmdz.home.enums.EnumsPrivacy;
import cn.blmdz.home.model.Talk;
import cn.blmdz.home.model.TalkForward;
import cn.blmdz.home.model.TalkReply;
import cn.blmdz.home.model.search.TalkSearch;
import cn.blmdz.home.model.valid.TalkForwardValid;
import cn.blmdz.home.model.valid.TalkReplyValid;
import cn.blmdz.home.model.valid.TalkValid;
import cn.blmdz.home.model.vo.TalkVo;
import cn.blmdz.home.services.manager.TalkManager;
import cn.blmdz.home.services.service.TalkService;
import cn.blmdz.home.util.JsonMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = HomesteadApplication.class)
@WebAppConfiguration
//@Transactional
//@Rollback
@ActiveProfiles("local")
public class TalkServicesTest {
    
    public static JsonMapper json = JsonMapper.nonEmptyMapper();

    @Autowired
    TalkService talkService;
    
    @Autowired
    TalkManager talkManager;
    
    @Test
    public void manager() {
        TalkSearch talkSearch = new TalkSearch();
        talkSearch.setLookUserId(8L);
        talkSearch.setUserId(9L);
        TalkVo vo1 = talkManager.firstTalkByUserId(talkSearch);
        System.out.println(json.toJson(vo1));
        talkSearch.setLookUserId(9L);
        talkSearch.setUserId(8L);
        TalkVo vo2 = talkManager.firstTalkByUserId(talkSearch);
        System.out.println(json.toJson(vo2));
    }
    
	@Test
	public void test() throws Exception {
	    // 发布talk
	    TalkValid talkValid = new TalkValid();
	    talkValid.setPrivacy(EnumsPrivacy.PUBLIC.value());
	    talkValid.setContent("今天是小年的第二天");
//	    talkService.issue(8L, talkValid);
	    // 点赞
//        talkService.like(8L, 1L);
	    // 取消点赞
//        talkService.unlike(8L, 1L);
	    // 评论
	    TalkReplyValid talkReplyValid = new TalkReplyValid();
	    talkReplyValid.setContent("yyy, 2018年要好好对你哦1");
	    talkReplyValid.setTalkId(1L);
//	    talkService.reply(8L, talkReplyValid);
	    // 回复
	    talkReplyValid.setContent("(#^.^#，她敢不听话");
	    talkReplyValid.setSuperId(3L);
	    talkReplyValid.setReplyToId(8L);
	    talkReplyValid.setTalkId(1L);
//	    talkService.reply(9L, talkReplyValid);
	    // 转发
	    TalkForwardValid talkForwardValid = new TalkForwardValid();
	    talkForwardValid.setContent("w(ﾟДﾟ)w哇哦");
	    talkForwardValid.setTalkId(1L);
	    talkForwardValid.setPrivacy(EnumsPrivacy.PUBLIC.value());
//	    Long talkId = talkService.forward(9L, talkForwardValid);
//	    TalkForwardEvent event = new TalkForwardEvent(talkForwardValid.getTalkId(), talkId, 0L);
//	    talkService.forwardEvent(event);
	    // 查看TALK分页
	    BasePage<TalkSearch, Talk> tsearch = new BasePage<>();
	    TalkSearch talkSearch = new TalkSearch();
	    talkSearch.setPrivacy(EnumsPrivacy.PUBLIC.value());
	    talkSearch.setUserId(8L);
	    tsearch.setMode(talkSearch);
//	    tsearch = talkService.pageTalkBySelect(tsearch);
	    for (Talk talk : tsearch.getData()) {
            System.out.println(talk);
        }
	    // 查看转发分页
	    BasePage<Long, TalkForward> search = new BasePage<>();
	    search.setMode(1L);
	    search = talkService.pageTalkForwardBySelect(search);
        for (TalkForward talkForward : search.getData()) {
            System.out.println(talkForward);
        }
        // 查看转发评论赞个数
        // 查看评论
        List<TalkReply> talkReplys = talkService.talkReplyBySelect(1L);
        for (TalkReply talkReply : talkReplys) {
            System.out.println(talkReply);
        }
	}
}
