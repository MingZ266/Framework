package com.mingz.framework.noframework;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mingz.framework.R;
import com.mingz.framework.utils.Tools;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class NoMainActivity extends AppCompatActivity {
    private final AppCompatActivity activity = this;
    private InputMethodManager inputMethodManager;
    private AlertDialog waitDialog;

    private EditText inputLink;
    private Button sendRequest;
    private TextView resultTitle;
    private View resultMessageLayout;
    private TextView resultMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_main);
        Tools.setBlackWordOnStatus(activity);

        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        initView();
        myListener();
    }

    private void initView() {
        inputLink = findViewById(R.id.inputLink);
        sendRequest = findViewById(R.id.sendRequest);
        resultTitle = findViewById(R.id.resultTitle);
        resultMessageLayout = findViewById(R.id.resultMessageLayout);
        resultMessage = findViewById(R.id.resultMessage);
    }

    private void myListener() {
        sendRequest.setOnClickListener(v -> {
            inputMethodManager.hideSoftInputFromWindow(inputLink.getWindowToken(), 0);
            String url = String.valueOf(inputLink.getText());
            if (TextUtils.isEmpty(url)) {
                showToast("输入不能为空");
            } else {
                // 展示等待弹窗
                View waitView = View.inflate(activity, R.layout.dialog_wait, null);
                waitDialog = new AlertDialog.Builder(activity, R.style.CircleCornerAlertDialog)
                        .setView(waitView)
                        .create();
                waitDialog.setCancelable(false);
                waitDialog.setCanceledOnTouchOutside(false);
                waitView.findViewById(R.id.wait).setAnimation(AnimationUtils.loadAnimation(activity, R.anim.rotate_wait));
                waitDialog.show();
                // 隐藏结果区域
                resultTitle.setVisibility(View.INVISIBLE);
                resultMessageLayout.setVisibility(View.INVISIBLE);
                // 网络请求
                sendRequest(url);
            }
        });
    }

    public void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(activity, message, Toast.LENGTH_SHORT).show());
    }

    public void dismissWaitDialog() {
        if (waitDialog != null) {
            waitDialog.dismiss();
            waitDialog = null;
        }
    }

    private void sendRequest(String url) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                if (e instanceof SocketTimeoutException) {
                    showToast("连接超时");
                    dismissWaitDialog();
                } else {
                    showResult("连接失败：", e.getClass().getSimpleName() + ": " + e.getMessage());
                }
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                ResponseBody body = response.body();
                if (body != null) {
                    showResult("请求结果：", body.string());
                } else {
                    showToast("请求异常");
                    dismissWaitDialog();
                }
            }

            private void showResult(String title, String message) {
                runOnUiThread(() -> {
                    resultTitle.setVisibility(View.VISIBLE);
                    resultMessageLayout.setVisibility(View.VISIBLE);
                    resultTitle.setText(title);
                    resultMessage.setText(message);
                    dismissWaitDialog();
                });
            }
        });
    }
}