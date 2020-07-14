package com.imagemodify.demo.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.imagemodify.demo.constant.ResponseCode;
import com.imagemodify.demo.exception.ServiceException;

import java.io.Serializable;

public class ResponseUtil implements Serializable{

    private static final long serialVersionUID = 7498483649536881777L;

    private Integer status;

    private String msg;

    private Object data;

    public ResponseUtil() {
    }

    public ResponseUtil(Integer status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    @JsonIgnore
    public boolean isSuccess() {
        return this.status == ResponseCode.SUCCESS.getCode();
    }

    public static ResponseUtil success() {
        return new ResponseUtil(ResponseCode.SUCCESS.getCode(), null, null);
    }

    public static ResponseUtil success(String msg) {
        return new ResponseUtil(ResponseCode.SUCCESS.getCode(), msg, null);
    }

    public static ResponseUtil success(Object data) {
        return new ResponseUtil(ResponseCode.SUCCESS.getCode(), null, data);
    }

    public static ResponseUtil success(String msg, Object data) {
        return new ResponseUtil(ResponseCode.SUCCESS.getCode(), msg, data);
    }

    public static ResponseUtil errorException(ServiceException se) {
        return new ResponseUtil(se.getCode(), se.getMsg(), null);
    }

    public static ResponseUtil errorMsg(String msg) {
        return new ResponseUtil(ResponseCode.ERROR.getCode(), msg, null);
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}