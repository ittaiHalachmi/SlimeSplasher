package com.example.testapp.model;

import android.view.View;

public class BordTile {
    private String position;
    private View look;
    private String color;
    private Card card;
    public BordTile(String position, View look){
        this.position = position;
        this.look = look;
    }

    public void setCard(Card card) { this.card = card; }

    public View getLook() {
        return look;
    }

    public void setColor(String color) { this.color = color; }

    public String getColor() { return color; }

    public String getPosition() { return position; }

    public Card getCard() { return this.card; }

    public boolean cardIsNull() {
        return card != null;
    }
}
