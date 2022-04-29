package com.example.testapp.controller;

import android.content.Context;

import com.example.testapp.model.BordTile;
import com.example.testapp.model.Card;
import com.example.testapp.model.Deck;
import com.example.testapp.view.PlayingField;
import com.example.testapp.model.BordTile;
import com.example.testapp.model.Card;
import com.example.testapp.model.Deck;
import com.example.testapp.view.PlayingField;

public interface Pcontroller {


    void moveCardsUp(BordTile[] bordTiles, Card[] cards, Context context);
    Deck shafelDeck(Deck deck);
    String returnNewPosition(String position, Card card, BordTile[] bordTiles);
}
