package com.hope.igb.italianlab.comman;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedData {

    private final SharedPreferences currentDoctorInfo;



    public SharedData(Context context) {

        currentDoctorInfo = context.getApplicationContext().getSharedPreferences("doctorPref",0);
    }


    public  void saveNewDoctorData(String current_doctor_id, String phone_number, String name,
                                 String specialization){

        //shared preferences put data

        SharedPreferences.Editor editor=currentDoctorInfo.edit();
        editor.putString("currentDoctorId",current_doctor_id);
        editor.putString("phoneNumber",phone_number);
        editor.putString("name",name);
        editor.putString("specialization",specialization);
        editor.apply();

    }


    public  String getCurrentDoctorId(){

        //get current doctor id return "NewDoctor" if not exist (defValue)
        return currentDoctorInfo.getString("currentDoctorId","NewDoctor");

    }

    public  String getPhoneNumber(){

        //get current doctor id return "NewDoctor" if not exist (defValue)
        return currentDoctorInfo.getString("phoneNumber","NewDoctor");

    }




    public  String getName(){

        // return current doctor name
        return currentDoctorInfo.getString("name","IncompleteLogin");

    }


    public  String getSpecialization(){
        // return current doctor specialization
        return currentDoctorInfo.getString("specialization","IncompleteLogin");
    }

    public void editPreferences(String key, String new_value){
        SharedPreferences.Editor editor=currentDoctorInfo.edit();
        editor.putString(""+key, new_value);
        editor.apply();
    }


    public void setScreenDimension(int screenHeight, int ScreenWidth){
        SharedPreferences.Editor editor=currentDoctorInfo.edit();
        editor.putInt("screenHeight", screenHeight);
        editor.putInt("screenWidth", ScreenWidth);
        editor.apply();
    }

    public int getHeight(){
        return currentDoctorInfo.getInt("screenHeight", 0);
    }

    public int getWidth(){
        return currentDoctorInfo.getInt("screenWidth", 0);
    }








}
