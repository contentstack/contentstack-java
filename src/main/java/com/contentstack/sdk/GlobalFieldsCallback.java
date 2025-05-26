package com.contentstack.sdk;

/**
 * The callback for Content Types that contains GlobalFieldsModel and Error
 */
public abstract class GlobalFieldsCallback implements ResultCallBack {

    public abstract void onCompletion(GlobalFieldsModel globalFieldsModel, Error error);

    void onRequestFinish(GlobalFieldsModel globalFieldsModel) {
        onCompletion(globalFieldsModel, null);
    }

    @Override
    public void onRequestFail(ResponseType responseType, Error error) {
        onCompletion(null, error);
    }
}
