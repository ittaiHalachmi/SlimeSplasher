package com.example.testapp.model;

public class TileSend {
    private String position;
    private String imageTag;
    private String pow;
    private String color;
    private String effect;
    public  TileSend (String position){
        this.position = position;
    }

    public String getPosition() {
        return position;
    }

    public String getImageTag() {
        return imageTag;
    }

    public void setImageTag(String imageTag) {
        this.imageTag = imageTag;
    }

    public String  getPow() {
        return pow;
    }

    public void setPow(String pow) {
        this.pow = pow;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }
}
