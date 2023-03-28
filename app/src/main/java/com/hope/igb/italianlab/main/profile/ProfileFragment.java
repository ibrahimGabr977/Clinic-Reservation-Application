package com.hope.igb.italianlab.main.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hope.igb.italianlab.R;
import com.hope.igb.italianlab.comman.GeneralMethods;
import com.hope.igb.italianlab.comman.SharedData;
import com.hope.igb.italianlab.comman.textshelper.EditTextTypingHelper;
import com.hope.igb.italianlab.comman.PopupWindowHelper;
import com.hope.igb.italianlab.comman.textshelper.ToastHelper;
import com.hope.igb.italianlab.networking.firebase.MyFirebaseBuilderImpl;
import com.hope.igb.italianlab.networking.models.Reservation;

public class ProfileFragment extends Fragment implements MyFirebaseBuilderImpl.ProfileListener,
        EditTextTypingHelper.EditTextSaveDrawableClickListener , PopupWindowHelper.PopupWindowListener {

    private MyFirebaseBuilderImpl firebaseBuilder;
    private final SharedData sharedData;
    private ImageView add_clinic_reservation, add_home_reservation;
    private TextView profile_phone_number, profile_logout;
    private EditText profile_name, profile_specialization, add_doctor;
    private EditTextTypingHelper typingHelper;
    private ToastHelper toastHelper;
    private final int screenHeight, screenWidth;
    private  PopupWindowHelper windowHelper;


    public ProfileFragment(SharedData sharedData, int screenHeight, int screenWidth) {
        this.sharedData = sharedData;
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.profile_fragment, container, false);


        toastHelper = new ToastHelper(requireActivity());
        firebaseBuilder = new MyFirebaseBuilderImpl(requireActivity());
        typingHelper = new EditTextTypingHelper();





        profile_phone_number = viewRoot.findViewById(R.id.profile_text_phoneNumber);
//        profile_phone_number.setText(sharedData.getPhoneNumber());
        profile_phone_number.setText("+20 XXXXX-XXXXX");


        profile_name = viewRoot.findViewById(R.id.profile_edit_name);
        profile_name.setText(sharedData.getName());
        typingHelper.editTextEditDrawable(profile_name);

        profile_specialization = viewRoot.findViewById(R.id.profile_edit_specialization);
        profile_specialization.setText(sharedData.getSpecialization());
        typingHelper.editTextEditDrawable(profile_specialization);

        add_doctor =viewRoot.findViewById(R.id.profile_edit_addDoctor);
        typingHelper.editTextEditDrawable(add_doctor);





        add_clinic_reservation = viewRoot.findViewById(R.id.profile_imageB_clinicVisit);



        add_home_reservation = viewRoot.findViewById(R.id.profile_imageB_homeVisit);





        return viewRoot;
    }







