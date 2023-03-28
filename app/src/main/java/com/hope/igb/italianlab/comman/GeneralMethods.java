package com.hope.igb.italianlab.comman;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.DateFormat;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class GeneralMethods {


    private final Activity activity;


    public GeneralMethods(Activity activity){

        this.activity = activity;
    }



    public  void switchToFragment(int frameLayout_id, Fragment fragment) {
        //!!!!!
        FragmentManager manager = ((FragmentActivity)activity).getSupportFragmentManager();
//        manager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE); //--to clean the previous fragment
        manager.beginTransaction().replace(frameLayout_id,fragment).commit();


    }




    public  boolean isInternetAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }





    public static String millisDateToString(long timeInMillis, String dateStyle){

        Calendar objectTime = Calendar.getInstance(); //Object (post, comment or ..etc)
        objectTime.setTimeInMillis(timeInMillis); //Server time


        //Full date of object with format like "Wed, Sep 17, 2020 07:02 AM"
        if (dateStyle.equals("Date"))
            return DateFormat.format("dd/MM/yyyy", objectTime).toString();

        else if (dateStyle.equals("Time"))
            return DateFormat.format("KK:mm aa", objectTime).toString();


        else if (dateStyle.equals("Duration"))//duration case
            return DateFormat.format("mm:ss", objectTime).toString();

        else if (dateStyle.equals("Day"))
            return DateFormat.format("dd", objectTime).toString();

        else if (dateStyle.equals("Month"))
            return DateFormat.format("MM", objectTime).toString();

        else if (dateStyle.equals("Year"))
            return DateFormat.format("yyyy", objectTime).toString();

        else if (dateStyle.equals("Hours"))
            return DateFormat.format("KK", objectTime).toString();

        else if (dateStyle.equals("Minutes"))
            return DateFormat.format("mm", objectTime).toString();

        else //DayTime case
            return DateFormat.format("aa", objectTime).toString();
    }


    public static long stringDateToMillis(String string_date){


           SimpleDateFormat formatter= new SimpleDateFormat("dd/MM/yyyy, KK:mm aa");



        Date date = null;
        try {
            date = formatter.parse(string_date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date.getTime();
    }



}
