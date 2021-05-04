package com.mobdeve.sarabiar.androidchallenge;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {

    private Button latestButton;
    private Button newButton;
    private RecyclerView emailList;

    private ArrayList<String> receivers = new ArrayList<String>();
    private ArrayList<String> subjects = new ArrayList<String>();
    private ArrayList<String> bodies = new ArrayList<String>();

    private String receiverDraft;
    private String subjectDraft;
    private String bodyDraft;
    private boolean hasDraft = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.latestButton = findViewById(R.id.latestButton);
        this.newButton = findViewById(R.id.newButton);
        this.emailList = findViewById(R.id.emails);

        this.newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasDraft) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Draft email present")
                            .setMessage("By creating a new email, you'll automatically delete the draft. Do you wish to proceed?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    receiverDraft = null;
                                    subjectDraft = null;
                                    bodyDraft = null;
                                    Intent goToNew = new Intent(MainActivity.this, NewEmail.class);
                                    startActivity(goToNew);
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                else {
                    Intent goToNew = new Intent(MainActivity.this, NewEmail.class);
                    startActivity(goToNew);
                }
            }
        });

        this.latestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToLatest = new Intent(MainActivity.this, LatestEmail.class);
                if ((hasDraft && receivers.size()==1) || receivers.size()==0){
                    Toast.makeText(MainActivity.this, "There are no emails currently.", Toast.LENGTH_SHORT).show();
                }
                else{
                    goToLatest.putExtra("receiver", MainActivity.this.receivers.get(receivers.size()-1));
                    goToLatest.putExtra("subject", MainActivity.this.subjects.get(subjects.size()-1));
                    goToLatest.putExtra("body", MainActivity.this.bodies.get(bodies.size()-1));
                    startActivity(goToLatest);
                }
            }
        });


    }
    protected void onResume(){
        super.onResume();
        hasDraft = false;
        loadData();
        if (hasDraft){
            if (receiverDraft!=null && !receiverDraft.equals("")){
                receivers.add(0, receiverDraft);
            }
            else{
                receivers.add(0, "<RECEIVER EMAIL>");
            }
            if (subjectDraft!=null && !subjectDraft.equals("")){
                subjects.add(0, subjectDraft);
            }
            else{
                subjects.add(0, "<EMAIL SUBJECT>");
            }
            if (bodyDraft!=null && !bodyDraft.equals("")){
                bodies.add(0, bodyDraft);
            }
            else{
                bodies.add(0, "<EMAIL BODY>");
            }

        }

        emailAdapter adapter = new emailAdapter(this, receivers, subjects, bodies, hasDraft);
        emailList.setAdapter(adapter);
        emailList.setLayoutManager(new LinearLayoutManager(this));
        adapter.setOnItemClickListener(new emailAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if(position==0 && hasDraft){
                    Intent goToNew = new Intent (MainActivity.this, NewEmail.class);
                    if (receiverDraft != null && !receiverDraft.equals("<RECEIVER EMAIL>")){
                        goToNew.putExtra("receiverDraft", receiverDraft);
                    }
                    if (subjectDraft != null && !subjectDraft.equals("<EMAIL SUBJECT>")){
                        goToNew.putExtra("subjectDraft", subjectDraft);
                    }
                    if (bodyDraft != null && !bodyDraft.equals("<EMAIL BODY>")){
                        goToNew.putExtra("bodyDraft", bodyDraft);
                    }
                    startActivity(goToNew);
                }
            }
        });

    }


    protected void onPause(){
        super.onPause();
        if (hasDraft){
            receivers.remove(0);
            subjects.remove(0);
            bodies.remove(0);
        }
        saveData();
        receiverDraft = null;
        subjectDraft = null;
        bodyDraft = null;
    }

    public void saveData(){

        Set<String> receiversSet = new LinkedHashSet<String>(receivers);
        Set<String> subjectsSet = new LinkedHashSet<String>(subjects);
        Set<String> bodiesSet = new LinkedHashSet<String>(bodies);

        SharedPreferences sharedpreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        StringBuilder receiverString = new StringBuilder();
        for (int i =0; i<receivers.size(); i++){
            receiverString.append(receivers.get(i)).append(",");
        }
        StringBuilder subjectString = new StringBuilder();
        for (int i =0; i<receivers.size(); i++){
            subjectString.append(subjects.get(i)).append(",");
        }
        StringBuilder bodyString = new StringBuilder();
        for (int i =0; i<receivers.size(); i++){
            bodyString.append(bodies.get(i)).append(",");
        }



        editor.putString("receivers", receiverString.toString());
        editor.putString("subjects", subjectString.toString());
        editor.putString("bodies", bodyString.toString());
        editor.putString("receiverDraft", receiverDraft);
        editor.putString("subjectDraft", subjectDraft);
        editor.putString("bodyDraft", bodyDraft);
        editor.putInt("emailListSize", receivers.size());
        editor.apply();
    }

    public void loadData(){
        SharedPreferences sharedpreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        String tempReceivers = sharedpreferences.getString("receivers", null);
        String tempSubjects = sharedpreferences.getString("subjects", null);
        String tempBodies = sharedpreferences.getString("bodies", null);
        String tempReceiverDraft = sharedpreferences.getString("receiverDraft", null);
        String tempSubjectDraft = sharedpreferences.getString("subjectDraft", null);
        String tempBodyDraft = sharedpreferences.getString("bodyDraft", null);
        String tempReceiverNew = sharedpreferences.getString("receiverNew", null);
        String tempSubjectNew = sharedpreferences.getString("subjectNew", null);
        String tempBodyNew = sharedpreferences.getString("bodyNew", null);
        Integer emailListSize = sharedpreferences.getInt("emailListSize", 0);
        if (tempReceivers!=null && tempSubjects!=null && tempBodies!=null){
            receivers = new ArrayList<String>();
            subjects = new ArrayList<String>();
            bodies = new ArrayList<String>();

            StringTokenizer stReceivers = new StringTokenizer(tempReceivers, ",");
            for (int i=0; i<emailListSize; i++){
                receivers.add(stReceivers.nextToken());
            }
            StringTokenizer stSubjects = new StringTokenizer(tempSubjects, ",");
            for (int i=0; i<emailListSize; i++){
                subjects.add(stSubjects.nextToken());
            }
            StringTokenizer stBodies = new StringTokenizer(tempBodies, ",");
            for (int i=0; i<emailListSize; i++){
                bodies.add(stBodies.nextToken());
            }

            for (int i =0; i<emailListSize; i++){
                receivers.set(i, prepareTextForDisplay(receivers.get(i)));
                subjects.set(i, prepareTextForDisplay(subjects.get(i)));
                bodies.set(i, prepareTextForDisplay(bodies.get(i)));
            }

        }
        if((tempReceiverDraft!=null && !tempReceiverDraft.equals("")) || (tempSubjectDraft != null && !tempSubjectDraft.equals("")) || (tempBodyDraft!=null && !tempBodyDraft.equals(""))){
            hasDraft = true;
            receiverDraft =  prepareTextForDisplay(tempReceiverDraft);
            subjectDraft =  prepareTextForDisplay(tempSubjectDraft);
            bodyDraft =  prepareTextForDisplay(tempBodyDraft);
        }
        if (tempReceiverNew!=null){
            receivers.add(tempReceiverNew);
            subjects.add(tempSubjectNew);
            bodies.add(tempBodyNew);
            sharedpreferences.edit().remove("receiverNew").apply();
            sharedpreferences.edit().remove("subjectNew").apply();
            sharedpreferences.edit().remove("bodyNew").apply();
        }

    }

    private String prepareTextForDisplay(String x) {
        return x.replaceAll("(\\n)+"," ");
    }
}