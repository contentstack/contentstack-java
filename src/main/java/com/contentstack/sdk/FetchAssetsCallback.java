package com.contentstack.sdk;

import java.util.List;


public abstract class FetchAssetsCallback implements ResultCallBack {


    public abstract void onCompletion(ResponseType responseType, List<Asset> assets, Error error);

    public void onRequestFinish(ResponseType responseType, List<Asset> assets) {
        onCompletion(responseType, assets, null);
    }

    @Override
    public void onRequestFail(ResponseType responseType, Error error) {
        onCompletion(responseType, null, error);
    }

}
