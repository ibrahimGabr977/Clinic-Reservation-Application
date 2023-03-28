package com.hope.igb.italianlab.main.achievements;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hope.igb.italianlab.R;
import com.hope.igb.italianlab.comman.PopupWindowHelper;
import com.hope.igb.italianlab.comman.SharedData;
import com.hope.igb.italianlab.networking.firebase.MyFirebaseBuilderImpl;
import com.hope.igb.italianlab.networking.models.VideoModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AchievementsFragment extends Fragment implements
        MyFirebaseBuilderImpl.AchievementsStorageListener,
        VideosRecyclerAdapter.VideoClickedListener,
        ImagesRecyclerAdapter.ImageClickedListener,
        PopupWindowHelper.PopupWindowListener {

    private RecyclerView imagesRecyclerView, videosRecyclerView;
    private MyFirebaseBuilderImpl firebaseBuilder;
    private View viewRoot;
    private PopupWindowHelper windowHelper;
    private SharedData sharedData;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        viewRoot = inflater.inflate(R.layout.achievements_fragment, container, false);

        videosRecyclerView = viewRoot.findViewById(R.id.videosRecyclerView);
        imagesRecyclerView = viewRoot.findViewById(R.id.imagesRecyclerView);
        FloatingActionButton upload_button = viewRoot.findViewById(R.id.achievements_upload_file);


        upload_button.setOnClickListener(v -> uploadNewAchievement());



        firebaseBuilder=new MyFirebaseBuilderImpl(requireActivity());
        firebaseBuilder.initializeCloudStorage();
        sharedData = new SharedData(getContext());



        return viewRoot;
    }



    private void uploadNewAchievement(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/* video/*");
        startMyActivityResult.launch(Intent.createChooser(intent, "Select Picture"));

    }




    private  final ActivityResultLauncher<Intent> startMyActivityResult =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {


                        if (result.getResultCode() == Activity.RESULT_OK) {

                            if (result.getData().getData() != null) {

                                Uri fileUri = result.getData().getData();

                                if (fileUri.toString().contains("video")){

                                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                                    retriever.setDataSource(requireActivity(), fileUri);
                                    Bitmap video_thumb = retriever.getFrameAtTime();
                                    long duration = Long.parseLong(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));

                                    firebaseBuilder.uploadAchievementVideo(fileUri, video_thumb, duration);

                                }else {
                                    firebaseBuilder.uploadAchievementImage(fileUri);


                                }



                            }
                        }
                    });






    @Override
    public void onUploadProgressChangedListener(double progress) {

        ProgressBar progressBar = requireActivity().findViewById(R.id.uploadProgress);
        TextView progress_text = requireActivity().findViewById(R.id.progress_text);
        View disabled_view = requireActivity().findViewById(R.id.achievements_uploading_view);//this view will disable action till uploading finish

        if (progress == 100){
            progressBar.setProgress(0);
            progressBar.setVisibility(View.GONE);
            progress_text.setText("0%");
            progress_text.setVisibility(View.GONE);
            disabled_view.setVisibility(View.GONE);

        }else {

            progressBar.setVisibility(View.VISIBLE);
            progress_text.setVisibility(View.VISIBLE);
            disabled_view.setVisibility(View.VISIBLE);
            disabled_view.setClickable(false);
            progressBar.setProgress((int) progress);
            progress_text.setText((int) progress+"%");
        }


    }



    @Override
    public void onImagesFetchedListener(ArrayList<String> images_urls) {



        TextView empty_images = viewRoot.findViewById(R.id.achievements_text_empty_images);


        if (images_urls.size() != 0){
            empty_images.setVisibility(View.GONE);
            empty_images.setText(null);



            ImagesRecyclerAdapter adapter = new ImagesRecyclerAdapter(images_urls, sharedData.getWidth(),
                    this);
            imagesRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }else {

            empty_images.setVisibility(View.VISIBLE);

            empty_images.setText(R.string.empty_images_adapter);

        }



    }

    @Override
    public void onVideosFetchedListener(ArrayList<VideoModel> videos) {
        TextView empty_videos = viewRoot.findViewById(R.id.achievements_text_empty_videos);


        if (videos.size() != 0){
            empty_videos.setVisibility(View.GONE);
            empty_videos.setText(null);


            VideosRecyclerAdapter adapter = new VideosRecyclerAdapter(videos, sharedData.getWidth(),
                    this);
            videosRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }else {


            empty_videos.setVisibility(View.VISIBLE);
            empty_videos.setText(R.string.empty_videos_adapter);

        }

    }








    @Override
    public void onImageClickedListener(String image_url) {

        windowHelper = new PopupWindowHelper(requireActivity(), R.layout.image_displayer_window, R.style.WindowAnimation, this);
        windowHelper.displayWindow(sharedData.getHeight() * 0.61, sharedData.getWidth() * 0.85, "Image", image_url);

    }

    @Override
    public void onVideoClickListener(VideoModel video) {

        windowHelper = new PopupWindowHelper(requireActivity(), R.layout.video_displayer_window, R.style.WindowAnimation, this);
        windowHelper.displayWindow(sharedData.getHeight() * 0.61, sharedData.getWidth() * 0.85,
                "Video", video);


    }





    private void bindImagesView(){

        imagesRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new GridLayoutManager(getContext(), 3,
                GridLayoutManager.VERTICAL, false);

        imagesRecyclerView.setLayoutManager(layoutManager);
        imagesRecyclerView.setItemViewCacheSize(24);

        firebaseBuilder.fetchAchievementsImages();

    }


    private void bindVideosView(){

        videosRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new GridLayoutManager(getContext(), 4,
                GridLayoutManager.VERTICAL, false);
        videosRecyclerView.setLayoutManager(layoutManager);

        videosRecyclerView.setItemViewCacheSize(24);

        firebaseBuilder.fetchAchievementsVideos();
    }


    @Override
    public void onStart() {
        super.onStart();
        bindImagesView();
        bindVideosView();
        firebaseBuilder.setListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        firebaseBuilder.setListener(null);
        if (windowHelper != null){
            windowHelper.dismissWindow();
            windowHelper = null;
        }
    }




    @Override
    public void onPopupViewRootCreated(View windowRoot, String view_type, Object displayed_Object) {

        if (view_type.equals("Image")){
            ImageView imageView = windowRoot.findViewById(R.id.image_display_view);
            imageView.setClipToOutline(true);
            Picasso.get().load((String) displayed_Object).noPlaceholder().fit()
                    .into(imageView);


        }else {
            VideoView videoView;
            MediaController controller;

           ProgressDialog bar=new ProgressDialog(viewRoot.getContext());
            bar.setTitle("Connecting server");
            bar.setMessage("Please Wait... ");
            bar.setCancelable(false);
            bar.show();


            if(bar.isShowing()) {

                videoView = windowRoot.findViewById(R.id.video_display_view);
                videoView.setClipToOutline(true);
                Uri uri = Uri.parse(((VideoModel) displayed_Object).getVideo_url());
                videoView.setVideoURI(uri);
                videoView.start();
                controller = new MediaController(viewRoot.getContext());
                controller.setMediaPlayer(videoView);
                videoView.setMediaController(controller);
                videoView.requestFocus();

            }
            bar.dismiss();


        }






    }



}
