package com.imagemodify.demo.exception;

import com.imagemodify.demo.constant.ResponseCode;
import com.imagemodify.demo.util.ResponseUtil;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class MyControllerAdvice {


    @ResponseBody
    @ExceptionHandler(ServiceException.class)
    public ResponseUtil serviceExceptionHandler(ServiceException se) {
        //System.out.println("-----------serviceExceptionHandler begin");
        return ResponseUtil.errorException(se);
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseUtil exceptionHandler(Exception e) {
        //log.error("Exception: ", e);
        System.out.println("e:");
        System.out.println(e.toString());
        System.out.println("-----------exceptionHandler begin");
        return ResponseUtil.errorMsg(ResponseCode.SERVER_ERROR.getMsg());
    }

}