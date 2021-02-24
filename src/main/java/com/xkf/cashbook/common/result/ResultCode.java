package com.xkf.cashbook.common.result;

/**
 * 响应码枚举，参考HTTP状态码的语义
 */
public enum ResultCode {
    /**
     * 0 成功
     * 1 失败
     * -2 未认证（签名错误）
     * 404 接口不存在
     * 500 服务器内部错误
     */
    SUCCESS(0),
    FAIL(1),
    INVALID_PARAM(2),
    UNAUTHORIZED(-2),
    NOT_FOUND(404),
    INTERNAL_SERVER_ERROR(500);

    private final int code;

    ResultCode(int code) {
        this.code = code;
    }

    public int code() {
        return code;
    }
}
