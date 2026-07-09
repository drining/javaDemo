package org.example.springdemo3.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private Integer code;
    private String msg;
    private T data;

    public static <T> Result<T> success() {
        return new Result<T>(1, "success", null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<T>(1, "success", data);
    }

    public static <T> Result<T> error(String msg) {
        return new Result<T>(0, msg, null);
    }
}