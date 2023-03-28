package com.hope.igb.italianlab.networking.models;

public class Reservation {

    //address  also can contain "Clinical visit" instead of address
    //date will be separate to day date and clock time later
    private  String reservation_id, patient_name, patient_phone_number, address, patient_status;
    private long date;


    Reservation(){}

    public Reservation(String reservation_id, String patient_name, String patient_phone_number,
                       String address, long date, String patient_status) {

        this.reservation_id = reservation_id;
        this.patient_name = patient_name;
        this.patient_phone_number = patient_phone_number;
        this.address = address;
        this.date = date;
        this.patient_status = patient_status;
    }


    public String getReservation_id() {
        return reservation_id;
    }

    public String getPatient_name() {
        return patient_name;
    }

    public String getPatient_phone_number() {
        return patient_phone_number;
    }

    public String getAddress() {
        return address;
    }

    public long getDate() {
        return date;
    }

    public String getPatient_status() {
        return patient_status;
    }


}
