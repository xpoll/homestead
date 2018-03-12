package cn.blmdz.test.dtalk;

import java.util.List;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.common.collect.Lists;

import cn.blmdz.home.util.JsonMapper;
import lombok.Data;

public class Rebot {

	/**
	 * 1. text类型
	 * 2. link类型
	 * 3. markdown类型
	 * 4. 整体跳转ActionCard类型
	 * 5. 独立跳转ActionCard类型
	 * 6. FeedCard类型
	 * https://open-doc.dingtalk.com/docs/doc.htm?spm=a219a.7629140.0.0.karFPe&treeId=257&articleId=105735&docType=1#s2
	 * @throws InterruptedException 
	 */
	
	public static void main(String[] args) throws InterruptedException {
		// 1. text类型
		text();
		link();
		markdown();
	}
	
	/**
	 * text类型
	 */
	public static void text() {
		RebotText text = new RebotText();
		text.setContent("啦啦啦(～￣▽￣～");
		
		RebotMsg msg = new RebotMsg();
		msg.setMsgtype(RebotType.text);
		msg.setText(text);
		
		post(JsonMapper.nonDefaultMapper().toJson(msg));
	}
	
	/**
	 * link类型
	 */
	public static void link() {
		RebotLink link = new RebotLink();
		link.setTitle("啦啦啦(～￣▽￣～");
		link.setText("我是卖报的小行家，O(∩_∩)O哈哈~");
		link.setMessageUrl("http://blmdz.xyz");
		
		RebotMsg msg = new RebotMsg();
		msg.setMsgtype(RebotType.link);
		msg.setLink(link);
		
		post(JsonMapper.nonDefaultMapper().toJson(msg));
	}
	
	/**
	 * markdown类型
	 */
	public static void markdown() {
		RebotMarkdown markdown = new RebotMarkdown();
		markdown.setTitle("啦啦啦(～￣▽￣～");
		markdown.setText( "#### 杭州天气\n" +
                "> 9度，西北风1级，空气良89，相对温度73%\n\n" +
                "> ![screenshot](http://xpoll.blmdz.cn/img/call-to-action-bg.jpg)\n"  +
                "> ###### 10点20分发布 [天气](http://www.thinkpage.cn/) \n");
		
		RebotMsg msg = new RebotMsg();
		msg.setMsgtype(RebotType.markdown);
		msg.setMarkdown(markdown);
		
		post(JsonMapper.nonDefaultMapper().toJson(msg));
	}
	
	/**
	 * post
	 */
	public static void post(String msg) {
		List<String> urls = Lists.newArrayList();
		urls.add("https://oapi.dingtalk.com/robot/send?access_token=b1c1fb94545ef80c61688ed9c28ee5ce3324561e5b2d60d12eb08156a3fe3a1b");
		urls.add("https://oapi.dingtalk.com/robot/send?access_token=170206ff72d5ecb189f166fb1a65aabd55ecd41c0665b9ab5123b7e3dbeb9ea1");
		urls.add("https://oapi.dingtalk.com/robot/send?access_token=2887888f8ae0149e47efa97d4d14ee17e1955c573613542978d5cb5bbe349d62");
		HttpRequest request = HttpRequest.post(urls.get((int) (urls.size() * Math.random())));
		request.header(HttpRequest.HEADER_CONTENT_TYPE, HttpRequest.CONTENT_TYPE_JSON);
		request.send(msg);
		System.out.println(request.ok());
		System.out.println(request.body());
		
	}

}

enum RebotType {
	text, link, markdown, actionCard;
}

@Data
class RebotMsg {
	private RebotType msgtype;
	private RebotText text;
	private RebotLink link;
	private RebotMarkdown markdown;
	private RebotAt at;
}

@Data
class RebotText {
	private String content;
}

@Data
class RebotLink {
	private String title;
	private String text;
	private String priUrl;
	private String messageUrl;
}

@Data
class RebotMarkdown {
	private String title;
	private String text;
}

/**
 * RebotType.text/markdown
 */
@Data
class RebotAt {
	private List<String> atMobiles; // 手机号
	private Boolean isAtAll = Boolean.FALSE.booleanValue();
}
