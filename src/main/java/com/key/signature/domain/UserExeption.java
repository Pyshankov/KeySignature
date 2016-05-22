package com.key.signature.domain;

/**
 * Created by pyshankov on 22.05.2016.
 */
public class UserExeption extends RuntimeException {

 private   String message;

    public UserExeption(String message) {
        super(message);
    }
}
