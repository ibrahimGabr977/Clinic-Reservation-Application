package com.hope.igb.italianlab.networking.models;

public class Request {

    private  String request_id, name, phone_number, address ;


    Request(){}

    public Request(String request_id, String name, String phone_number, String address) {
        this.request_id = request_id;
        this.phone_number = phone_number;
        this.name = name;
        this.address = address;

    }


    public String getRequest_id() {
        return request_id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone_number() {
        return phone_number;
    }
}
