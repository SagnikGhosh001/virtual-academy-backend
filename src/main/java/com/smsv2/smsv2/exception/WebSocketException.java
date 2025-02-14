package com.smsv2.smsv2.exception;

public class WebSocketException extends RuntimeException {
	 private String msg;
    public WebSocketException(String msg) {
    	 super(msg,null, false, false);
         this.msg = msg;
    }
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
