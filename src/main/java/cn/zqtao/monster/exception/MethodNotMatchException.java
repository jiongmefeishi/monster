package cn.zqtao.monster.exception;

public class MethodNotMatchException extends RuntimeException {
    public MethodNotMatchException() {
        super();
    }

    public MethodNotMatchException(String message) {
        super(message);
    }

    public MethodNotMatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public MethodNotMatchException(Throwable cause) {
        super(cause);
    }
}
