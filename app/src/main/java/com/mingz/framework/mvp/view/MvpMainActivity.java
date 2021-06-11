package com.mingz.framework.mvp.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mingz.framework.R;
import com.mingz.framework.mvp.presenter.MvpMainPresenter;
import com.mingz.framework.mvp.presenter.impl.MvpMainPresenterImpl;
import com.mingz.framework.mvp.view.impl.MvpMainViewImpl;
import com.mingz.framework.utils.Tools;

import java.io.IOException;

public class MvpMainActivity extends AppCompatActivity implements MvpMainViewImpl {
    private final MvpMainPresenterImpl mvpMainPresenter = new MvpMainPresenter(this);
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
        setContentView(R.layout.activity_mvp_main);
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
            mvpMainPresenter.sendRequest(String.valueOf(inputLink.getText()));
        });
    }

    @Override
    public void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(activity, message, Toast.LENGTH_SHORT).show());
    }

    @Override
    public void showResultArea(String result) {
        runOnUiThread(() -> {
            resultTitle.setVisibility(View.VISIBLE);
            resultMessageLayout.setVisibility(View.VISIBLE);
            resultTitle.setText("请求结果：");
            resultMessage.setText(result);
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void showResultArea(IOException e) {
        runOnUiThread(() -> {
            resultTitle.setVisibility(View.VISIBLE);
            resultMessageLayout.setVisibility(View.VISIBLE);
            resultTitle.setText("连接失败：");
            resultMessage.setText(e.getClass().getSimpleName() + ": " + e.getMessage());
        });
    }

    @Override
    public void hideResultArea() {
        runOnUiThread(() -> {
            resultTitle.setVisibility(View.INVISIBLE);
            resultMessageLayout.setVisibility(View.INVISIBLE);
        });
    }

    @Override
    public void showWaitDialog() {
        runOnUiThread(() -> {
            View waitView = View.inflate(activity, R.layout.dialog_wait, null);
            waitDialog = new AlertDialog.Builder(activity, R.style.CircleCornerAlertDialog)
                    .setView(waitView)
                    .create();
            waitDialog.setCancelable(false);
            waitDialog.setCanceledOnTouchOutside(false);
            waitView.findViewById(R.id.wait).setAnimation(AnimationUtils.loadAnimation(activity, R.anim.rotate_wait));
            waitDialog.show();
        });
    }

    @Override
    public void dismissWaitDialog() {
        runOnUiThread(() -> {
            if (waitDialog != null) {
                waitDialog.dismiss();
                waitDialog = null;
            }
        });
    }
}
