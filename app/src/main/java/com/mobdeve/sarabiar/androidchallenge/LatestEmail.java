package com.mobdeve.sarabiar.androidchallenge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class LatestEmail extends AppCompatActivity {

    private TextView receiverLatest;
    private TextView subjectLatest;
    private TextView bodyLatest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latest_email);

        this.receiverLatest = findViewById(R.id.receiverLatest);
        this.subjectLatest = findViewById(R.id.subjectLatest);
        this.bodyLatest = findViewById(R.id.bodyLatest);

        Intent fromMain = getIntent();

        if (fromMain.getStringExtra("receiver") != null){
            this.receiverLatest.setText("To: " + fromMain.getStringExtra("receiver"));
            this.subjectLatest.setText(fromMain.getStringExtra("subject"));
            this.bodyLatest.setText(fromMain.getStringExtra("body"));
        }

    }
}