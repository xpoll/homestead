package cn.blmdz.home.dtalk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RebotLink {
    private String title;
    private String text;
    private String priUrl;
    private String messageUrl;
}