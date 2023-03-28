package com.hope.igb.italianlab.comman.textshelper;

import android.app.Activity;
import android.widget.Toast;

public class ToastHelper {

    private final Activity activity;


    public ToastHelper(Activity activity) {
        this.activity = activity;
    }


    public void showFailureMessage(String message){

        Toast.makeText(activity, "There is something wrong, "+message, Toast.LENGTH_LONG).show();

    }

    public void showSuccessMessage(String message){
        Toast.makeText(activity, "Successfully "+message, Toast.LENGTH_SHORT).show();
    }


    public void showWarningMessage(String message){
        Toast.makeText(activity, "#Give Attention, "+message, Toast.LENGTH_LONG).show();
    }

}
