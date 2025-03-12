package com.fmv.healthkiosk.core.utils;

public abstract class Resource<T> {
    public static class Loading<T> extends Resource<T> {}

    public static class Success<T> extends Resource<T> {
        public final T data;
        public Success(T data) { this.data = data; }
    }

    public static class Error<T> extends Resource<T> {
        public final String message;
        public Error(String message) { this.message = message; }
    }
}