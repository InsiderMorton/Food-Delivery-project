package edu.aitu.oop3.shared.models;

public class Result<T> {
    private final T data;
    private final String error;
    private final boolean success;

    private Result(T data, String error, boolean success) {
        this.data = data;
        this.error = error;
        this.success = success;
    }

    public static <T> Result<T> ok(T data) {
        return new Result<>(data, null, true);
    }

    public static <T> Result<T> fail(String error) {
        return new Result<>(null, error, false);
    }

    public boolean isSuccess() { return success; }
    public T getData() { return data; }
    public String getError() { return error; }

    @Override
    public String toString() {
        if (success) {
            return "Result[SUCCESS]: " + data;
        }
        return "Result[ERROR]: " + error;
    }
}
