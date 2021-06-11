package com.mingz.framework.mvp.presenter.impl;

import java.io.IOException;

public interface MvpMainPresenterImpl {
    void sendRequest(String url);

    void requestTimeout();

    void requestSuccess(String result);

    void requestFail(IOException e);

    void requestException();
}
