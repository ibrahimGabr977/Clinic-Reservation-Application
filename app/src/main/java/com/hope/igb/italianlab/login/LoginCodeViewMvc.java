package com.hope.igb.italianlab.login;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.hope.igb.italianlab.R;
import com.hope.igb.italianlab.comman.textshelper.EditTextTypingHelper;

import java.util.ArrayList;

public class LoginCodeViewMvc {

    private final View rootView;
    private final ArrayList<Listener> listeners = new ArrayList<>(1);


    interface Listener{
        void onVerifyClickListener(String code, String entered_code);
    }





    public LoginCodeViewMvc(LayoutInflater inflater, ViewGroup parent, EditTextTypingHelper typingHelper,
                            String phone_number, String code) {

        rootView = inflater.inflate(R.layout.activity_login_verification, parent, false);

       // setContentView(R.layout.activity_login_verification);
        EditText edit_code = findViewById(R.id.loginCode_edit_code);
        TextView error_message = findViewById(R.id.loginCode_text_error);
        TextView enter_code = findViewById(R.id.loginCode_text_enterCode);
        TextView verify_number = findViewById(R.id.loginCode_textB_verify);


        enter_code.setText(String.format("%s%s", enter_code.getText(), phone_number));





        edit_code.setVisibility(View.VISIBLE);
        typingHelper.onStartTyping(edit_code, R.drawable.a_login_coded, 6);




        verify_number.setOnClickListener(null);

        verify_number.setOnClickListener(v -> {


            if (!typingHelper.checkCompleteLoginRequirements(edit_code.getText())){
                error_message.setVisibility(View.VISIBLE);
                error_message.setText(R.string.login_invalid_code);
            }else {

                for (Listener listener: listeners)
                listener.onVerifyClickListener(code, edit_code.getText().toString());

                error_message.setText(null);
                error_message.setVisibility(View.GONE);
            }

        });


//
//        change_number.setOnClickListener(v -> {
//
//            //back view to it's main view to repeat the whole sign in process
//            edit_code.setVisibility(View.GONE);
//            change_number.setOnClickListener(null);
//            change_number.setVisibility(View.GONE);
//            sign_in.setText(R.string.login_signin);
//            edit_phone_number.setEnabled(true);
//
//
//
//            sign_in.setOnClickListener(null);
//            sign_in.setOnClickListener(v1 -> login());
//
//        });


    }

    private <T extends View> T findViewById(int view_id) {
        return getRootView().findViewById(view_id);
    }

    public View getRootView() {
        return rootView;
    }


    public void registerListener(Listener listener){
        listeners.add(listener);
    }

    public void unRegisterListener(Listener listener){
        listeners.remove(listener);
    }

}
