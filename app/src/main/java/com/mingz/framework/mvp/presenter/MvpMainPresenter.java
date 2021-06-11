package com.mingz.framework.mvp.presenter;

import android.text.TextUtils;

import com.mingz.framework.mvp.model.MvpMainModel;
import com.mingz.framework.mvp.model.impl.MvpMainModelImpl;
import com.mingz.framework.mvp.presenter.impl.MvpMainPresenterImpl;
import com.mingz.framework.mvp.view.impl.MvpMainViewImpl;
import com.mingz.framework.utils.MyLog;

import java.io.IOException;

public class MvpMainPresenter implements MvpMainPresenterImpl {
    private final MyLog myLog = new MyLog("MvpMainMyTAG");

    private final MvpMainViewImpl mvpMainView;
    private final MvpMainModelImpl mvpMainModel;

    public MvpMainPresenter(MvpMainViewImpl mvpMainView) {
        this.mvpMainView = mvpMainView;
        mvpMainModel = new MvpMainModel(this);
    }

    @Override
    public void sendRequest(String url) {
        if (TextUtils.isEmpty(url)) {
            mvpMainView.showToast("输入不能为空");
        } else {
            mvpMainView.hideResultArea();
            mvpMainView.showWaitDialog();
            mvpMainModel.sendRequest(url);
        }
    }

    @Override
    public void requestTimeout() {
        mvpMainView.showToast("连接超时");
        mvpMainView.dismissWaitDialog();
    }

    @Override
    public void requestSuccess(String result) {
        mvpMainView.showResultArea(result);
        mvpMainView.dismissWaitDialog();
    }

    @Override
    public void requestFail(IOException e) {
        mvpMainView.showResultArea(e);
        myLog.d("连接失败", e);
        mvpMainView.dismissWaitDialog();
    }

    @Override
    public void requestException() {
        mvpMainView.showToast("请求异常");
        mvpMainView.dismissWaitDialog();
    }
}
