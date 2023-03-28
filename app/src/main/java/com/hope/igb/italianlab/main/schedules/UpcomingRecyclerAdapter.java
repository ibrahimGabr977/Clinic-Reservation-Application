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

public class UpcomingRecyclerAdapter extends RecyclerView.Adapter<UpcomingRecyclerAdapter.UpcomingSchedulesViewHolder> {

    private final Context context;
    private final ArrayList<Reservation> reservations;
    private final UpcomingListener listener;


    public interface UpcomingListener{
        void onRescheduleClickedListener(Reservation current_reservation);
        void onCancelClickedListener(String reservation_id);
    }

    public UpcomingRecyclerAdapter(Context context, ArrayList<Reservation> reservations, UpcomingListener listener) {
        this.context = context;
        this.reservations = reservations;
        this.listener = listener;
    }


    @NonNull
    @Override
    public UpcomingSchedulesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewRoot = LayoutInflater.from(context).inflate(R.layout.schedule_upcoming_holder, parent, false);

        return new UpcomingSchedulesViewHolder(viewRoot);
    }

    @Override
    public void onBindViewHolder(@NonNull UpcomingSchedulesViewHolder holder, int position) {
//        Reservation  reservation = reservations.get(position);


        if (position == 0){
            holder.title.setVisibility(View.VISIBLE);
            holder.title.setText("Nearest Appointment");

        }else if (position == 1){
            holder.title.setVisibility(View.VISIBLE);
            holder.title.setText("Future Appointment");
        }else {
            holder.title.setText(null);
            holder.title.setVisibility(View.GONE);
        }



//        holder.patient_name.setText(reservation.getPatient_name());
//        holder.address.setText(reservation.getAddress());
//        holder.date.setText(GeneralMethods.millisDateToString(reservation.getDate(), "Date"));
//        holder.time.setText(GeneralMethods.millisDateToString(reservation.getDate(), "Time"));
//        holder.patient_status.setText(reservation.getPatient_status());
//



    }

    @Override
    public int getItemCount() {
        return 5;
    }



    public class UpcomingSchedulesViewHolder extends RecyclerView.ViewHolder {

        private final TextView title, patient_name, address, date, time, patient_status;
        private final TextView cancel, reschedule; //clickable textViews

        public UpcomingSchedulesViewHolder(@NonNull View itemView) {
            super(itemView);

            patient_name = itemView.findViewById(R.id.upcomingHolder_text_name);
            address = itemView.findViewById(R.id.upcomingHolder_text_address);
            date = itemView.findViewById(R.id.upcomingHolder_text_date);
            time = itemView.findViewById(R.id.upcomingHolder_text_time);
            patient_status = itemView.findViewById(R.id.upcomingHolder_text_patientStatus);

            cancel = itemView.findViewById(R.id.upcomingHolder_textB_cancel);
            reschedule = itemView.findViewById(R.id.upcomingHolder_textB_reschedule);
            title = itemView.findViewById(R.id.upcomingHolder_text_title);

//
//            reschedule.setOnClickListener(v -> listener.onRescheduleClickedListener(reservations.get(this.getAdapterPosition())));
//
//            cancel.setOnClickListener(v -> listener.onCancelClickedListener(reservations.get(this.getAdapterPosition()).getReservation_id()));

        }
    }
}
