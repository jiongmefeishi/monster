package cn.zqtao.monster.exception;

/**
 * 用户未登录或超时
 */
public class UserNotLoginException extends RuntimeException {
    public UserNotLoginException() {
        super();
    }

    public UserNotLoginException(String message) {
        super(message);
    }
}
