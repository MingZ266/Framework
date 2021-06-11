package com.mingz.framework.mvp.model;

import com.mingz.framework.mvp.model.impl.MvpMainModelImpl;
import com.mingz.framework.mvp.presenter.impl.MvpMainPresenterImpl;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MvpMainModel implements MvpMainModelImpl {
    private final MvpMainPresenterImpl mvpMainPresenter;

    public MvpMainModel(MvpMainPresenterImpl mvpMainPresenter) {
        this.mvpMainPresenter = mvpMainPresenter;
    }

    @Override
    public void sendRequest(HttpUrl url) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                if (e instanceof SocketTimeoutException) {
                    mvpMainPresenter.requestTimeout();
                } else {
                    mvpMainPresenter.requestFail(e);
                }
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                ResponseBody body = response.body();
                if (body != null) {
                    mvpMainPresenter.requestSuccess(body.string());
                } else {
                    mvpMainPresenter.requestException();
                }
            }
        });
    }
}
