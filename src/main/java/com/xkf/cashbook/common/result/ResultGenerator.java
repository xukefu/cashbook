package com.xkf.cashbook.common.result;

import lombok.extern.slf4j.Slf4j;

/**
 * 响应结果生成工具
 */
@Slf4j
public class ResultGenerator {

    /**
     * Request result message
     */
    public static final String DEFAULT_SUCCESS_MESSAGE = "success";
    public static final String UNAUTHORIZED_MESSAGE = "unauthorized";
    public static final String DEFAULT_FAIL_MESSAGE = "fail";
    public static final String NO_RESULT_MESSAGE = "no result";

    /**
     * Operation status
     */
    public static final String SUCCESS = "SUCCESS";
    public static final String ERROR = "ERROR";

    /**
     * Error or exception message
     */
    public static final String DB_ERROR_MESSAGE = "Database Error";
    public static final String NETWORK_ERROR_MESSAGE = "网络异常,请重试";
    public static final String SERVER_ERROR_MESSAGE = "Server Error";
    public static final String SERVICE_ERROR_MESSAGE = "服务繁忙，请稍后重试";
    public static final String LOGIN_EXPIRATION_MESSAGE = "登录已过期,请刷新后重新登录";

    /**
     * return response with default success or error message by status
     *
     * @return
     */
    public static Result genSuccessResult() {
        Result result = new Result()
                .setCode(ResultCode.SUCCESS)
                .setMessage(DEFAULT_SUCCESS_MESSAGE);
        return result;
    }

    public static <T> Result<T> genSuccessResult(String message, T data) {
        Result result = new Result()
                .setCode(ResultCode.SUCCESS)
                .setMessage(message)
                .setData(data);
        return  result;
    }

    public static <T> Result<T> genSuccessResult(T data) {
        Result result = new Result()
                .setCode(ResultCode.SUCCESS)
                .setMessage(DEFAULT_SUCCESS_MESSAGE)
                .setData(data);
        return result;
    }

    public static Result genUnAuthorizedResult() {
        Result result = new Result()
                .setCode(ResultCode.UNAUTHORIZED)
                .setMessage(UNAUTHORIZED_MESSAGE);
        return result;
    }

    public static Result genUnAuthorizedResult(String message) {
        return new Result()
                .setCode(ResultCode.UNAUTHORIZED)
                .setMessage(message);
    }

    public static Result genFailResult() {
        Result result = new Result()
                .setCode(ResultCode.FAIL)
                .setMessage(DEFAULT_FAIL_MESSAGE);
        return result;
    }

    public static Result genFailResult(String message) {
        Result result = new Result()
                .setCode(ResultCode.FAIL)
                .setMessage(message);
        return result;
    }

    public static Result genInvalidParamResult(String message) {
        Result result = new Result()
                .setCode(ResultCode.INVALID_PARAM)
                .setMessage(message);
        return result;
    }

    public static Result genNotFoundResult(String message) {
        return new Result()
                .setCode(ResultCode.NOT_FOUND)
                .setMessage(message);
    }

    public static Result genInternalServerErrorResult(String message) {
        return new Result()
                .setCode(ResultCode.INTERNAL_SERVER_ERROR)
                .setMessage(message);
    }

    public static <T> Result<T> genResult(ResultCode code, String message, T data) {
        Result result = new Result()
                .setCode(code)
                .setMessage(message)
                .setData(data);
        return result;
    }

}
