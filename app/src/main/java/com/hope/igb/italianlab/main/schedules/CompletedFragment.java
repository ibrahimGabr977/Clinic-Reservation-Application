package com.hope.igb.italianlab.main.schedules;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.hope.igb.italianlab.R;
import com.hope.igb.italianlab.networking.firebase.MyFirebaseBuilderImpl;
import com.hope.igb.italianlab.networking.models.Reservation;

import java.util.ArrayList;

public class CompletedFragment extends Fragment implements MyFirebaseBuilderImpl.ReservingCommandListener {
    private RecyclerView recyclerView;
    private final MyFirebaseBuilderImpl firebaseBuilder;

    public CompletedFragment(MyFirebaseBuilderImpl firebaseBuilder) {
        this.firebaseBuilder = firebaseBuilder;
        firebaseBuilder.setListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.schedules_adapter, container, false);

        recyclerView = viewRoot.findViewById(R.id.schedulesRecyclerView);



        return viewRoot;
    }



    private void bindView(){

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemViewCacheSize(21);

        firebaseBuilder.fetchReservations("Completed");
    }



    @Override
    public void onStart() {
        super.onStart();
        bindView();

    }


    @Override
    public void onReservationsFetched(ArrayList<Reservation> reservations) {
        CompletedRecyclerAdapter adapter = new CompletedRecyclerAdapter(getContext(), reservations);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
