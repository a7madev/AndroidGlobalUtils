package me.a7madev.androidglobalutils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class CustomAppCompatActivity extends AppCompatActivity implements AsyncResponse {

    public static String TAG = CustomAppCompatActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void processFinish(String requestType, String output, String messageToDisplay, Object extraDetails) {
        // do nothing
    }
}
