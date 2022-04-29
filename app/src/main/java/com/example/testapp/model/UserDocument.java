package com.example.testapp.model;

import com.google.firebase.firestore.auth.User;

import java.util.List;

public class UserDocument {
    public List<TileSend> bordTiles;
    public boolean status;
}
