package com.vweinert.fedditbackend.exception.custom;

public class PostNotFoundException extends Exception{
    public PostNotFoundException() {

    }
    public PostNotFoundException(String message){
        super(message);
    }
    public PostNotFoundException(Throwable cause) {
        super(cause);
    }
    public PostNotFoundException(String message, Throwable cause) {
        super(message,cause);
    }
}
