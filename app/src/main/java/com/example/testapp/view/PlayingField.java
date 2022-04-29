package com.example.testapp.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testapp.R;
import com.example.testapp.controller.Pcontroller;
import com.example.testapp.controller.PlayController;
import com.example.testapp.model.BordTile;
import com.example.testapp.model.Card;
import com.example.testapp.model.Deck;
import com.example.testapp.model.TileSend;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;

public class PlayingField extends AppCompatActivity implements view {
    BordTile[] bordTiles = new BordTile[25];
    BordTile[] myCards = new BordTile[8];
    Pcontroller playController;
    Card selectedCard;
    int selectedIndex;
    int points;
    int turnCount;
    int greenLife;
    int redLife;
    boolean tileSelectModeOn = false;
    static boolean directAttack = false;
    boolean MyTurn;

    private static final String TAG = "PlayingField";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing_field);

        Intent intent = getIntent();

        MyTurn = intent.getBooleanExtra("myTurn", false); Toast.makeText(this, MyTurn + "", Toast.LENGTH_SHORT).show();
        String id = intent.getStringExtra("idGame");
        FirebaseFirestore rootNode;
        rootNode = FirebaseFirestore.getInstance();
        DocumentReference myRef = rootNode.collection("games").document(id);

        redLife = 3;
        greenLife = 3;
        playController = new PlayController();
        Card[] cards = new Card[8];
        turnCount = 0;
        points = 0;
        if(MyTurn){
            points = 20;
            ((TextView)findViewById(R.id.PointCounter)).setText(points + "");
        }
        Button b = findViewById(R.id.testbutton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyTurn = true;
            }
        });






        for (int i = 0; i < 8; i++) {
            ImageView imageView = new ImageView(this);

            if (i == 0 || i == 2 || i == 1) {
                imageView.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.king_blob));
                imageView.setTag("king_blob");
                cards[i] = new Card(0, "" + i, imageView, "green", "King");
            }
            if (i == 3) {
                imageView.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.rocket_slime));
                imageView.setTag("rocket_slime");
                cards[i] = new Card(0, "" + i, imageView, "green", "rocket");
            }
            if (i == 4 || i == 5) {
                imageView.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.solid_slime));
                imageView.setTag("solid_slime");
                cards[i] = new Card(0, "" + i, imageView, "green", "solid");
            }

            if (i == 6 || i == 7) {
                imageView.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.send_bucket));
                imageView.setTag("send_bucket");
                cards[i] = new Card(0, "" + i, imageView, "green", "dry");
            }
        }


        myRef.addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    myRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            List<Map<String, Object>> tiles = (List<Map<String, Object>>) task.getResult().get("bordTiles");
                            String turnPass = (String) task.getResult().get("turnPass");

                            if(!task.getResult().getMetadata().hasPendingWrites()){
                                if(!MyTurn){
                                    for (int i = 0; i < 25; i++) {
                                        bordTiles[24 - i].setCard(null);
                                        ((ImageView)bordTiles[24-i].getLook().findViewById(R.id.cardImage)).setImageDrawable(null);
                                        ((TextView)bordTiles[24-i].getLook().findViewById(R.id.cardPow)).setText("");
                                        String color = (String) tiles.get(i).get("color");
                                        switch (color){
                                            case "green":
                                                bordTiles[24 - i].setColor("red");
                                                bordTiles[24 - i].getLook().findViewById(R.id.cardImage).setBackgroundColor(PlayingField.this.getResources().getColor(R.color.red, null));
                                                break;
                                            case "red":
                                                bordTiles[24 - i].setColor("green");
                                                bordTiles[24 - i].getLook().findViewById(R.id.cardImage).setBackgroundColor(PlayingField.this.getResources().getColor(R.color.green, null));
                                                break;
                                        }
                                        if(tiles.get(i).get("imageTag") != ""){
                                            ImageView imageView = new ImageView(PlayingField.this);
                                            String tag = (String) tiles.get(i).get("imageTag");
                                            switch(tag){
                                                case "king_blob":
                                                    imageView.setImageDrawable(AppCompatResources.getDrawable(PlayingField.this, R.drawable.king_blob));
                                                    imageView.setTag("king_blob");
                                                    break;
                                                case "rocket_slime":
                                                    imageView.setImageDrawable(AppCompatResources.getDrawable(PlayingField.this, R.drawable.rocket_slime));
                                                    imageView.setTag("rocket_slime");
                                                    break;
                                                case "solid_slime":
                                                    imageView.setImageDrawable(AppCompatResources.getDrawable(PlayingField.this, R.drawable.solid_slime));
                                                    imageView.setTag("solid_slime");
                                                    break;
                                                case "send_bucket":
                                                    imageView.setImageDrawable(AppCompatResources.getDrawable(PlayingField.this, R.drawable.send_bucket));
                                                    imageView.setTag("send_bucket");
                                                    break;

                                            }
                                            if((String) tiles.get(i).get("pow") != ""){
                                                int n = Integer.valueOf((String) tiles.get(i).get("pow"));
                                                bordTiles[24 - i].setCard(new Card(n, bordTiles[24 -i].getPosition(), imageView, (String) tiles.get(i).get("color"), (String) tiles.get(i).get("effect")));
                                            }
                                            switch (color){
                                                case "green":
                                                    bordTiles[24 - i].getCard().setColor("red");
                                                    break;
                                                case "red":
                                                    bordTiles[24 - i].getCard().setColor("green");
                                                    break;
                                            }
                                            ((ImageView)bordTiles[24 - i].getLook().findViewById(R.id.cardImage)).setImageDrawable(bordTiles[24 - i].getCard().getCardImage().getDrawable());
                                            ((TextView)bordTiles[24 - i].getLook().findViewById(R.id.cardPow)).setText("\n\n\n         " + bordTiles[24 - i].getCard().getPower());
                                        }
                                    }
                                }

                                String damage = (String) task.getResult().get("takeDamage");
                                if(damage != null && damage.equals("1") && !MyTurn){
                                    greenLife--;
                                    ((TextView)findViewById(R.id.greenLife)).setText(greenLife + "");

                                }

                                if(turnPass != null && turnPass.equals("1")){
                                    if (MyTurn){
                                        MyTurn = false;
                                    }
                                    else{
                                        MyTurn = true;
                                        turnCount++;
                                        points = points + 2 + turnCount;
                                        ((TextView)findViewById(R.id.PointCounter)).setText(points + "");
                                    }
                                }

                            }

                        }
                    });


            }
        });

        Deck deck = new Deck(cards);
        setUpField(deck, id);

        Button button = findViewById(R.id.PlusToCost);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView p = findViewById(R.id.PointCounter);
                if(tileSelectModeOn && points > 0){
                   points--;
                   p.setText("   " + points);
                   selectedCard.setPower(selectedCard.getPower()+1);
                }
            }
        });
        Button button2 = findViewById(R.id.MinusToCost);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView p = findViewById(R.id.PointCounter);
                if(tileSelectModeOn){
                    points++;
                    p.setText("   " + points);
                    selectedCard.setPower(selectedCard.getPower()-1);
                }
            }
        });

        Button button3 = findViewById(R.id.turnEnd);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MyTurn){
                    MyTurnEnd(id);
                }

            }
        });
    }



    public void updateBored(String id, boolean myTurn){
        FirebaseFirestore rootNode;
        rootNode = FirebaseFirestore.getInstance();
        DocumentReference myRef;
        Map<String, Object> map = new HashMap<>();
        List<TileSend> bordTilesSend = new ArrayList<>(25);
        if(!myTurn){
            map.put("turnPass","1");
            if (directAttack){
                for (int i = 15; i < 24; i++) {
                    if(bordTiles[i].getColor() == "green"){
                        bordTiles[i].setCard(null);
                        ((ImageView)bordTiles[i].getLook().findViewById(R.id.cardImage)).setImageDrawable(null);
                        ((TextView)bordTiles[i].getLook().findViewById(R.id.cardPow)).setText("");
                    }
                    bordTiles[i].setColor("red");
                    ((ImageView)bordTiles[i].getLook().findViewById(R.id.cardImage)).setBackgroundColor(PlayingField.this.getResources().getColor(R.color.red, null));
                }
                redLife--;
                ((TextView)findViewById(R.id.redLife)).setText(redLife + "");
                directAttack = false;
                if(redLife == 0){
                    //you win dgfdhfhdfgfdfgdsdfgwfgfjeijfbjidfvhbdhjfvbdejjfdhfjijebfjjdbhjijfbwifvhdhduvwvhfuhwbhijebfnkndvkjbscjsijsbcwjfbeknvsjbchjsvdfghjgfdsgf
                }
                map.put("takeDamage","1");
            }
        }
        else {
            map.put("turnPass","0");

        }
        for (int i = 0; i < 25; i++) {
            bordTilesSend.add(new TileSend(bordTiles[i].getPosition()));
            if (bordTiles[i].getCard() != null){
                bordTilesSend.get(i).setEffect(bordTiles[i].getCard().getEffect());
                bordTilesSend.get(i).setPow(String.valueOf(bordTiles[i].getCard().getPower()));
                bordTilesSend.get(i).setImageTag(bordTiles[i].getCard().getCardImage().getTag().toString());
            }

            else{
                bordTilesSend.get(i).setEffect("");
                bordTilesSend.get(i).setPow("");
                bordTilesSend.get(i).setImageTag("");
            }
            bordTilesSend.get(i).setColor(bordTiles[i].getColor());

        }


        map.put("bordTiles", bordTilesSend);
        myRef = rootNode.collection("games").document(id);
        myRef.set(map);
    }

    private void MyTurnEnd(String id) {
        Card[] AllyCards = new Card[25];
        int i2 = 0;
        for (int i = 0; i < 25; i++) {
            if (((TextView)bordTiles[i].getLook().findViewById(R.id.cardPow)).getText() != "" && bordTiles[i].getCard() != null && bordTiles[i].getCard().getPower() > 0){
                if(bordTiles[i].getCard().getColor().equals("green") && !bordTiles[i].getCard().getEffect().equals("solid") && (!SolidInFront(bordTiles[i].getCard()) || bordTiles[i].getCard().getEffect() == "rocket")){
                    AllyCards[i2] = bordTiles[i].getCard();
                    i2++;
                    bordTiles[i].setCard(null);
                    ((TextView) bordTiles[i].getLook().findViewById(R.id.cardPow)).setText("");
                    ((ImageView)bordTiles[i].getLook().findViewById(R.id.cardImage)).setImageDrawable(null);
                }
            }
        }
        playController.moveCardsUp(bordTiles, AllyCards, PlayingField.this);
        updateBored(id, false);

    }

    public boolean SolidInFront (Card card){
        String position = card.getPosition();
        if (!position.equals("40") && !position.equals("41") &&!position.equals("42") &&!position.equals("43") &&!position.equals("44")){
            for (int i = 0; i < 5; i++) {
                position = playController.returnNewPosition(position,null,bordTiles);
                for (int h = 0; h < bordTiles.length; h++) {
                    if (bordTiles[h].getPosition().equals(position)){
                        if(bordTiles[h].getCard() == null){
                            return false;
                        }
                        else if(bordTiles[h].getPosition().equals(position) && bordTiles[h].getCard().getEffect().equals("solid")){
                            return true;
                        }
                    }
                }
            }
            return false;
        }
        return false;
    }

    public boolean KingInFront (Card card){
        String position = card.getPosition();
        if (!position.equals("40") && !position.equals("41") &&!position.equals("42") &&!position.equals("43") &&!position.equals("44")){
            for (int i = 0; i < 5; i++) {
                position = playController.returnNewPosition(position,null,bordTiles);
                for (int h = 0; h < bordTiles.length; h++) {
                    if(bordTiles[h].getCard() != null && bordTiles[h].getPosition().equals(position) && bordTiles[h].getCard().getEffect().equals("King")){
                            return true;
                    }
                }
            }
            return false;
        }
        return false;
    }

    public static void EnemyTakeDamage() {
        directAttack = true;
    }

    private void setUpField(Deck deck, String id) {
        int l = 0;
        int i2 = 0;
        TableLayout field = findViewById(R.id.tableLayout);
        int n = field.getMeasuredWidth();
        TableRow r1 = findViewById(R.id.row1);
        TableRow r2 = findViewById(R.id.row2);
        TableRow r3 = findViewById(R.id.row3);
        TableRow r4 = findViewById(R.id.row4);
        TableRow r5 = findViewById(R.id.row5);

        deck = playController.shafelDeck(deck);
        setUpHand(deck);


        for (int i = 0; i < 5; i++){
            View view2 = LayoutInflater.from(PlayingField.this).inflate(R.layout.custom_card, null);
            view2.setBackgroundColor(PlayingField.this.getResources().getColor(R.color.green, null));
            bordTiles[i] = new BordTile("" + l + "" +i2,view2);
            bordTiles[i].setColor("green");
            int finalI = i;
            view2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (tileSelectModeOn && bordTiles[finalI].getColor().equals("green") && bordTiles[finalI].getCard() == null){
                        bordTiles[finalI].setCard(selectedCard);
                        if(KingInFront(selectedCard)){
                            selectedCard.setPower(selectedCard.getPower() + 1);
                        }
                        ((ImageView)bordTiles[finalI].getLook().findViewById(R.id.cardImage)).setImageDrawable(selectedCard.getCardImage().getDrawable());
                        ((TextView)bordTiles[finalI].getLook().findViewById(R.id.cardPow)).setText("\n\n\n         " + selectedCard.getPower());
                        bordTiles[finalI].getLook().setBackgroundColor(PlayingField.this.getResources().getColor(R.color.green, null));
                        bordTiles[finalI].getCard().setPosition(bordTiles[finalI].getPosition());
                        tileSelectModeOn = false;
                        updateBored(id, true);
                        drawCard(selectedIndex);
                    }
                }
            });
            r1.addView(view2, 206, 250);
            i2++;
        }
        l++;
        i2 =0;
        for (int i = 5; i < 10; i++){
            insertBordTile(l,i,i2,r2);
            i2++;
        }
        l++;
        i2 =0;
        for (int i = 10; i < 15; i++){
            insertBordTile(l,i,i2,r3);
            i2++;
        }
        l++;
        i2 =0;
        for (int i = 15; i < 20; i++){
            insertBordTile(l,i,i2,r4);
            i2++;
        }
        l++;
        i2 =0;
        for (int i = 20; i < 25; i++){
            View view2 = LayoutInflater.from(PlayingField.this).inflate(R.layout.custom_card, null);
            view2.setBackgroundColor(PlayingField.this.getResources().getColor(R.color.red, null));
            bordTiles[i] = new BordTile("" + l+i2,view2);
            bordTiles[i].setColor("red");

            int finalI = i;
            view2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (tileSelectModeOn && bordTiles[finalI].getColor().equals("green") && bordTiles[finalI].getCard() == null){
                        bordTiles[finalI].setCard(selectedCard);
                        ((ImageView)bordTiles[finalI].getLook().findViewById(R.id.cardImage)).setImageDrawable(selectedCard.getCardImage().getDrawable());
                        ((TextView)bordTiles[finalI].getLook().findViewById(R.id.cardPow)).setText("\n\n\n         " + selectedCard.getPower());
                        bordTiles[finalI].getLook().setBackgroundColor(PlayingField.this.getResources().getColor(R.color.green, null));
                        bordTiles[finalI].getCard().setPosition(bordTiles[finalI].getPosition());
                        tileSelectModeOn = false;
                        drawCard(selectedIndex);
                    }
                }
            });
            r5.addView(view2, 206, 250);
            i2++;
        }
       // updateData();
    }

    private void setUpHand(Deck deck) {
        LinearLayout hand = findViewById(R.id.Hand);
        for (int i = 0; i < 4; i++) {
            View view = LayoutInflater.from(PlayingField.this).inflate(R.layout.custom_card, null);
            ((ImageView)view.findViewById(R.id.cardImage)).setImageDrawable(deck.getCards()[i].getCardImage().getDrawable());
            myCards[i] = new BordTile("" + i, view);
            myCards[i].setCard(deck.getCards()[i]);
            int finalI = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (tileSelectModeOn)
                        tileSelectModeOn = false;
                    else
                        tileSelectModeOn = MyTurn;
                    Toast.makeText(PlayingField.this, "" + MyTurn, Toast.LENGTH_SHORT).show();

                    if (tileSelectModeOn){
                        selectedCard = myCards[finalI].getCard();
                        selectedIndex = finalI;
                    }

                }
            });
            hand.addView(view,210,280);
        }
        for (int i = 4; i < 8; i++) {
            View view = LayoutInflater.from(PlayingField.this).inflate(R.layout.custom_card, null);
            ((ImageView)view.findViewById(R.id.cardImage)).setImageDrawable(deck.getCards()[i].getCardImage().getDrawable());
            myCards[i] = new BordTile("" + i, view);
            myCards[i].setCard(deck.getCards()[i]);
        }
    }
    public void drawCard(int i){
        Card temp = new Card(0,"", myCards[i].getCard().getCardImage(),myCards[i].getCard().getColor(),myCards[i].getCard().getEffect());
        myCards[i].setCard(myCards[4].getCard());
        for (int h = 4; h < 7; h++) {
            myCards[h].setCard(myCards[h+1].getCard());
        }
        myCards[7].setCard(temp);

        for (int g = 0; g < 8; g++) {
            ((ImageView)myCards[g].getLook().findViewById(R.id.cardImage)).setImageDrawable(myCards[g].getCard().getCardImage().getDrawable());
        }
    }


    public void insertBordTile(int l, int i, int i2, LinearLayout row){

        View view2 = LayoutInflater.from(PlayingField.this).inflate(R.layout.custom_card, null);
        bordTiles[i] = new BordTile("" + l+i2,view2);
        bordTiles[i].setColor("");
        view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tileSelectModeOn && bordTiles[i].getColor().equals("green") && bordTiles[i].getCard() == null){
                    bordTiles[i].setCard(selectedCard);
                    ((ImageView)bordTiles[i].getLook().findViewById(R.id.cardImage)).setImageDrawable(selectedCard.getCardImage().getDrawable());
                    ((TextView)bordTiles[i].getLook().findViewById(R.id.cardPow)).setText("\n\n\n         " + selectedCard.getPower());
                    bordTiles[i].getLook().setBackgroundColor(PlayingField.this.getResources().getColor(R.color.green, null));
                    bordTiles[i].getCard().setPosition(bordTiles[i].getPosition());
                    tileSelectModeOn = false;
                    drawCard(selectedIndex);
                }
            }
        });
        row.addView(view2, 206, 250);

    }
    public void send(){
        
    }



}
