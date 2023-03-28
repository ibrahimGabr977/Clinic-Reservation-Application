package com.hope.igb.italianlab.main.achievements;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hope.igb.italianlab.R;
import com.hope.igb.italianlab.comman.GeneralMethods;
import com.hope.igb.italianlab.networking.models.VideoModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class VideosRecyclerAdapter extends RecyclerView.Adapter<VideosRecyclerAdapter.VideosViewHolder> {


    interface VideoClickedListener{
        void onVideoClickListener(VideoModel video);
    }

    private final ArrayList<VideoModel> videos;
    private final int screenWidth;
    private final VideoClickedListener listener;


    public VideosRecyclerAdapter(ArrayList<VideoModel> videos, int screenWidth, VideoClickedListener listener) {
        this.videos = videos;
        this.screenWidth = screenWidth;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VideosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View viewRoot = LayoutInflater.from(parent.getContext()).inflate(R.layout.achievements_video_holder, parent, false);
        ViewGroup.LayoutParams params = viewRoot.getLayoutParams();
        params.width = (int) ((screenWidth * 0.9) / 4) - 10;
        params.height = (int) ((screenWidth * 0.9) / 4) - 10;

        viewRoot.setLayoutParams(params);

        return new VideosViewHolder(viewRoot);
    }

    @Override
    public void onBindViewHolder(@NonNull VideosViewHolder holder, int position) {

        VideoModel videoModel = videos.get(position);

        if (position == 7){
            holder.duration.setText("");
            holder.duration.setVisibility(View.GONE);
            holder.video_thumb.setImageResource(R.drawable.b_main_achievements_mored);

        }else{
            holder.duration.setVisibility(View.VISIBLE);
            holder.duration.setText(GeneralMethods.millisDateToString(videoModel.getDuration(), "Duration"));
            Picasso.get().load(videoModel.getThumbnail_url()).noPlaceholder().fit().into(holder.video_thumb);

        }


    }

    @Override
    public int getItemCount() {
        return Math.min(videos.size(), 8);
    }

     class VideosViewHolder extends RecyclerView.ViewHolder {

        private final TextView duration;
        private final ImageView video_thumb;

        public VideosViewHolder(@NonNull View itemView) {
            super(itemView);

            video_thumb = itemView.findViewById(R.id.video_holder_image);
            duration = itemView.findViewById(R.id.video_holder_duration);


            video_thumb.setOnClickListener(v -> {

                listener.onVideoClickListener(videos.get(this.getAdapterPosition()));
            });

        }
    }
}
