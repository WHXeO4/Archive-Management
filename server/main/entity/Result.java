package main.entity;

public class Result <T>{
    private int code;
    private String message;
    private T data;

    public Result(int code, String message) {
        this.code = code;
        this.message = message;
        this.data = null;
    }
    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static <T> Result<T> success() {
        return new Result<>(0, "success");
    }
    public static <T> Result<T> success(String msg) {
        return new Result<>(0, msg);
    }
    public static <T> Result<T> success(String msg, T data) {
        return new Result<>(0, msg, data);
    }

    public static Result error(String msg) {
        return new Result(1, msg);
    }

}
