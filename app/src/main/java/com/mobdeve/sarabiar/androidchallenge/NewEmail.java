package com.mobdeve.sarabiar.androidchallenge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NewEmail extends AppCompatActivity {

    private Button discardButton;
    private Button sendButton;
    private EditText toText;
    private EditText subjectText;
    private EditText bodyText;
    private TextView to;

    private boolean toDrafts = true;
    private boolean fromDrafts = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_email);

        this.discardButton = findViewById(R.id.discard);
        this.sendButton = findViewById(R.id.send);
        this.toText = findViewById(R.id.toText);
        this.subjectText = findViewById(R.id.subjectText);
        this.bodyText = findViewById(R.id.bodyText);
        this.to = findViewById(R.id.to);


        this.discardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDrafts = false;
                if (fromDrafts){
                    deleteDrafts();
                }
                finish();
            }
        });

        this.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (toText.getText().toString().length()<1 || subjectText.getText().toString().length()<1 || bodyText.getText().toString().length()<1){
                    Toast.makeText(NewEmail.this, "Please make sure all entries have text.", Toast.LENGTH_SHORT).show();
                }
                else{
                    toDrafts=false;
                    newEmail();
                    finish();
                }
            }
        });

        Intent draftSent = getIntent();
        if (draftSent.getStringExtra("receiverDraft")!=null || draftSent.getStringExtra("subjectDraft")!=null || draftSent.getStringExtra("bodyDraft")!=null){
            if (draftSent.getStringExtra("receiverDraft")!=null){
                this.toText.setText(draftSent.getStringExtra("receiverDraft"));
            }
            if (draftSent.getStringExtra("subjectDraft")!=null){
                this.subjectText.setText(draftSent.getStringExtra("subjectDraft"));
            }
            if (draftSent.getStringExtra("bodyDraft")!=null){
                this.bodyText.setText(draftSent.getStringExtra("bodyDraft"));
            }
            fromDrafts = true;
        }
    }

    protected void onPause(){
        super.onPause();
        if (toDrafts) {
            saveData();
        }

    }

    protected void saveData(){
        SharedPreferences sharedpreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString("receiverDraft", toText.getText().toString());
        editor.putString("subjectDraft", subjectText.getText().toString());
        editor.putString("bodyDraft", bodyText.getText().toString());
        editor.apply();
    }

    protected void newEmail(){
        SharedPreferences sharedpreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString("receiverNew", toText.getText().toString());
        editor.putString("subjectNew", subjectText.getText().toString());
        editor.putString("bodyNew", bodyText.getText().toString());
        if (fromDrafts){
            deleteDrafts();
        }
        editor.apply();
    }

    protected void deleteDrafts(){
        SharedPreferences sharedpreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.remove("receiverDraft");
        editor.remove("subjectDraft");
        editor.remove("bodyDraft");

        editor.apply();
    }


}