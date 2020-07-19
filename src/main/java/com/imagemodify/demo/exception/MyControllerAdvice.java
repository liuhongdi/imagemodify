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
        return ResponseUtil.errorException(se);
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseUtil exceptionHandler(Exception e) {
        return ResponseUtil.errorMsg(ResponseCode.SERVER_ERROR.getMsg());
    }

}