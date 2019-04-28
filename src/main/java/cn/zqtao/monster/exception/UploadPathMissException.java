package cn.zqtao.monster.exception;

public class UploadPathMissException extends RuntimeException {
    public UploadPathMissException() {
        super();
    }

    public UploadPathMissException(String message) {
        super(message);
    }

    public UploadPathMissException(String message, Throwable cause) {
        super(message, cause);
    }

    public UploadPathMissException(Throwable cause) {
        super(cause);
    }
}
