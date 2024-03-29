package com.baichen.base.controller;

import com.baichen.entity.Result;
import com.baichen.entity.StatusCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Program: BaseExceptionHandler
 * @Author: baichen
 * @Description: 统一异常处理类
 */
@ControllerAdvice       // 也可以改成@RestControllerAdvice，下面就不用@ResponseBody了
public class BaseExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result error(Exception e){
        e.printStackTrace();
        return new Result(false, StatusCode.ERROR,e.getMessage());
    }
}
