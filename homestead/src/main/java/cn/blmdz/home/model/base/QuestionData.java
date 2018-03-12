package cn.blmdz.home.model.base;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionData {

	private Long id; // 主键ID
	private String tips; // 提示
	private String answer; // 答案
	private Long time; // 开始时间
	private Long partId; // 画家
	private Set<Long> parts; // 猜对的人
	
	public static List<QuestionData> questions() {
		List<QuestionData> questions = Lists.newArrayList();
		questions.add(new QuestionData(1L, "成语", "落井下石", null, null, null));
		questions.add(new QuestionData(2L, "成语", "三长两短", null, null, null));
		questions.add(new QuestionData(3L, "成语", "画饼充饥", null, null, null));
		questions.add(new QuestionData(4L, "成语", "虎头蛇尾", null, null, null));
		questions.add(new QuestionData(5L, "成语", "泪流满面", null, null, null));
		questions.add(new QuestionData(6L, "成语", "捧腹大笑", null, null, null));
		questions.add(new QuestionData(7L, "成语", "画蛇添足", null, null, null));
		questions.add(new QuestionData(8L, "成语", "一手遮天", null, null, null));
		questions.add(new QuestionData(9L, "成语", "羊入虎口", null, null, null));
		questions.add(new QuestionData(10L, "成语", "掩耳盗铃", null, null, null));
		questions.add(new QuestionData(11L, "成语", "三头六臂", null, null, null));
		questions.add(new QuestionData(12L, "成语", "愚公移山", null, null, null));
		questions.add(new QuestionData(13L, "成语", "对牛弹琴", null, null, null));
		questions.add(new QuestionData(14L, "成语", "刻舟求剑", null, null, null));
		questions.add(new QuestionData(15L, "成语", "七上八下", null, null, null));
		return questions;
	}
}
