package com.example.photoapplication.Utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.photoapplication.R;
import com.example.photoapplication.Utils.OnResultCallBack.ConfigureCallBack;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WarningDialog extends Dialog {
    @BindView(R.id.warning_title)
    TextView warningTitle;

    @BindView(R.id.warning_message)
    TextView warningMessage;

    @BindView(R.id.okay_button)
    Button okayBtn;

    @BindView(R.id.cancel_button)
    Button cancelBtn;


    ConfigureCallBack configureCallBack;
    WarningData warningData;

    public WarningDialog(@NonNull Context context, WarningData warningData, ConfigureCallBack configureCallBack) {
        super(context);
        this.warningData = warningData;
        this.configureCallBack = configureCallBack;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.warrning_dialog);
        ButterKnife.bind(this);
        setViewsText();
        setViewsListeners();
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private void setViewsText() {
        warningTitle.setText(warningData.warningTitle);
        warningMessage.setText(warningData.warningMessage);
        okayBtn.setText(warningData.okayButtonText);
        cancelBtn.setText(warningData.cancelButtonText);
    }

    private void setViewsListeners(){
        okayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                configureCallBack.requestConfigure(true);
                dismiss();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                configureCallBack.requestConfigure(false);
                dismiss();
            }
        });
    }
}
