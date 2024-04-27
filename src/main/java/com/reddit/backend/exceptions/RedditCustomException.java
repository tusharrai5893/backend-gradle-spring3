package com.reddit.backend.exceptions;

public class RedditCustomException extends RuntimeException {
    public RedditCustomException(String msg) {
        super(msg);
    }
}
