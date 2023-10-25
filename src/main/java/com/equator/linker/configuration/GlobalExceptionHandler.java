package com.equator.linker.configuration;



import com.equator.core.http.model.Response;
import com.equator.core.model.exception.ForbiddenException;
import com.equator.core.model.exception.VerifyException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;



/**
 * 统一异常管理
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public Response<String> ConstraintViolationExceptionExceptionHandler(Exception e) {
        log.error("参数错误：{}", e.getMessage());
        return Response.badRequest(String.format("参数错误%s", e.getMessage()), e.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public Response<String> MissingServletRequestParameterExceptionHandler(Exception e) {
        log.error("参数错误：{}", e.getMessage());
        return Response.badRequest("参数错误", e.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public Response<String> HttpRequestMethodNotSupportedExceptionHandler(Exception e) {
        log.error("请求方法错误：{}", e.getMessage());
        return Response.badRequest("请求方法错误", e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Response<String> MethodArgumentNotValidExceptionExceptionHandler(MethodArgumentNotValidException e) {
        log.error("参数错误 {}", e.getMessage());
        FieldError fieldError = e.getBindingResult().getFieldError();
        return Response.badRequest("参数错误", fieldError == null ? "" :
                fieldError.getField() + ":" + fieldError.getDefaultMessage());
    }

    @ExceptionHandler(VerifyException.class)
    @ResponseBody
    public Response<String> VerifyExceptionHandler(VerifyException e) {
        log.error("程序逻辑错误，拒绝执行 {}", e.getMessage());
        return Response.preconditionFailed(String.format("出错了：%s", e.getMessage()), null);
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseBody
    public Response<String> ForbiddenExceptionHandler(ForbiddenException e) {
        log.error("权限不够，拒绝执行 {}", e.getMessage());
        return Response.forbidden(String.format("权限不够：%s", e.getMessage()), null);
    }

    @ExceptionHandler(BindException.class)
    @ResponseBody
    public Response<String> BindExceptionExceptionHandler(BindException e) {
        log.error("参数错误 {}", e.getMessage());
        FieldError fieldError = e.getBindingResult().getFieldError();
        return Response.badRequest("参数错误", fieldError == null ? "" :
                fieldError.getField() + fieldError.getDefaultMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Response<String> exceptionHandler(Exception e) {
        log.error("服务器错误：{}", e.getMessage(), e);
        return Response.serverError("服务器异常", e.getMessage());
    }
}
