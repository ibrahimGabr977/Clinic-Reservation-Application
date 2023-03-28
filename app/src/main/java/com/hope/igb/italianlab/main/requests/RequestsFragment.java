package com.hope.igb.italianlab.main.requests;


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
import com.hope.igb.italianlab.comman.SharedData;
import com.hope.igb.italianlab.comman.textshelper.ToastHelper;
import com.hope.igb.italianlab.networking.firebase.MyFirebaseBuilderImpl;
import com.hope.igb.italianlab.networking.models.Request;


import java.util.ArrayList;

public class RequestsFragment extends Fragment implements RequestsRecyclerAdapter.RequestsListener,
        MyFirebaseBuilderImpl.RequestingCommandListener, PopupWindowHelper.PopupWindowListener {

    private  MyFirebaseBuilderImpl firebaseBuilder;
    private RecyclerView recyclerView;
    private PopupWindowHelper windowHelper;
    private ToastHelper toastHelper;
    private SharedData sharedData;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.schedules_adapter, container, false);

        recyclerView = viewRoot.findViewById(R.id.schedulesRecyclerView);
        firebaseBuilder = new MyFirebaseBuilderImpl(requireActivity());
        sharedData = new SharedData(getContext());


        toastHelper = new ToastHelper(requireActivity());



        return viewRoot;
    }



    private void bindView(){


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemViewCacheSize(21);


        firebaseBuilder.fetchRequests();
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
    public void onAcceptedClickedListener(Request current_request) {


        windowHelper = new PopupWindowHelper(requireActivity(), R.layout.reserve_window_layout,R.style.WindowAnimation,
                this);
        windowHelper.displayWindow(sharedData.getHeight() * 0.85, sharedData.getWidth() * 0.85,
                "Upcoming", current_request);
    }

    @Override
    public void onRefusedClickedListener(String request_id) {
        firebaseBuilder.refuseRequest(request_id);

    }


    @Override
    public void onRequestsFetched(ArrayList<Request> requests) {
        RequestsRecyclerAdapter adapter = new RequestsRecyclerAdapter(getContext(), requests, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }



    private String editToString(EditText editText){
        return editText.getText().toString();
    }



    @Override
    public void onPopupViewRootCreated(View windowRoot, String view_type, Object displayed_object) {

        Request request = (Request) displayed_object;
        EditText phone_number = windowRoot.findViewById(R.id.reserveWindow_edit_phoneNumber);
        phone_number.setText(request.getPhone_number());
        phone_number.setFocusable(false);


        EditText name = windowRoot.findViewById(R.id.reserveWindow_edit_name);
        name.setText(request.getName());
        name.setFocusable(false);

        EditText address = windowRoot.findViewById(R.id.reserveWindow_edit_address);
        address.setText(request.getAddress());
        address.setFocusable(false);

        EditText patient_status = windowRoot.findViewById(R.id.reserveWindow_edit_status);





        EditText day = windowRoot.findViewById(R.id.reserveWindow_edit_date_day);
        EditText month = windowRoot.findViewById(R.id.reserveWindow_edit_date_month);
        EditText year = windowRoot.findViewById(R.id.reserveWindow_edit_date_year);
        EditText hour = windowRoot.findViewById(R.id.reserveWindow_edit_hours);
        EditText minutes = windowRoot.findViewById(R.id.reserveWindow_edit_minutes);
        EditText dayTime = windowRoot.findViewById(R.id.reserveWindow_edit_daytime);



        TextView reserve = windowRoot.findViewById(R.id.reserveWindow_textB_reserve);
        reserve.setText(R.string.add_reservation);


        reserve.setOnClickListener(v -> {

            if (editToString(patient_status).trim().length() == 0)
                toastHelper.showFailureMessage("please fill the patient status field");

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


                firebaseBuilder.acceptRequestAndReserve(request,
                        GeneralMethods.stringDateToMillis(editToString(day)+"/"+editToString(month)+"/"+editToString(year) //date
                                +", "+editToString(hour)+":"+editToString(minutes)+" "+editToString(dayTime)),

                        editToString(patient_status)); //time



                windowHelper.dismissWindow();
            }




        });


    }

}
