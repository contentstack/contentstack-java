package com.contentstack.sdk;

public abstract class ContentstackResultCallback extends  ResultCallBack {
    public abstract void onCompletion(ResponseType responseType, Error error);
    public void onRequestFinish(ResponseType responseType){
        onCompletion(responseType, null);
    }
    @Override
    void onRequestFail(ResponseType responseType, Error error) {
        onCompletion(responseType, error);
    }
    @Override
    public void always() { }
}
