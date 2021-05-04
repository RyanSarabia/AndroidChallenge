package com.mobdeve.sarabiar.androidchallenge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class emailAdapter extends RecyclerView.Adapter <emailAdapter.cartViewHolder> {

    ArrayList<String>receiver, subject, body;
    Context context;
    boolean hasDraft;
    private OnItemClickListener mListerner;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListerner = listener;
    }

    public emailAdapter (Context ct, ArrayList<String>receiver, ArrayList<String> subject, ArrayList<String> body, boolean hasDraft){
        context = ct;
        this.receiver = receiver;
        this.subject=subject;
        this.body=body;
        this.hasDraft=hasDraft;
    }

    @NonNull
    @Override
    public cartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_email_row, parent, false);
        return new cartViewHolder(view, mListerner);
    }

    @Override
    public void onBindViewHolder(@NonNull cartViewHolder holder, int position) {
        if (position ==0 && hasDraft){

            holder.draft.setText("DRAFT");
            holder.draft.setTypeface(Typeface.DEFAULT_BOLD);
            holder.draft.setTextColor(Color.parseColor("#FF0000"));
            holder.receiver.setText("To: " + receiver.get(position));
            holder.subject.setText(subject.get(position));
            holder.body.setText(body.get(position));
        }
        else{
            holder.receiver.setText("To: " + receiver.get(position));
            holder.subject.setText(subject.get(position));
            holder.body.setText(body.get(position));
        }


    }

    @Override
    public int getItemCount() {
        return receiver.size();
    }

    public class cartViewHolder extends RecyclerView.ViewHolder{

        TextView receiver, subject, body, draft;

        public cartViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            receiver = itemView.findViewById(R.id.receiverRow);
            subject = itemView.findViewById(R.id.subjectRow);
            body = itemView.findViewById(R.id.bodyRow);
            draft = itemView.findViewById(R.id.draft);


            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
