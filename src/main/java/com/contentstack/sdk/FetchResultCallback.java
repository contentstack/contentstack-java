package com.contentstack.sdk;


public abstract class FetchResultCallback implements ResultCallBack {

    /**
     * Triggered after call execution complete.
     *
     * @param responseType call response from cache or network.
     * @param error        {@link Error} instance if call failed else null.
     */

    public abstract void onCompletion(ResponseType responseType, Error error);

    void onRequestFinish(ResponseType responseType) {
        onCompletion(responseType, null);
    }

    @Override
    public void onRequestFail(ResponseType responseType, Error error) {
        onCompletion(responseType, error);
    }


}
