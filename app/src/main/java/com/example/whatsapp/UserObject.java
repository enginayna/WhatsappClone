package com.example.whatsapp;

public class UserObject {
    private String isim;
    private String photoURL;
    private String durum;
    private String UID;

    public UserObject(String isim, String photoURL, String durum,String UID) {
        this.isim = isim;
        this.photoURL = photoURL;
        this.durum = durum;
        this.UID=UID;
    }
    public UserObject(String isim, String photoURL, String durum) {
        this.isim = isim;
        this.photoURL = photoURL;
        this.durum = durum;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getIsim() {
        return isim;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getDurum() {
        return durum;
    }

    public void setDurum(String durum) {
        this.durum = durum;
    }
}
