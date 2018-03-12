package cn.blmdz.home.services.manager;

import cn.blmdz.home.base.BasePage;
import cn.blmdz.home.model.search.TalkSearch;
import cn.blmdz.home.model.vo.TalkVo;

public interface TalkManager {

    /**
     * 查看用户第一条TALK
     */
    TalkVo firstTalkByUserId(TalkSearch talkSearch);
    
    /**
     * 分页查看用户TALK
     */
    BasePage<TalkSearch, TalkVo> pageTalkByUserId(BasePage<TalkSearch, TalkVo> search);
    
}
