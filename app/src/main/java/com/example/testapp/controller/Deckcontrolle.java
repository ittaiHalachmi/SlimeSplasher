package com.example.testapp.controller;

import com.example.testapp.model.Card;
import com.example.testapp.model.Deck;
import com.example.testapp.model.Card;
import com.example.testapp.model.Deck;

public interface Deckcontrolle {
    public Deck getDeck();
    public Deck changeCard(Card card, int i);
}
