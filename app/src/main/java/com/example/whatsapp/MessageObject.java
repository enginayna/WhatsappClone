package com.example.whatsapp;

public class MessageObject {
    private String isim;
    private String photo;
    private String uid;

    public MessageObject(String isim, String photo, String uid) {
        this.isim = isim;
        this.photo = photo;
        this.uid = uid;
    }

    public String getIsim() {
        return isim;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
