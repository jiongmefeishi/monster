package cn.zqtao.monster.exception;

public class ArticleFetchFailedException extends RuntimeException {
    public ArticleFetchFailedException() {
        super("文章获取失败！");
    }

    public ArticleFetchFailedException(String message) {
        super(message);
    }
}
