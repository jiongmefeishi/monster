package cn.zqtao.monster.exception;

/**
 * 角色未授权
 */
public class UnauthorizedRoleException extends RuntimeException {
    public UnauthorizedRoleException() {
        super();
    }

    public UnauthorizedRoleException(String message) {
        super(message);
    }
}
