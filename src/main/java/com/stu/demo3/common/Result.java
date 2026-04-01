package com.stu.demo3.common;
import lombok.Data;

@Data
public class Result<T> {
    private String msg;
    private Integer code;
    private T data;

    // 静态工厂方法：成功回调
    public static <T> Result<T> success(T data){
        Result<T> result = new Result<>();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMsg(ResultCode.SUCCESS.getMsg());
        result.setData(data);
        return result;
    }

    // 静态工厂方法：失败回调
    public static <T> Result<T> error(ResultCode resultCode){
        Result<T> result = new Result<>();
        result.setCode(resultCode.getCode());
        result.setMsg(resultCode.getMsg());
        result.setData(null); // 失败时无数据
        return result;
    }

    // Getter和Setter方法
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
