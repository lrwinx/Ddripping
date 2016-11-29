package com.tasly.core.exception;

/**
 * Created by dulei on 11/29/16.
 * Email:dreamfly@126.com
 * Site:http://lrwinx.github.io/
 * Github:https://github.com/lrwinx
 * Author:Lrwin
 * Description: 拒绝策略不支持这样的设置 异常
 */
public class RejectNotSupportedException extends RuntimeException{
    public RejectNotSupportedException(String message) {
        super(message);
    }
}
