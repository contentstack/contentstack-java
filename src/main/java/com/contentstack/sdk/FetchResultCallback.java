package com.contentstack.sdk;


public abstract class FetchResultCallback implements ResultCallBack {

    public abstract void onCompletion(ResponseType responseType, Error error);

    void onRequestFinish(ResponseType responseType) {
        onCompletion(responseType, null);
    }

    @Override
    public void onRequestFail(ResponseType responseType, Error error) {
        onCompletion(responseType, error);
    }


}
