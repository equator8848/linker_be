package xyz.equator8848.linker.service.user;

public enum LoginStatus {
    SUCCESS("登录成功"),

    USER_NOT_FOUND("账号密码不匹配"),

    PASSWORD_NOT_MATCH("账号密码不匹配"),

    FORBIDDEN("账号未开通或被冻结"),

    NEED_SET_PASSWORD("密码未设置，需要重置密码");

    private String message;

    LoginStatus(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
