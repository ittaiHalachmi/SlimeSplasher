package com.example.testapp.model;

import android.widget.ImageView;

public class Card {

    private int power;

    private String position;
    private  String color;
    private final ImageView cardImage;
    private String effect;


    public Card(int power, String position, ImageView cardImage, String color, String effect) {
        this.power = power;
        this.effect = effect;
        this.position = position;
        this.cardImage = cardImage;
        this.color = color;
    }



    public String getEffect() { return effect; }

    public String getColor() {
        return color;
    }
    public void setColor(String color){this.color = color;}

    public ImageView getCardImage() {
        return cardImage;
    }

    public int getPower() {
        return power;
    }
    public void setPower(int power) {
        this.power = power;
    }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
}
