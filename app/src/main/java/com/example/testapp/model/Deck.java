package com.example.testapp.model;

public class Deck {

    private Card[] cards;

    public  Deck (Card[] cards){
        this.cards = cards;
    }

    public Card[] getCards() {
        return cards;
    }

    public void changeACard(int i, Card card){
        cards[i] = card;
    }
}
