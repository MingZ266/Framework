package com.mingz.framework.mvp.view.impl;

import java.io.IOException;

public interface MvpMainViewImpl {
    void showToast(String message);

    void showResultArea(String result);

    void showResultArea(IOException e);

    void hideResultArea();

    void showWaitDialog();

    void dismissWaitDialog();
}
