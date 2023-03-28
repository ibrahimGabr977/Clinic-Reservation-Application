package com.hope.igb.italianlab.networking.models;

public class Doctor {
    private  String userId, phone_number, name, specialization;

    public Doctor(String userId, String phone_number, String name, String specialization) {
        this.userId = userId;
        this.phone_number = phone_number;
        this.name = name;
        this.specialization = specialization;
    }


    Doctor(){}


    public String getUserId() {
        return userId;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getName() {
        return name;
    }

    public String getSpecialization() {
        return specialization;
    }

}
