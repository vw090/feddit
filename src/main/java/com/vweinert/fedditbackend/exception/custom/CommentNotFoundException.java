package com.vweinert.fedditbackend.exception.custom;

public class CommentNotFoundException extends Exception {
    public CommentNotFoundException() {

    }
    public CommentNotFoundException(String message){
        super(message);
    }
    public CommentNotFoundException(Throwable cause) {
        super(cause);
    }
    public CommentNotFoundException(String message, Throwable cause) {
        super(message,cause);
    }
}
