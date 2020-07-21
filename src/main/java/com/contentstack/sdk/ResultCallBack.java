package com.contentstack.sdk;

public abstract class ResultCallBack {

    abstract void onRequestFail(ResponseType responseType, Error error);
    public abstract void always();
}
