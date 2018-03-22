package cn.blmdz.home.services.manager.impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import cn.blmdz.home.base.BaseUser;
import cn.blmdz.home.enums.SocketType;
import cn.blmdz.home.model.base.QuestionData;
import cn.blmdz.home.model.msg.BaseMsg;
import cn.blmdz.home.services.manager.SocketManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SocketManagers implements SocketManager {
	
    // 先暂时不考虑线程问题 TODO
    
    // 所有在线人
    public static final Map<Long, WebSocketSession> allUsers = Maps.newHashMap();
    // 游戏匹配等待者
    public static final Set<Long> gameWaitUsers = Sets.newHashSet();
    // 游戏中的人<userId, roomId>，与gameRunRooms同步
    public static final Map<Long, Long> gameRunUsers = Maps.newHashMap();
    // 游戏房间，与gameRunUser同步
    public static final Map<Long, List<Long>> gameRunRooms = Maps.newHashMap();
    // 游戏题目
    public static final Map<Long, List<QuestionData>> gameRoomQuestion = Maps.newHashMap();
    
    // 游戏人数
    public static int gameUserNum = 2;
    
    public static long gameTimeMs = 30000;

    @Override
    public void connect(BaseUser user, WebSocketSession session) {
        log.info("{} success connected.", user.getNick());
        allUsers.put(user.getId(), session);
    }
    
    @PostConstruct
    public void r() {
        log.info("match.........");
    	new GameAllocationThread().start();
    }
    

    @Override
    public void message(BaseUser user, BaseMsg baseMsg) {
        log.info("{} send: {}.", user.getNick(), baseMsg.toString());
        SocketType type = SocketType.from(baseMsg.getType());
        switch (type) {
        case GAME_MATCH_REQUEST: // (11, "游戏-匹配排队请求"),
            gameWaitUsers.add(user.getId());
            sendMessage(user.getId(), new BaseMsg(SocketType.REQUEST_SUCCESS.value(), null));
            break;
            
        case GAME_MATCH_CANCEL: // (12, "游戏-匹配排队取消"),
            gameWaitUsers.remove(user.getId());
            sendMessage(user.getId(), new BaseMsg(SocketType.REQUEST_SUCCESS.value(), null));
            break;

        case DRAW_SCRAWL: // (1, "绘画"),
        case DRAW_RESET_CLEAR: // (5, "清除画布"),
            Long roomId = gameRunUsers.get(user.getId());
            if (roomId == null) break;
            
			for (Long id : gameRunRooms.get(roomId)) {
				if (Objects.equal(user.getId(), id)) continue;
				sendMessage(id, baseMsg);
			}
            break;
        case GAME_TALK_TEAM: // (21, "游戏-聊天队伍"),
        	Long roomId1 = gameRunUsers.get(user.getId());
        	if (roomId1 == null) break;
        	List<QuestionData> qs = gameRoomQuestion.get(roomId1);
        	if (CollectionUtils.isEmpty(qs)) {
    			for (Long id : gameRunRooms.get(roomId1)) {
    				sendMessage(id, baseMsg);
    			}
        	} else {
        		QuestionData q = qs.get(qs.size() - 1);
        		boolean answer = Objects.equal(q.getAnswer(), baseMsg.getMsg().trim());
        		if (!answer) {
	    			for (Long id : gameRunRooms.get(roomId1)) {
	    				sendMessage(id, baseMsg);
	    			}
        		} else {
        			q.getParts().add(user.getId());
	    			for (Long id : gameRunRooms.get(roomId1)) {
	    				if ( Objects.equal(user.getId(), id)) {
	    					sendMessage(id, new BaseMsg(SocketType.GAME_GUESS_SUCCESS.value(), SocketType.GAME_GUESS_SUCCESS.description()));
	    				} else {
	    					sendMessage(id, new BaseMsg(SocketType.GAME_GUESS_SUCCESS.value(), user.getNick() + " " + SocketType.GAME_GUESS_SUCCESS.description()));
	    				}
	    			}
        		}
        	}
            break;

        default:
            break;
        }
    }

    @Override
    public void close(BaseUser user) {
        log.info("{} success closed.", user.getNick());
        allUsers.remove(user.getId());
    }

    @Override
    public void error(BaseUser user) {
        log.info("{} connection error.", user.getNick());
        allUsers.remove(user.getId());
    }
    
    public static void sendMessage(Long userId, BaseMsg baseMsg) {
    	try {
    		allUsers.get(userId).sendMessage(new TextMessage(baseMsg.toString()));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}

/**
 * 匹配分配房间
 */
@Slf4j
class GameAllocationThread extends Thread {

    @Override
    public void run() {
        while(true) {
        	try {
				GameAllocationThread.sleep(5000L);
			} catch (InterruptedException e) {
			}
            log.info("all user: {}, game wait user: {}", SocketManagers.allUsers.keySet(), SocketManagers.gameWaitUsers);
            if (SocketManagers.gameWaitUsers.size() < SocketManagers.gameUserNum) continue;
            
            int times = SocketManagers.gameWaitUsers.size()/SocketManagers.gameUserNum;
            for (int i = 0; i < times; i++) {
            	List<Long> gameRunRoomUser = Lists.newArrayList();
            	Long roomId = new Date().getTime();
            	for (int j = 0; j < SocketManagers.gameUserNum; j++) {
            		Long id = SocketManagers.gameWaitUsers.iterator().next();
            		gameRunRoomUser.add(id);
            		SocketManagers.gameRunUsers.put(id, roomId);
            		SocketManagers.gameWaitUsers.remove(id);
            	}
            	SocketManagers.gameRunRooms.put(roomId, gameRunRoomUser);
            	new GamePostStartThread(roomId).start();
			}
            
        }
    }
}

/**
 * game.start.step.01
 * 分配房间后通知前端开始游戏
 * 
 * game.start.step.02
 * 发布游戏通知。（游戏开始，1为画 其他为猜）
 * 游戏包含: 提示、答案、画家、猜对的人
 * Map<roomId, List<题>>
 * 
 * game.start.step.03
 * 游戏结束计算分数
 * 
 * game.start.step.04
 * 将游戏者移至大厅
 */
@AllArgsConstructor
class GamePostStartThread extends Thread {
	private Long roomId;
    @Override
    public void run() {
    	List<Long> gameRunRoomUser = SocketManagers.gameRunRooms.get(roomId);
    	// 通知游戏开始
		for (Long id : gameRunRoomUser) {
			SocketManagers.sendMessage(id, new BaseMsg(SocketType.GAME_START.value(), null));
		}
		// 第一道题 start
    	List<QuestionData> questions = Lists.newArrayList();
		List<Long> users = SocketManagers.gameRunRooms.get(roomId);
		QuestionData question = new QuestionData();
		BeanUtils.copyProperties(QuestionData.questions().get(((int) (Math.random() * QuestionData.questions().size())) + 1), question);
		question.setPartId(users.get(0));
		question.setParts(Sets.newHashSet());
		question.setTime(System.currentTimeMillis());
		questions.add(question);
		SocketManagers.gameRoomQuestion.put(roomId, questions);
		for (int i = 0; i < users.size(); i++) {
			SocketManagers.sendMessage(
				users.get(i),
				new BaseMsg(i == 0 ? SocketType.GAME_DRAW.value() : SocketType.GAME_GUESS.value(),
				i == 0 ? "" : question.getTips()));// TODO
		}
    	while (true) {
    	    // 每2秒检查是否答完题目
			try {
				GamePostStartThread.sleep(1000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			List<QuestionData> qs = SocketManagers.gameRoomQuestion.get(roomId);
			Integer currentQuestion = qs.size();
			Long time = System.currentTimeMillis();
			QuestionData q = qs.get(qs.size() - 1);
			// 所有人答对了 或者 时间到了 next
			if (q.getParts().size() == (SocketManagers.gameUserNum - 1) || (time - q.getTime()) >= SocketManagers.gameTimeMs) {
				// 显示正确答案;
		    	for (Long id : users) {
					SocketManagers.sendMessage(id, new BaseMsg(SocketType.GAME_ANSWER.value(), q.getAnswer()));
				}
		    	// 结束
		    	if (currentQuestion == SocketManagers.gameUserNum) {
		    		Map<Long, Long> questionScore = Maps.newHashMap();
		    		for (Long id : users) {
		    			questionScore.put(id, 0L);
					}
		    		for (QuestionData questionData : qs) {
						for (Long userId : questionData.getParts()) {
							questionScore.put(userId, questionScore.get(userId) + 1);
						}
					}
    				for (Long id : users) {
    					SocketManagers.sendMessage(id, new BaseMsg(SocketType.GAME_END.value(), "分数: " + questionScore.get(id)));
    				}
    				// 结束移除
    				SocketManagers.gameRoomQuestion.remove(roomId);
    				for (Long id : SocketManagers.gameRunRooms.get(roomId)) {
    					SocketManagers.gameRunUsers.remove(id);
					}
    				SocketManagers.gameRunRooms.remove(roomId);
	    			return;
		    	}
		    	// 下一道题 start
				question = new QuestionData();
				BeanUtils.copyProperties(QuestionData.questions().get(((int) (Math.random() * QuestionData.questions().size())) + 1), question);
				question.setPartId(users.get(currentQuestion));
				question.setParts(Sets.newHashSet());
				question.setTime(System.currentTimeMillis());
				questions.add(question);
				SocketManagers.gameRoomQuestion.put(roomId, questions);
				for (int i = 0; i < users.size(); i++) {
					SocketManagers.sendMessage(
						users.get(i),
						new BaseMsg(i == currentQuestion ? SocketType.GAME_DRAW.value() : SocketType.GAME_GUESS.value(),
						i == currentQuestion ? question.getAnswer() : question.getTips()));
				}
			} 
    	}
    }
}
