package com.hope.igb.italianlab.login;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.hope.igb.italianlab.R;
import com.hope.igb.italianlab.comman.textshelper.EditTextTypingHelper;

import java.util.ArrayList;

public class LoginPhoneViewMvc {

    private final EditText edit_phone_number;
    private final View rootView;
    private final EditTextTypingHelper typingHelper;



    private final ArrayList<Listener> listeners = new ArrayList<>(1);

    interface Listener{
        void onSignedInClickListener(String phone_number);
    }




    public LoginPhoneViewMvc(LayoutInflater inflater, ViewGroup parent, EditTextTypingHelper typingHelper){
        //totally new user with no saved data

        rootView = inflater.inflate(R.layout.activity_login, parent, false);

        edit_phone_number = findViewById(R.id.login_edit_phone);


        this.typingHelper = typingHelper;
        typingHelper.onStartTypingPhoneNumber(edit_phone_number);




        TextView sign_in = findViewById(R.id.login_textB_signin);


        sign_in.setOnClickListener(v -> {

            login();
        });


    }



    public void registerListener(Listener listener){
        listeners.add(listener);
    }

    public void unRegisterListener(Listener listener){
        listeners.remove(listener);
    }





    private <T extends View> T findViewById(int view_id){
        return getRootView().findViewById(view_id);
    }




    public View getRootView() {
        return rootView;
    }





    private void login(){
        TextView error_text = findViewById(R.id.login_text_error);
        if (!typingHelper.checkCompleteLoginRequirements(edit_phone_number.getText())){

            error_text.setVisibility(View.VISIBLE);
            error_text.setText(R.string.login_empty_phone);

        } else if (!typingHelper.checkValidPhoneNumber(edit_phone_number.getText())){


            error_text.setVisibility(View.VISIBLE);
            error_text.setText(R.string.login_invalid_phone);


        }else {
            error_text.setText(null);
            error_text.setVisibility(View.GONE);


            for (Listener listener: listeners)
            listener.onSignedInClickListener(edit_phone_number.getText().toString().trim());
        }
    }



}
