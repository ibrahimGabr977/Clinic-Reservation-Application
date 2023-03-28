package com.hope.igb.italianlab.main.schedules;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hope.igb.italianlab.R;
import com.hope.igb.italianlab.comman.GeneralMethods;
import com.hope.igb.italianlab.networking.models.Reservation;

import java.util.ArrayList;

public class CanceledRecyclerAdapter extends RecyclerView.Adapter<CanceledRecyclerAdapter.CanceledViewHolder> {

    private final Context context;
    private final ArrayList<Reservation> reservations;

    public CanceledRecyclerAdapter(Context context, ArrayList<Reservation> reservations) {
        this.context = context;

        this.reservations = reservations;

    }

    @NonNull
    @Override
    public CanceledRecyclerAdapter.CanceledViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewRoot = LayoutInflater.from(context).inflate(R.layout.schedule_canceled_holder, parent, false);
        return new CanceledRecyclerAdapter.CanceledViewHolder(viewRoot);
    }

    @Override
    public void onBindViewHolder(@NonNull CanceledRecyclerAdapter.CanceledViewHolder holder, int position) {
        Reservation  reservation = reservations.get(position);



        holder.patient_name.setText(reservation.getPatient_name());
        holder.address.setText(reservation.getAddress());
        holder.date.setText(GeneralMethods.millisDateToString(reservation.getDate(), "Date"));
        holder.time.setText(GeneralMethods.millisDateToString(reservation.getDate(), "Time"));
        holder.patient_status.setText(reservation.getPatient_status());
    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }


    class CanceledViewHolder extends RecyclerView.ViewHolder{

        private final TextView patient_name, address, date, time, patient_status;

        public CanceledViewHolder(@NonNull View itemView) {
            super(itemView);

            patient_name = itemView.findViewById(R.id.canceledHolder_text_name);
            address = itemView.findViewById(R.id.canceledHolder_text_address);
            date = itemView.findViewById(R.id.canceledHolder_text_date);
            time = itemView.findViewById(R.id.canceledHolder_text_time);
            patient_status = itemView.findViewById(R.id.canceledHolder_text_patientStatus);





        }
    }
}
