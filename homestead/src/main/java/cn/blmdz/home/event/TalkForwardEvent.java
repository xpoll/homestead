package cn.blmdz.home.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TalkForwardEvent {
    private Long btalkId;//上节点TALKID
    private Long talkId;//本节点TALKID(当前TALKID)
    private Long superId;//异步处理时保证顺序--暂时未写 TODO
}
