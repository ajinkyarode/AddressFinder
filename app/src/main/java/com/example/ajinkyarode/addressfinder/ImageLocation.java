package com.example.ajinkyarode.addressfinder;

/**
 * Created by ajinkyarode on 3/28/17.
 */
public class ImageLocation {

    //private String name;
    private String address;

    public ImageLocation() {
      /*Blank default constructor essential for Firebase*/
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
