package com.xkf.cashbook.common.exception;

import com.xkf.cashbook.common.result.ResultCode;

public class ServiceException extends RuntimeException{

    private static final long serialVersionUID = 4255703327364270835L;
    private int errCode;

    private String errMsg;

    private Throwable causeThrowable;

    public ServiceException() {
    }

    public ServiceException(int errCode, String errMsg) {
        super(errMsg);
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public ServiceException(ResultCode resultCode, String errMsg) {
        super(errMsg);
        this.errCode = resultCode.code();
        this.errMsg = errMsg;
    }

    public ServiceException(int errCode, Throwable throwable) {
        super(throwable);
        this.errCode = errCode;
        setCauseThrowable(throwable);
    }

    public ServiceException(int errCode, String errMsg, Throwable throwable) {
        super(errMsg, throwable);
        this.errCode = errCode;
        this.errMsg = errMsg;
        setCauseThrowable(throwable);
    }

    public void setCauseThrowable(Throwable throwable) {
        this.causeThrowable = getCauseThrowable(throwable);
    }

    private Throwable getCauseThrowable(Throwable t) {
        if (t.getCause() == null) {
            return t;
        }
        return getCauseThrowable(t.getCause());
    }

    public int getErrCode() {
        return errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }
}
