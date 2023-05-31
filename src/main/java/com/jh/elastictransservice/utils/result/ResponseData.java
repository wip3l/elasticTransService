package com.jh.elastictransservice.utils.result;

/**
 * @author liqijian
 */
public class ResponseData extends Response{
    private Object data;

    public ResponseData() {
    }

    public ResponseData(ExceptionMsg exceptionMsg) {
        super(exceptionMsg);
    }

    public ResponseData(int responseCode) {
        super(responseCode);
    }

    public ResponseData(int responseCode, String responseMsg) {
        super(responseCode, responseMsg);
    }

    public ResponseData(int responseCode, String responseMsg, Object data) {
        super(responseCode, responseMsg);
        this.data = data;
    }

    public ResponseData(Object data) {
        this.data = data;
    }

    public ResponseData(Object data, ExceptionMsg exceptionMsg) {
        super(exceptionMsg);
        this.data = data;
    }

    public ResponseData(Object data, int responseCode) {
        super(responseCode);
        this.data = data;
    }

    public ResponseData(Object data, int responseCode, String responseMsg) {
        super(responseCode, responseMsg);
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public ResponseData setData(Object data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return "ResponseData{" +
                "data=" + data +
                '}';
    }
}
