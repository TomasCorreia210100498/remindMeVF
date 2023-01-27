package com.example.remindme.singleton;

public class galeria {
    private int id;
    private String desc;
    private byte[] image;

    public galeria(String desc, byte[] image, int id) {
        this.id = id;
        this.desc = desc;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getDesc() {
        return desc;
    }

    public byte[] getImage() {
        return image;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
