package cn.zqtao.monster.exception;

public class NoteFetchFailedException extends RuntimeException {
    public NoteFetchFailedException() {
        super("笔记获取失败！");
    }
}
