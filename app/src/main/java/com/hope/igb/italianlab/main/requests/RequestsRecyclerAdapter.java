package com.hope.igb.italianlab.main.requests;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.hope.igb.italianlab.R;
import com.hope.igb.italianlab.networking.models.Request;
import java.util.ArrayList;


public class RequestsRecyclerAdapter extends RecyclerView.Adapter<RequestsRecyclerAdapter.RequestsViewHolder> {

private final Context context;
private final ArrayList<Request> requests;
private final RequestsListener listener;



   interface RequestsListener{
    void onAcceptedClickedListener(Request current_request);
    void onRefusedClickedListener(String request_id);
  }


    public RequestsRecyclerAdapter(Context context, ArrayList<Request> requests, RequestsRecyclerAdapter.RequestsListener listener) {
        this.context = context;
        this.requests = requests;
        this.listener = listener;
    }


    @NonNull
    @Override
    public RequestsRecyclerAdapter.RequestsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewRoot = LayoutInflater.from(context).inflate(R.layout.request_holder, parent, false);

        return new RequestsRecyclerAdapter.RequestsViewHolder(viewRoot);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestsRecyclerAdapter.RequestsViewHolder holder, int position) {
        Request  request = requests.get(position);


        if (position == 0){
            holder.title.setVisibility(View.VISIBLE);
            holder.title.setText(R.string.latest_requests);

        }else if (position == 2){
            holder.title.setVisibility(View.VISIBLE);
            holder.title.setText(R.string.other_requests);
        }else {
            holder.title.setText(null);
            holder.title.setVisibility(View.GONE);
        }



        holder.patient_name.setText(request.getName());
        holder.address.setText(request.getAddress());





    }

    @Override
    public int getItemCount() {
        return requests.size();
    }



    class RequestsViewHolder extends RecyclerView.ViewHolder {

    private final TextView title, patient_name, address;

        public RequestsViewHolder(@NonNull View itemView) {
        super(itemView);

        patient_name = itemView.findViewById(R.id.requestsHolder_text_name);
        address = itemView.findViewById(R.id.requestsHolder_text_address);


       //clickable textViews
        TextView refuse = itemView.findViewById(R.id.requestsHolder_textB_refuse);
        TextView accept = itemView.findViewById(R.id.requestHolder_textB_accept);


        title = itemView.findViewById(R.id.requestsHolder_text_title);


        accept.setOnClickListener(v -> listener.onAcceptedClickedListener(requests.get(this.getAdapterPosition())));

        refuse.setOnClickListener(v -> listener.onRefusedClickedListener(requests.get(this.getAdapterPosition()).getRequest_id()));

    }
}
}
