package com.hope.igb.italianlab.comman.textshelper;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.hope.igb.italianlab.R;

//-- this class to handle start typing to put the end drawable

public class EditTextTypingHelper {


    private EditTextSaveDrawableClickListener listener;





    public void onStartTypingPhoneNumber(EditText editText){

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().startsWith("+2 "))
                    s.insert(0, "+2 ");


                if (checkValidPhoneNumber(s))
                    editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.a_login_phoned,
                            0,R.drawable.a_login_correctd,  0);
                else
                    editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.a_login_phoned,
                            0,0,  0);


            }
        });
    }






    public void onStartTyping(EditText editText, int drawable_res, int required_length){

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


                if (checkCompleteLoginRequirements(s) && s.toString().trim().length() >= required_length)
                    editText.setCompoundDrawablesWithIntrinsicBounds(drawable_res,
                            0,R.drawable.a_login_correctd,  0);
                else
                    editText.setCompoundDrawablesWithIntrinsicBounds(drawable_res,
                            0, 0,  0);


            }
        });
    }



    public boolean checkValidPhoneNumber(Editable editable){
        return editable.toString().startsWith("+2 01") && editable.toString().length() == 14;
    }


    public boolean checkCompleteLoginRequirements(Editable editable){
        return editable !=  null &&
                editable.toString().trim().length() != 0;
    }




    public interface EditTextSaveDrawableClickListener{
        void onDrawableSaveClickListener(EditText editText);
    }

    public void setListener(EditTextSaveDrawableClickListener listener) {
        this.listener = listener;
    }

    @SuppressLint("ClickableViewAccessibility")
    public void editTextEditDrawable(EditText editText){

        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if( editText.getCompoundDrawables()[DRAWABLE_RIGHT] != null &&
                            event.getRawX() >= (editText.getRight() - editText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {


                        //on click edit drawable
                        if (!editText.isFocusable()){
                            editText.setFocusable(true);
                            editText.setCompoundDrawablesWithIntrinsicBounds(0,
                                    0, 0,  0);


                            if (editText.getId() == R.id.profile_edit_name)
                                onStartTyping(editText, R.drawable.b_main_profile_save_editedd, 6);
                            else if (editText.getId() == R.id.profile_edit_specialization)
                                onStartTyping(editText, R.drawable.b_main_profile_save_editedd, 4);
                            else
                                onStartTypingPhoneNumber(editText);



                        }else {
                            //on click save drawable
                            listener.onDrawableSaveClickListener(editText);
                        }



                        return true;
                    }
                }
                return false;
            }

        }


        );
    }








}
