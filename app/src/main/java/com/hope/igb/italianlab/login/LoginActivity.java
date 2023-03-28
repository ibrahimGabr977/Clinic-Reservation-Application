package com.hope.igb.italianlab.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.hope.igb.italianlab.BaseActivity;
import com.hope.igb.italianlab.R;
import com.hope.igb.italianlab.comman.SharedData;
import com.hope.igb.italianlab.comman.textshelper.EditTextTypingHelper;
import com.hope.igb.italianlab.networking.firebase.MyFirebaseBuilderImpl;
import com.hope.igb.italianlab.main.MainActivity;


import java.sql.Timestamp;


public class LoginActivity  extends BaseActivity implements
        MyFirebaseBuilderImpl.AuthCommandListener,
        LoginPhoneViewMvc.Listener,
        LoginCodeViewMvc.Listener,
        LoginCreateAccountViewMvc.Listener {

    //#res/drawable folder will be organized by names
    //like a-b-..etc(application basic navigators orders)_container(activity, fragment or so on)_named/bg (d drawable, bg background)



    private MyFirebaseBuilderImpl firebaseBuilder;
    private SharedData sharedData;
    private EditTextTypingHelper typingHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);




         sharedData = new SharedData(this);

        firebaseBuilder = new MyFirebaseBuilderImpl(this);
        firebaseBuilder.setListener(this);
        firebaseBuilder.initializeSignIn(sharedData);
        typingHelper = new EditTextTypingHelper();





//        if (!sharedData.getName().equals("IncompleteLogin")){
//            //the user(doctor) verified phone number and provided his data (name and specialization)
//
//            onSignedInCompletedListener();
//
//        }else if (sharedData.getCurrentDoctorId().equals("NewDoctor")){
//
//            LoginPhoneViewMvc phoneViewMvc = new LoginPhoneViewMvc(LayoutInflater.from(this), null, typingHelper);
//
//            setContentView(phoneViewMvc.getRootView());
//
//            phoneViewMvc.registerListener(this);
//
//        }else {
//            //the user(doctor) verified phone number but didn't provide his data (name and specialization)
//
//            onNewUserVerifiedCompletedListener();
//
//        }








    }


    @Override
    public void onSignedInClickListener(String phone_number) {
        //*a user without any saved data clicked signIn button
        //sending code to the provided phone number
//        firebaseBuilder.verifyPhoneNumber(phone_number);
//
//        //save screen height and width to shared preferences
//        sharedData.setScreenDimension(this.getWindow().getDecorView().getHeight(),
//                this.getWindow().getDecorView().getWidth());

        onSignedInCompletedListener();
    }





    @Override
    public void onCodeSentListener(String phone_number, String code) {

        //the code already sent to the provided phone number
        //set view to code layout

        LoginCodeViewMvc codeViewMvc = new LoginCodeViewMvc(LayoutInflater.from(this), null, typingHelper,
                phone_number, code);


        setContentView(codeViewMvc.getRootView());

        codeViewMvc.registerListener(this);

    }


    @Override
    public void onVerifyClickListener(String code, String entered_code) {

        //login or open create account screen in case of new users
        firebaseBuilder.signIn(firebaseBuilder.getPhoneAuthCredentials(code, entered_code));
    }









    @Override
    public void onNewUserVerifiedCompletedListener() {

        LoginCreateAccountViewMvc createAccountViewMvc = new LoginCreateAccountViewMvc(LayoutInflater.from(this),
                null, typingHelper);


        setContentView(createAccountViewMvc.getRootView());


        createAccountViewMvc.registerListener(this);

    }



    @Override
    public void onCreateAccountClickListener(String name, String specialization) {

        firebaseBuilder.createDoctorAccount(name, specialization);


    }



    @Override
    public void onSignedInCompletedListener() {

        //the last method called on login process
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);





    }


    public void onSignInClicked(View view) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
    }
}