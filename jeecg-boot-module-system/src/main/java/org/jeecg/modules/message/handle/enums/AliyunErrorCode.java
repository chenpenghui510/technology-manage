package org.jeecg.modules.message.handle.enums;

import lombok.Getter;
import lombok.Setter;

public enum AliyunErrorCode {
    OK(1,"请求成功");

    private Integer code;

    private String msg;
    private AliyunErrorCode(Integer code,String msg) {
        this.code = code;
        this.msg=msg;
    }
    public Integer getCode() {
        return code;
    }

    public void setStatusCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setStatusMsg(String msg) {
        this.msg = msg;
    }
}
