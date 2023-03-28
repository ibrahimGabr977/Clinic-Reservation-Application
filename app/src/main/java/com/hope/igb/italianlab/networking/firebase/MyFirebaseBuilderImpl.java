package com.hope.igb.italianlab.networking.firebase;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import androidx.annotation.NonNull;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hope.igb.italianlab.comman.SharedData;
import com.hope.igb.italianlab.comman.textshelper.ToastHelper;
import com.hope.igb.italianlab.networking.models.Doctor;
import com.hope.igb.italianlab.networking.models.Request;
import com.hope.igb.italianlab.networking.models.Reservation;
import com.hope.igb.italianlab.networking.models.VideoModel;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MyFirebaseBuilderImpl implements MyFirebaseBuilder {

    private  FirebaseAuth mAuth;
    private final Activity activity;
    private  FirebaseListener listener;
    private final ToastHelper toastHelper;
    private final DatabaseReference reference;
    private  SharedData sharedData;


    public MyFirebaseBuilderImpl(Activity activity) {
        this.activity = activity;


        toastHelper = new ToastHelper(activity);
        reference = FirebaseDatabase.getInstance().getReference();


    }



    @Override
    public void setListener(FirebaseListener listener) {
        this.listener = listener;
    }

    @Override
    public void endValueListeners(ValueEventListener valueEventListener) {
        reference.removeEventListener(valueEventListener);
    }

    @Override
    public void initializeSignIn(SharedData sharedData) {
        this.sharedData = sharedData;


        mAuth = FirebaseAuth.getInstance();


    }


    @Override
    public void verifyPhoneNumber(String phone_number){


        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                //automatically sign in
                signIn(phoneAuthCredential);


            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                toastHelper.showFailureMessage(e.getMessage());

            }

            @Override
            public void onCodeSent(@NonNull String code, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(code, forceResendingToken);

                //call onCodeSentListener on the activity to get the code first then sign in
                ((AuthCommandListener)listener).onCodeSentListener(phone_number, code);
            }
        };





        PhoneAuthProvider.verifyPhoneNumber(
                PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phone_number)       // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(activity)                 // Activity (for callback binding)
                .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                .build());






    }




    @Override
    public void signIn(PhoneAuthCredential phoneAuthCredential){


        mAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(task -> {



                    if (task.isSuccessful()){

                        FirebaseUser user = task.getResult().getUser();

                        //new user
                        //this method will automatic check if the current user is a permitted doctor or normal patient
                        //then notifies the activity with the result using the  FirebaseAuthCommandListener interface
                        //user already exist
                        verifyDoctorUser(user.getMetadata().getCreationTimestamp() == user.getMetadata().getLastSignInTimestamp(), task.getResult().getUser());

                    }else {

                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                        toastHelper.showFailureMessage("incorrect verification code");
                        else
                        toastHelper.showFailureMessage(task.getException().getMessage());
                    }


                });
    }





    @Override
    public void verifyDoctorUser(boolean new_doctor, FirebaseUser firebaseUser) {

        //Permitted Doctors reference contains all permitted doctors phone numbers
        reference.child("Permitted Doctors")
                .child(firebaseUser.getPhoneNumber())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            //a permitted doctor so sign in

                            if (new_doctor){ //first sign in with his phone number so create account


                                ((AuthCommandListener)listener).onNewUserVerifiedCompletedListener();

                                sharedData.saveNewDoctorData(firebaseUser.getUid(), firebaseUser.getPhoneNumber(),
                                        null, null);

                            }else { //already exist account so sign in and save his data to SharedPreferences by SharedData class
                                saveExistingUserToSharedData();
                            }



                        } else{ //this phone number isn't a doctor phone number

                            toastHelper.showFailureMessage("you cannot sign in as doctor, Please contact with admin "
                                    +firebaseUser.getPhoneNumber());
                        }

                        endValueListeners(this);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        toastHelper.showFailureMessage(error.getMessage());
                        endValueListeners(this);
                    }
                });

    }






    @Override
    public void createDoctorAccount(String name, String specialization){


        //this class and reference for patients
        Doctor patient = new Doctor(mAuth.getCurrentUser().getUid(), mAuth.getCurrentUser().getPhoneNumber(), name, specialization);

        reference.child("Doctors").child(mAuth.getCurrentUser().getUid())
                .setValue(patient).addOnCompleteListener(task -> {

                    if (task.isSuccessful()){
                        toastHelper.showSuccessMessage("created account");

                        sharedData.saveNewDoctorData(mAuth.getCurrentUser().getUid(), mAuth.getCurrentUser().getPhoneNumber(),
                                name , specialization);

                        ((AuthCommandListener)listener).onSignedInCompletedListener();

                    }else {
                        toastHelper.showFailureMessage("created account");
                    }
                });

    }






    @Override
    public void saveExistingUserToSharedData() {
        // reference_type to refer to doctor or patient reference
        //this method to get the user information (id, phone number, name and so on) of an existing user
        //from the database and save them on SharedPreference using SharedData class

        reference.child("Doctors").child(""+mAuth.getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                            Doctor doctor = snapshot.getValue(Doctor.class);
                            sharedData.saveNewDoctorData(doctor.getUserId(), doctor.getPhone_number(),
                                    doctor.getName(), doctor.getSpecialization());

                     endValueListeners(this);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        toastHelper.showFailureMessage(error.getMessage());
                        endValueListeners(this);
                    }
                });

        //sign in after save the required data to SharedPreference using Shared data class
        ((AuthCommandListener)listener).onSignedInCompletedListener();

    }






    @Override
    public PhoneAuthCredential getPhoneAuthCredentials(String verificationId, String code){
        return PhoneAuthProvider.getCredential(verificationId, code);

    }




    //====================================================Reservation=======================================================



    @Override
    public void fetchReservations(String reservation_state) {
        ArrayList<Reservation> reservations = new ArrayList<>();
        reference.child("Reservations")
                .child(reservation_state)
                .orderByChild("date")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        reservations.clear();
                        Reservation reservation;
                        for (DataSnapshot dss: snapshot.getChildren()){
                            reservation = dss.getValue(Reservation.class);
                            reservations.add(reservation);

                        }

                        ((ReservingCommandListener)listener).onReservationsFetched(reservations);

                        endValueListeners(this);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        toastHelper.showFailureMessage(error.getMessage());
                        endValueListeners(this);
                    }
                });
    }



    @Override
    public void reserve(Reservation reservation) {

        reference.child("Reservations")
                .child("Upcoming")
                .child(reservation.getReservation_id())
                .setValue(reservation).addOnCompleteListener(task -> {

                    if (task.isSuccessful()){
                        toastHelper.showSuccessMessage( "create reservation");
                    }else {
                        toastHelper.showFailureMessage("cannot  create reservation");
                    }
                });



    }

    @Override
    public void rescheduleReservation (String reservation_id, long date) {

        reference.child("Reservations")
                .child("Upcoming")
                .child(reservation_id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        snapshot.child("date").getRef().setValue(date);

                        toastHelper.showSuccessMessage("rescheduled this reservation");
                        endValueListeners(this);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        toastHelper.showFailureMessage(error.getMessage());
                        endValueListeners(this);
                    }
                });


    }



    private void changeReservationState(Reservation reservation, String current_state, String new_state) {

        //new state and current state ----> Upcoming, Canceled, Completed
        //move reservation from upcoming to completed or canceled references
        reference.child("Reservations")
                .child(new_state) //<<<< Completed or Canceled
                .child(reservation.getReservation_id())
                .setValue(reservation).addOnCompleteListener(task -> {

                    if (task.isSuccessful()){

                        //on success remove it from upcoming
                        FirebaseDatabase.getInstance().getReference()
                                .child("Reservations")
                                .child(current_state)
                                .child(reservation.getReservation_id()).removeValue();

                        toastHelper.showSuccessMessage(new_state.toLowerCase()+" reservation");
                    }else {
                        toastHelper.showFailureMessage("cannot "+new_state.toLowerCase()+" reservation");
                    }
                });
    }



    @Override
    public void cancelReservation(String reservation_id) {

        reference.child("Reservations")
                .child("Upcoming")
                .child(reservation_id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        changeReservationState(snapshot.getValue(Reservation.class), "Upcoming",
                                "Canceled");
                        endValueListeners(this);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        toastHelper.showFailureMessage(error.getMessage());
                        endValueListeners(this);
                    }
                });

    }





    //================================================================Requesting==============================================


