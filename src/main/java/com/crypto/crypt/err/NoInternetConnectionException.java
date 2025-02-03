package com.crypto.crypt.err;

public class NoInternetConnectionException extends Exception {
    public NoInternetConnectionException() {
        super("No Internet Connection");
    }
}
