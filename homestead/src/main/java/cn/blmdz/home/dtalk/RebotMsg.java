package cn.blmdz.home.dtalk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RebotMsg {
    private RebotType msgtype;
    private RebotText text;
    private RebotLink link;
    private RebotMarkdown markdown;
    private RebotAt at;
}