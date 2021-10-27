package com.contentstack.sdk;

public abstract class QueryResultsCallBack implements ResultCallBack {

    public abstract void onCompletion(ResponseType responseType, QueryResult queryresult, Error error);

    void onRequestFinish(ResponseType responseType, QueryResult queryResultObject) {
        onCompletion(responseType, queryResultObject, null);
    }

    @Override
    public void onRequestFail(ResponseType responseType, Error error) {
        onCompletion(responseType, null, error);
    }

}
