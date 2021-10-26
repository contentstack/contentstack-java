package com.contentstack.sdk;


public interface ResultCallBack {
    void onRequestFail(ResponseType responseType, Error error);
}