//
//    private void addRequest(){
//
//        Request request = new Request(""+System.currentTimeMillis(),
//                editToString(name), editToString(phone_number), editToString(address));
//
//
//        firebaseBuilder.addRequest(request);
//
//    }


    private String editToString(EditText editText){
        return editText.getText().toString();
    }



    @Override
    public void onStart() {
        super.onStart();

        typingHelper.setListener(this);

         windowHelper = new PopupWindowHelper(requireActivity(), R.layout.reserve_window_layout,R.style.WindowAnimation,
                this);
        add_clinic_reservation.setOnClickListener(v -> windowHelper.displayWindow(screenHeight * 0.85, screenWidth *0.85,
                "Clinic", null));
        add_home_reservation.setOnClickListener(v -> windowHelper.displayWindow(screenHeight * 0.85, screenWidth * 0.85,
                "Home", null));
    }

    @Override
    public void onStop() {
        super.onStop();
        typingHelper.setListener(null);
        if (windowHelper != null){
            windowHelper.dismissWindow();
            windowHelper = null;
        }

        add_clinic_reservation.setOnClickListener(null);
        add_home_reservation.setOnClickListener(null);
    }



    @Override
    public void onDrawableSaveClickListener(EditText editText) {

        if (editText.getId() == R.id.profile_edit_name){
            firebaseBuilder.updateChildValue("name", editToString(editText));
            profile_name.setFocusable(false);
        }else if (editText.getId() == R.id.profile_edit_specialization){
            firebaseBuilder.updateChildValue("specialization", editToString(editText));
            profile_name.setFocusable(false);
        }else {
            firebaseBuilder.addPermittedDoctor(editToString(add_doctor));
            add_doctor.setFocusable(false);
            add_doctor.setText(null);

        }


    }

    @Override
    public void onUpdateChildListener(String target_child, String new_value) {
        sharedData.editPreferences(target_child, new_value);

        profile_name.setText(sharedData.getName());
        profile_specialization.setText(sharedData.getSpecialization());

    }


    //displayed_image here = null(unused) because here no previous assumption a totally new object (reservation)
    @Override
    public void onPopupViewRootCreated(View windowRoot, String view_type, Object displayed_object) {

       EditText phone_number = windowRoot.findViewById(R.id.reserveWindow_edit_phoneNumber);
       typingHelper.onStartTypingPhoneNumber(phone_number);

       EditText name = windowRoot.findViewById(R.id.reserveWindow_edit_name);

       EditText address = windowRoot.findViewById(R.id.reserveWindow_edit_address);
       if (view_type.equals("Clinic")){
           address.setText(getResources().getString(R.string.clinic_visit));
           address.setFocusable(false);
       }

       EditText day = windowRoot.findViewById(R.id.reserveWindow_edit_date_day);
       EditText month = windowRoot.findViewById(R.id.reserveWindow_edit_date_month);
       EditText year = windowRoot.findViewById(R.id.reserveWindow_edit_date_year);
       EditText hour = windowRoot.findViewById(R.id.reserveWindow_edit_hours);
       EditText minutes = windowRoot.findViewById(R.id.reserveWindow_edit_minutes);
       EditText dayTime = windowRoot.findViewById(R.id.reserveWindow_edit_daytime);
       EditText patient_status = windowRoot.findViewById(R.id.reserveWindow_edit_status);

       TextView reserve = windowRoot.findViewById(R.id.reserveWindow_textB_reserve);


       reserve.setOnClickListener(v -> {

           if (!typingHelper.checkValidPhoneNumber(phone_number.getText()))
               toastHelper.showFailureMessage("invalid phone number");

           else if (!typingHelper.checkCompleteLoginRequirements(name.getText()) ||
                   !typingHelper.checkCompleteLoginRequirements(address.getText()) ||
                   !typingHelper.checkCompleteLoginRequirements(patient_status.getText()))

               toastHelper.showFailureMessage("incomplete information, please fill all fields to reserve ");

           else if (Integer.parseInt(editToString(day)) > 31 || Integer.parseInt(editToString(day)) <= 0)
               toastHelper.showFailureMessage("invalid day, day must be from 1 to 31");

           else if (Integer.parseInt(editToString(month)) > 12 || Integer.parseInt(editToString(month)) <= 0)
               toastHelper.showFailureMessage("invalid month, month must be from 1 to 12");


           else if (Integer.parseInt(editToString(year)) > 2050 || Integer.parseInt(editToString(year)) < 2021)
               toastHelper.showFailureMessage("invalid year, year must be start from 2021");


           else if (Integer.parseInt(editToString(hour)) > 12 || Integer.parseInt(editToString(hour)) <= 0)
               toastHelper.showFailureMessage("invalid hour, hour must be from 1 to 12");


           else if (Integer.parseInt(editToString(minutes)) > 59 || Integer.parseInt(editToString(minutes)) <= 0)
               toastHelper.showFailureMessage("invalid minute, minutes must be from 1 to 59");


           else if (!editToString(dayTime).equals("PM") || !editToString(dayTime).equals("AM"))
               toastHelper.showFailureMessage("invalid dayTime, dayTime must PM or AM");


           else {
                       Reservation reservation = new Reservation(""+System.currentTimeMillis(),
            editToString(name), editToString(phone_number), editToString(address),

            GeneralMethods.stringDateToMillis(editToString(day)+"/"+editToString(month)+"/"+editToString(year) //date
                    +", "+editToString(hour)+":"+editToString(minutes)+" "+editToString(dayTime)), //time

            editToString(patient_status));


              firebaseBuilder.reserve(reservation);
              windowHelper.dismissWindow();
           }




       });


    }

}
