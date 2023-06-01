package com.jh.elastictransservice.result;

/**
 * @author liqijian
 */
public enum ExceptionMsg {
    SUCCESS(200, "操作成功"),
    ERROR(500, "操作失败"),
    NEED_LOGIN(401, "请登录后操作"),
    ILLEGAL_ARGUMENT(400, "参数错误"),
    REPETITIVE_OPERATION(402, "重复操作"),
    NO_PERMISSION(403, "无权限操作"),
    NOT_FOUND(404, "找不到资源"),
    NOT_SUPPORTED(405, "请求方法不支持"),
    INTERNAL_SERVER_ERROR(500, "服务器异常"),
    SERVER_BUSY(503, "服务器繁忙，请稍后再试");


    private final int code;
    private final String desc;

    ExceptionMsg(int code, String desc) {
        this.code = code;
        this.desc = desc;
        }


    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
