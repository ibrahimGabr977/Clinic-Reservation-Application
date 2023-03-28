package com.hope.igb.italianlab.login;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.hope.igb.italianlab.R;
import com.hope.igb.italianlab.comman.textshelper.EditTextTypingHelper;

import java.util.ArrayList;

public class LoginCreateAccountViewMvc {

    private final View rootView;
    private final ArrayList<Listener> listeners = new ArrayList<>(1);

    interface Listener{
        void onCreateAccountClickListener(String name, String specialization);
    }


    public LoginCreateAccountViewMvc(LayoutInflater inflater, ViewGroup parent, EditTextTypingHelper typingHelper) {

        rootView = inflater.inflate(R.layout.activity_login_create_account, parent, false);



        EditText edit_name = findViewById(R.id.loginCreate_edit_name);
        typingHelper.onStartTyping(edit_name, R.drawable.a_login_named, 5);



        EditText edit_specialization= findViewById(R.id.loginCreate_edit_specialization);

        TextView create_account = findViewById(R.id.loginCreate_textB_create);
        TextView error_message = findViewById(R.id.loginCreate_text_error);



        typingHelper.onStartTyping(edit_specialization, R.drawable.a_login_specializationd, 5);





        create_account.setOnClickListener(v -> {



            if (!typingHelper.checkCompleteLoginRequirements(edit_name.getText()) ||
                    !typingHelper.checkCompleteLoginRequirements(edit_specialization.getText())){


                error_message.setVisibility(View.VISIBLE);
                error_message.setText(R.string.login_empty_fields);


            } else {



                error_message.setText(null);
                error_message.setVisibility(View.GONE);


                for (Listener listener : listeners)
                    listener.onCreateAccountClickListener(edit_name.getText().toString(), edit_specialization.getText().toString());


            }

        });



    }




    public View getRootView() {
        return rootView;
    }


    private <T extends View>T findViewById(int view_id) {
        return getRootView().findViewById(view_id);
    }






    public void registerListener(Listener listener){
        listeners.add(listener);
    }

    public void unRegisterListener(Listener listener){
        listeners.remove(listener);
    }


}
