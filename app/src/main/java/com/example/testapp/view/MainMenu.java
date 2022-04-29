package com.example.testapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testapp.R;
import com.example.testapp.model.TileSend;
import com.example.testapp.model.UserDocument;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);


        Intent i = getIntent();
        String userName = i.getStringExtra("userName");
        TextView textView = findViewById(R.id.userName);
        textView.setText(userName);

        Button beckBuilder = findViewById(R.id.DeckBuilder);
        Button shop = findViewById(R.id.Shop);
        Button findGame = findViewById(R.id.FindGame);

        beckBuilder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainMenu.this, ruleBook.class);
                startActivity(i);
            }
        });

        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // go to shop menu
            }
        });


        findGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore rootNode;
                rootNode = FirebaseFirestore.getInstance();
                Intent i = new Intent(MainMenu.this, PlayingField.class);
                rootNode.collection("games").whereEqualTo("status", true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) {
                              //  Toast.makeText(MainMenu.this, "match found", Toast.LENGTH_SHORT).show();
                                String id = task.getResult().getDocuments().get(0).getId();
                                i.putExtra("myTurn", false);
                                i.putExtra("idGame", id);
                                startActivity(i);


                            }
                            else {
                                Else(i, rootNode);
                            }
                        }
                        else{
                          Else(i, rootNode);
                        }
                    }
                });

            }
        });

    }
    public void Else(Intent i, FirebaseFirestore rootNode){
        List<TileSend> bordTilesSend = new ArrayList<>(25);
        for (int j = 0; j < 25; j++) {
            bordTilesSend.add(new TileSend("00"));
            bordTilesSend.get(j).setPow("0");
            bordTilesSend.get(j).setImageTag("");
            bordTilesSend.get(j).setEffect("");
            switch (j){
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                    bordTilesSend.get(j).setColor("green");
                    break;
                case 20:
                case 21:
                case 22:
                case 23:
                case 24:
                    bordTilesSend.get(j).setColor("red");
                    break;
            }
            bordTilesSend.get(j).setColor("");
        }
        DocumentReference myRef;
        Map<String, Object> map = new HashMap<>();
        map.put("bordTiles", bordTilesSend);
        myRef = rootNode.collection("games").document("game3");
        map.put("status", true);
        myRef.set(map);
        String id2 = myRef.getId();
        i.putExtra("idGame", id2);
        i.putExtra("myTurn", true);

        startActivity(i);
    }

}