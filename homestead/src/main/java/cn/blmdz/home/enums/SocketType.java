package cn.blmdz.home.enums;

public enum SocketType {
	
	REQUEST_FAILD(-1, "请求失败"),
	REQUEST_SUCCESS(0, "请求成功"),
    
    DRAW_MOUSE_DOWN(1, "绘画-鼠标单机点下去"),
    DRAW_MOUSE_MOVE(2, "绘画-鼠标移动"),
    DRAW_MOUSE_UP(3, "绘画-鼠标单机放开"),
    DRAW_MOUSE_LEAVE(4, "绘画-鼠标立刻"),
    DRAW_RESET_CLEAR(5, "绘画-按钮清楚画布"),
    
    GAME_MATCH_REQUEST(11, "游戏-匹配排队请求"),
    GAME_MATCH_CANCEL(12, "游戏-匹配排队取消"),
    GAME_START(21, "游戏-开始"),
    GAME_END(22, "游戏-结束"),
    GAME_DRAW(31, "游戏-轮到我画"),
    GAME_GUESS(32, "游戏-轮到我猜"),
    GAME_GUESS_SUCCESS(33, "游戏-猜中"),
    GAME_GUESS_FAILD(34, "游戏-未猜中"),
    GAME_ANSWER(35, "游戏-答案"),
    
    GAME_TALK_TEAM(41, "游戏-聊天队伍"),
    ;

    private final int value;
    private final String description;

    private SocketType(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public static SocketType from(Integer value) {
        for (SocketType item : values()) {
            if (Integer.valueOf(item.value) == value) {
                return item;
            }
        }

        return null;
    }

    public int value() {
        return this.value;
    }

    public String description() {
        return this.description;
    }


}
