package com.ysf.ipadress.security;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Result<T>{


    private EResult result;
    private T data;
    private String message;

    public Result(EResult eResult, T data) {
        this.result = eResult;
        this.data = data;
    }

    public Result(EResult eResult, T data, String message) {
        this.result = eResult;
        this.data = data;
        this.message = message;
    }

    public Result(EResult eResult,String message, T data) {
        this.result = eResult;
        this.data = data;
        this.message = message;
    }

    public Result(EResult eResult, String message) {
        this.result = eResult;
        this.message = message;
    }
}
