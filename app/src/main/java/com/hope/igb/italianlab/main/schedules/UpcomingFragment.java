package com.hope.igb.italianlab.main.schedules;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.hope.igb.italianlab.R;
import com.hope.igb.italianlab.comman.GeneralMethods;
import com.hope.igb.italianlab.comman.PopupWindowHelper;
import com.hope.igb.italianlab.comman.textshelper.ToastHelper;
import com.hope.igb.italianlab.networking.firebase.MyFirebaseBuilderImpl;
import com.hope.igb.italianlab.networking.models.Reservation;

import java.util.ArrayList;

public class UpcomingFragment extends Fragment implements UpcomingRecyclerAdapter.UpcomingListener,
        MyFirebaseBuilderImpl.ReservingCommandListener, PopupWindowHelper.PopupWindowListener {

    private final MyFirebaseBuilderImpl firebaseBuilder;
    private RecyclerView recyclerView;
    private PopupWindowHelper windowHelper;
    private ToastHelper toastHelper;
    private final int screenHeight, screenWidth;


    public UpcomingFragment(MyFirebaseBuilderImpl firebaseBuilder, int screenHeight, int screenWidth) {
        this.firebaseBuilder = firebaseBuilder;
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.schedules_adapter, container, false);

        recyclerView = viewRoot.findViewById(R.id.schedulesRecyclerView);
        toastHelper = new ToastHelper(requireActivity());


        return viewRoot;
    }



    private void bindView(){

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemViewCacheSize(21);


        UpcomingRecyclerAdapter adapter = new UpcomingRecyclerAdapter(getContext(), null, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

       firebaseBuilder.fetchReservations("Upcoming");
    }





    @Override
    public void onRescheduleClickedListener(Reservation current_reservation) {

         windowHelper = new PopupWindowHelper(requireActivity(), R.layout.reserve_window_layout,R.style.WindowAnimation,
                this);
        windowHelper.displayWindow(screenHeight * 0.85, screenWidth * 0.85,
                "Upcoming", current_reservation);
    }

    @Override
    public void onCancelClickedListener(String reservation_id) {
        firebaseBuilder.cancelReservation(reservation_id);
    }

    @Override
    public void onStart() {
        super.onStart();
        bindView();
        firebaseBuilder.setListener(this);

    }

    @Override
    public void onStop() {
        super.onStop();
        firebaseBuilder.setListener(null);
    }

    @Override
    public void onReservationsFetched(ArrayList<Reservation> reservations) {
        UpcomingRecyclerAdapter adapter = new UpcomingRecyclerAdapter(getContext(), reservations, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private String editToString(EditText editText){
        return editText.getText().toString();
    }



    @Override
    public void onPopupViewRootCreated(View windowRoot, String view_type, Object displayed_object) {


        Reservation reservation = (Reservation) displayed_object;

        EditText phone_number = windowRoot.findViewById(R.id.reserveWindow_edit_phoneNumber);
        phone_number.setText(reservation.getPatient_phone_number());
        phone_number.setFocusable(false);


        EditText name = windowRoot.findViewById(R.id.reserveWindow_edit_name);
        name.setText(reservation.getPatient_name());
        name.setFocusable(false);

        EditText address = windowRoot.findViewById(R.id.reserveWindow_edit_address);
        address.setText(reservation.getAddress());
        address.setFocusable(false);

        EditText patient_status = windowRoot.findViewById(R.id.reserveWindow_edit_status);
        patient_status.setText(reservation.getPatient_status());
        patient_status.setFocusable(false);





        EditText day = windowRoot.findViewById(R.id.reserveWindow_edit_date_day);
        day.setHint(GeneralMethods.millisDateToString(reservation.getDate(),"Day"));

        EditText month = windowRoot.findViewById(R.id.reserveWindow_edit_date_month);
        month.setHint(GeneralMethods.millisDateToString(reservation.getDate(),"Month"));

        EditText year = windowRoot.findViewById(R.id.reserveWindow_edit_date_year);
        year.setHint(GeneralMethods.millisDateToString(reservation.getDate(),"Year"));

        EditText hour = windowRoot.findViewById(R.id.reserveWindow_edit_hours);
        hour.setHint(GeneralMethods.millisDateToString(reservation.getDate(),"Hours"));

        EditText minutes = windowRoot.findViewById(R.id.reserveWindow_edit_minutes);
        minutes.setHint(GeneralMethods.millisDateToString(reservation.getDate(),"Minutes"));

        EditText dayTime = windowRoot.findViewById(R.id.reserveWindow_edit_daytime);
        dayTime.setHint(GeneralMethods.millisDateToString(reservation.getDate(),"DayTime"));



        TextView reserve = windowRoot.findViewById(R.id.reserveWindow_textB_reserve);
        reserve.setText(R.string.reschedule);


        reserve.setOnClickListener(v -> {


             if (Integer.parseInt(editToString(day)) > 31 || Integer.parseInt(editToString(day)) <= 0)
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



                firebaseBuilder.rescheduleReservation(reservation.getReservation_id(),
                        GeneralMethods.stringDateToMillis(editToString(day)+"/"+editToString(month)+"/"+editToString(year) //date
                                +", "+editToString(hour)+":"+editToString(minutes)+" "+editToString(dayTime))); //time

                 windowHelper.dismissWindow();
            }




        });


    }





}
