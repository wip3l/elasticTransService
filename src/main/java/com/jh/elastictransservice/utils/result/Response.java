package com.jh.elastictransservice.utils.result;

/**
 * @author liqijian
 */

//返回对象实体
public class Response {
    private int responseCode = 200;
    private String responseMsg = "操作成功";

    public Response() {
    }

    public Response(ExceptionMsg exceptionMsg) {
        this.responseCode = exceptionMsg.getCode();
        this.responseMsg = exceptionMsg.getDesc();
    }

    public Response(int responseCode) {
        this.responseCode = responseCode;
        if (responseCode == ExceptionMsg.SUCCESS.getCode()) {
            this.responseMsg = ExceptionMsg.SUCCESS.getDesc();
        } else {
            this.responseMsg = ExceptionMsg.ERROR.getDesc();
        }
    }
    public Response(int responseCode, String responseMsg) {
        this.responseCode = responseCode;
        this.responseMsg = responseMsg;
    }
    public int getResponseCode() {
        return responseCode;
    }
    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }
    public String getResponseMsg() {
        return responseMsg;
    }
    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    @Override
    public String toString() {
        return "Response{" +
                "responseCode=" + responseCode +
                ", responseMsg='" + responseMsg + '\'' +
                '}';
    }
}
