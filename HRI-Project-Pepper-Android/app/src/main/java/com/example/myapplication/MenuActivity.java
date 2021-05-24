package com.example.myapplication;

import android.os.Bundle;
import android.widget.ImageButton;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;

public class MenuActivity extends RobotActivity implements RobotLifecycleCallbacks {

    private ImageButton cheeseburgerImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        initOnClick();
    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {

    }

    @Override
    public void onRobotFocusLost() {

    }

    @Override
    public void onRobotFocusRefused(String reason) {

    }

    public void initOnClick() {
        cheeseburgerImageButton = (ImageButton)findViewById(R.id.imagebutton_cheeseburger);
        cheeseburgerImageButton.setOnClickListener(v -> {
            Order.cheeseburger.setNumber(Order.cheeseburger.getNumber() + 1);
        });
    }
}