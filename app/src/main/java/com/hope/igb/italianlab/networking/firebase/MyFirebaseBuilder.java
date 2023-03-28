package com.hope.igb.italianlab.networking.firebase;

import android.graphics.Bitmap;
import android.net.Uri;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.database.ValueEventListener;
import com.hope.igb.italianlab.comman.SharedData;
import com.hope.igb.italianlab.networking.models.Request;
import com.hope.igb.italianlab.networking.models.Reservation;
import com.hope.igb.italianlab.networking.models.VideoModel;
import java.util.ArrayList;

 interface MyFirebaseBuilder {


     interface FirebaseListener{}




    //=================================login and create account====================================================
     interface AuthCommandListener extends FirebaseListener{

        void onCodeSentListener(String phone_number, String code);
        void onSignedInCompletedListener();
        void onNewUserVerifiedCompletedListener();


    }

    //============================================common methods=====================================================
    void setListener(FirebaseListener listener);
    void endValueListeners(ValueEventListener valueEventListener);
    //===============================================================================================================


    //assign uncommon fields that related only to sign in
    void initializeSignIn(SharedData sharedData);

    void verifyPhoneNumber(String phoneNumber);

    void signIn(PhoneAuthCredential phoneAuthCredential);



    //create a doctor account by getting the name and specialization after verifying phone number
    void createDoctorAccount(String name, String specialization);

    //check if the current user doctor or patient
    void verifyDoctorUser(boolean new_doctor, FirebaseUser firebaseUser);

    //this method to get the user information (id, phone number, name and so on) of an existing user
    //from the database and save them on SharedPreference using SharedData class
    void saveExistingUserToSharedData();


    PhoneAuthCredential getPhoneAuthCredentials(String verificationId, String code);
    //===================================================================================================================




    //===============================================reservations==========================================================
    void reserve(Reservation reservation);


    interface ReservingCommandListener extends FirebaseListener{
        void onReservationsFetched(ArrayList<Reservation> reservations);
    }




    void rescheduleReservation (String reservation_id, long date);

    void cancelReservation (String reservation_id);

    //private method
    // void changeReservationState (Reservation reservation, String reservation_state);

    void fetchReservations(String reservation_state);


    //=====================================================Requests=============================================

    interface RequestingCommandListener extends FirebaseListener{
        void onRequestsFetched(ArrayList<Request> requests);
    }





    void acceptRequestAndReserve(Request current_request, long date, String patient_status);

    void refuseRequest (String request_id);

    //private method
    // void changeReservationState (Reservation reservation, String reservation_state);

    void fetchRequests();



    //============================================================Achievements images and videos===============================================

    interface AchievementsStorageListener extends FirebaseListener{
        void onImagesFetchedListener(ArrayList<String> images_urls);
        void onUploadProgressChangedListener(double progress);
        void onVideosFetchedListener(ArrayList<VideoModel> videoModels);
    }


    void initializeCloudStorage();


    void uploadAchievementImage(Uri image);

    void uploadAchievementVideo(Uri video, Bitmap video_thumb, long duration);

    void fetchAchievementsImages();

    void fetchAchievementsVideos();





    //=======================================================================Profile======================================================

    interface ProfileListener extends  FirebaseListener{
       void onUpdateChildListener(String target_child, String new_value);
    }

    void updateChildValue(String target_child, String new_value);

    void addPermittedDoctor(String phone_number);




}
