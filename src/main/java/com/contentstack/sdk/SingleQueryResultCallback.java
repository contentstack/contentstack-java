package com.contentstack.sdk;

public abstract class SingleQueryResultCallback extends ResultCallBack {

    public abstract void onCompletion(ResponseType responseType, Entry entry, Error error);

    void onRequestFinish(ResponseType responseType, Entry entry){
        onCompletion(responseType, entry, null);
    }

    @Override
    void onRequestFail(ResponseType responseType, Error error) {
        onCompletion(responseType, null, error);
    }

    @Override
    public void always() {

    }


}

