package com.contentstack.sdk;

/**
 * @author Contentstack.com
 *
 */
public abstract class ResultCallBack {

    abstract void onRequestFail(ResponseType responseType, Error error);
    abstract void always();
}
