package com.hope.igb.italianlab.main.achievements;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hope.igb.italianlab.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImagesRecyclerAdapter extends RecyclerView.Adapter<ImagesRecyclerAdapter.ImagesViewHolder> {

    interface ImageClickedListener{
        void onImageClickedListener(String image_url);
    }


    private final ArrayList<String> images_urls;
    private final int screenWidth;
    private final ImageClickedListener listener;

    public ImagesRecyclerAdapter(ArrayList<String> images_urls, int screenWidth, ImageClickedListener listener) {
        this.images_urls = images_urls;
        this.screenWidth = screenWidth;
        this.listener = listener;
    }


    @NonNull
    @Override
    public ImagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewRoot = LayoutInflater.from(parent.getContext()).inflate(R.layout.achievements_image_holder, parent, false);
        ViewGroup.LayoutParams params = viewRoot.getLayoutParams();
        params.width = (int) ((screenWidth * 0.9) / 3) - 10;
        params.height = (int) ((screenWidth * 0.9) / 3) - 10;
        

        viewRoot.setLayoutParams(params);

        return new ImagesViewHolder(viewRoot);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagesViewHolder holder, int position) {

        Picasso.get().load(images_urls.get(position)).noPlaceholder().fit()
                .into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return images_urls.size();
    }


     class ImagesViewHolder extends RecyclerView.ViewHolder {
     private final ImageView imageView;


        public ImagesViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_holder);

            imageView.setOnClickListener(v -> {

                listener.onImageClickedListener(images_urls.get(this.getAdapterPosition()));
            });
        }
    }
}
