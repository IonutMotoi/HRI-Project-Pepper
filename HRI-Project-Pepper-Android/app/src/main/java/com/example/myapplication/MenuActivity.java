package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;

public class MenuActivity extends RobotActivity implements RobotLifecycleCallbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        Log.i("MENU", Order.items_list.toString());
    }

    @Override
    public void onRobotFocusLost() {

    }

    @Override
    public void onRobotFocusRefused(String reason) {

    }

    public void AddItem(View view) {
        Item cheeseburger = new Item("Cheeseburger", 1, 2.0f);
        Order.items_list.add(cheeseburger);
        Log.i("MENU", String.valueOf(Order.items_list.elementAt(0).getNumber()));
    }


}