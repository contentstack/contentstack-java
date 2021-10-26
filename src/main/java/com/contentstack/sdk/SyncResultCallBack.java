package com.contentstack.sdk;


public abstract class SyncResultCallBack implements ResultCallBack {

    public abstract void onCompletion(SyncStack syncStack, Error error);

    void onRequestFinish(SyncStack syncStack) {
        onCompletion(syncStack, null);
    }

    @Override
    public void onRequestFail(ResponseType responseType, Error error) {
        onCompletion(null, error);
    }


}
