package com.contentstack.sdk;

/**
 * @author  Contentstack.com
 *
 */
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
    void always() { }
}
