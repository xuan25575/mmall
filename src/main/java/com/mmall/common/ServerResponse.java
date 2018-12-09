package com.mmall.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

//保证json序列化的时候，如果是null的对象，不会放入key
@JsonSerialize(include =JsonSerialize.Inclusion.NON_NULL)
public class ServerResponse<T> implements Serializable {

    private int status;
    private String msg;
    private T data;

    private ServerResponse(int status){
        this.status=status;
    }
    private ServerResponse(int status,String msg){
        this.status=status;
        this.msg=msg;
    }
    private ServerResponse(int status,String msg,T data){
        this.status=status;
        this.msg=msg;
        this.data = data;
    }
    private ServerResponse(int status,T data){
        this.status=status;
        this.data = data;
    }

    @JsonIgnore
//    使之不在json序列化中
    public boolean isSuccess(){
        return status == ResponseCode.SUCCESS.getCode();
    }

    /* <T>ServerResponse<T>  第一个T标识是一个泛型方法 */
    public static <T>ServerResponse<T> createBySuccess(){
        return  new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
    }

    public static <T>ServerResponse<T> createBySuccessMessage(String msg){
        return  new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg);
    }

    public static <T>ServerResponse<T> createBySuccess(T data){
        return  new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),data);
    }

    public static <T>ServerResponse<T> createBySuccess(String msg ,T data){
        return  new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg,data);
    }
    public static <T>ServerResponse<T> createByError(){
        return  new ServerResponse<T>(ResponseCode.ERROR.getCode(),ResponseCode.ERROR.getDesc());
    }
    public static <T>ServerResponse<T> createByErrorMessage(String msg){
        return  new ServerResponse<T>(ResponseCode.ERROR.getCode(),msg);
    }
    public static <T>ServerResponse<T> createByErrorCodeMessage(int errorCode,String errorMsg){
        return  new ServerResponse<T>(errorCode,errorMsg);
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }
}