//    public void addRequest(Request request){
//        reference.child("Requests")
//                .child(request.getRequest_id())
//                .setValue(request).addOnCompleteListener(task -> {
//
//            if (task.isSuccessful()){
//                toastHelper.showSuccessMessage( "create request");
//            }else {
//                toastHelper.showFailureMessage("cannot  create request");
//            }
//        });
//    }

    @Override
    public void fetchRequests() {
        ArrayList<Request> requests = new ArrayList<>();
         reference.child("Requests")
                 .orderByChild("request_id")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        requests.clear();
                        Request request;
                        for (DataSnapshot dss: snapshot.getChildren()){
                            request = dss.getValue(Request.class);
                            requests.add(request);

                        }
                        ((RequestingCommandListener)listener).onRequestsFetched(requests);
                        endValueListeners(this);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        toastHelper.showFailureMessage(error.getMessage());
                        endValueListeners(this);
                    }
                });
    }




    @Override
    public void acceptRequestAndReserve(Request current_request, long date, String patient_status) {

        Reservation reservation = new Reservation(current_request.getRequest_id(), current_request.getName(), current_request.getPhone_number(),
                current_request.getAddress(), date, patient_status);

        //move request to upcoming  reference
        reference.child("Reservations")
                .child("Upcoming")
                .child(current_request.getRequest_id())
                .setValue(reservation).addOnCompleteListener(task -> {

            if (task.isSuccessful()){

                //on success remove it from requests
                FirebaseDatabase.getInstance().getReference()
                        .child("Requests")
                        .child(current_request.getRequest_id()).removeValue();

                toastHelper.showSuccessMessage("making reservation");
            }else {
                toastHelper.showFailureMessage("cannot make reservation");
            }
        });


    }


    @Override
    public void refuseRequest(String request_id) {
        reference.child("Requests")
                .child(request_id).removeValue();
    }









    //============================================================ِAchievements images and videos====================================================


    private StorageReference storageRef;

    @Override
    public void initializeCloudStorage() {
        storageRef = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public void uploadAchievementImage(Uri image) {

        String fileName = ""+System.currentTimeMillis();
        StorageReference fileRef = storageRef.child("Images").child(fileName);
        UploadTask uploadTask = fileRef.putFile(image);


                uploadTask.addOnFailureListener(exception -> toastHelper.showFailureMessage(exception.getMessage()))


                .addOnSuccessListener(taskSnapshot -> {

                    toastHelper.showSuccessMessage("upload the image");

                    //store the download link on database after uploading
                    fileRef.getDownloadUrl().addOnSuccessListener(uri ->
                            reference.child("Achievements")
                            .child("Images")
                            .child(fileName)
                            .setValue(uri.toString()));



                })


                .addOnProgressListener(snapshot -> {
                    //get uploading progress to views
                    double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();



                    ((AchievementsStorageListener)listener).onUploadProgressChangedListener(progress);
                });

    }


    @Override
    public void uploadAchievementVideo(Uri video, Bitmap video_thumb, long duration) {


        String fileName = ""+System.currentTimeMillis();
        StorageReference fileRef = storageRef.child("Videos").child(fileName);
        UploadTask uploadTask = fileRef.putFile(video);


        uploadTask.addOnFailureListener(exception -> toastHelper.showFailureMessage(exception.getMessage()))


                .addOnSuccessListener(taskSnapshot -> {
                    //store the download link on database after uploading
                    fileRef.getDownloadUrl().addOnSuccessListener(uri -> uploadThumbnail(fileName, video_thumb, uri.toString(), duration));

                })


                .addOnProgressListener(snapshot -> {
                    //get uploading progress to views
                    double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();


                    ((AchievementsStorageListener)listener).onUploadProgressChangedListener(progress);
                });

    }



    private void uploadThumbnail(String file_name, Bitmap video_thumb, String video_url, long duration){

        StorageReference fileRef = storageRef.child("Thumbnails").child(file_name);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        video_thumb.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask =  fileRef.putBytes(data);



                uploadTask.addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> {


                    toastHelper.showSuccessMessage("uploading the video");


                    VideoModel videoModel = new VideoModel(video_url, uri.toString(), duration);

                    reference.child("Achievements")
                            .child("Videos")
                            .child(file_name)
                            .setValue(videoModel);
                }));
    }





    @Override
    public  void fetchAchievementsImages(){

        ArrayList<String> urls=new ArrayList<>();
        reference.child("Achievements")
                .child("Images")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        urls.clear();
                        String url;

                        if (snapshot.getChildrenCount() != 0){
                            for (DataSnapshot dss: snapshot.getChildren()) {
                                url = dss.getValue(String.class);
                                urls.add(url);

                            }
                        }
                        ((AchievementsStorageListener)listener).onImagesFetchedListener(urls);
                        endValueListeners(this);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        toastHelper.showFailureMessage(error.getMessage());
                        endValueListeners(this);
                    }
                });



    }

    @Override
    public void fetchAchievementsVideos() {

        ArrayList<VideoModel> videoModels=new ArrayList<>();
        reference.child("Achievements")
                .child("Videos")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        videoModels.clear();

                        VideoModel videoModel;

                        if (snapshot.getChildrenCount() != 0){
                            for (DataSnapshot dss: snapshot.getChildren()) {
                                videoModel = dss.getValue(VideoModel.class);
                                videoModels.add(videoModel);

                            }
                        }

                        ((AchievementsStorageListener)listener).onVideosFetchedListener(videoModels);
                        endValueListeners(this);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        toastHelper.showFailureMessage(error.getMessage());
                        endValueListeners(this);
                    }
                });
    }







    //================================================================Profile===========================================




    @Override
    public void updateChildValue(String target_child, String new_value) {
        reference.child("Doctors")
                .child(sharedData.getCurrentDoctorId())
                .child(target_child)
                .setValue(new_value)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()){
                        toastHelper.showSuccessMessage("updated "+target_child);

                    } else
                        toastHelper.showFailureMessage(task.getException().getMessage());


                    ((ProfileListener)listener).onUpdateChildListener(target_child, new_value);
                });
    }

    @Override
    public void addPermittedDoctor(String phone_number) {
        reference.child("Permitted Doctors")
                .child(phone_number)
                .setValue(phone_number)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful())
                        toastHelper.showSuccessMessage("added doctor with this phone number");
                    else
                        toastHelper.showFailureMessage(task.getException().getMessage());



                });
    }
}
