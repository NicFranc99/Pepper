package com.example.pepperapp28aprile.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pepperapp28aprile.Persona;
import com.example.pepperapp28aprile.R;
import com.example.pepperapp28aprile.RecyclerAnswers;

import java.util.ArrayList;

public class RecyclerViewAnswersAdapter extends RecyclerView.Adapter<RecyclerViewAnswersAdapter.RecyclerViewHolder> {
    public OnItemClickListener listener;
    private Persona.Game.TypeInputGame chose;

    public void setMode(Persona.Game.TypeInputGame chose) {
        this.chose = chose;
    }


    public interface OnItemClickListener {
        void onItemClick(View v, String item,int position);
    }

    public int getPositionByItem(RecyclerAnswers answes){
        return courseDataArrayList.indexOf(answes);
    }

    public RecyclerAnswers getItemByText(String text){
        for (RecyclerAnswers answes:
            courseDataArrayList ) {
            String title = answes.getTitle();
            if(title.trim().equalsIgnoreCase(text.trim())){
                return answes;
            }

        }
        return null;
    }

    private final ArrayList<RecyclerAnswers> courseDataArrayList;
    private Context mcontext;

    public RecyclerViewAnswersAdapter(ArrayList<RecyclerAnswers> recyclerAnswersArrayList, Context mcontext) {
        this.courseDataArrayList = recyclerAnswersArrayList;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate Layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.answer_row, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        // Set the data to textview and imageview.
        RecyclerAnswers recyclerAnswers = courseDataArrayList.get(position);
        holder.card.setOnClickListener(v -> listener.onItemClick(holder.card, recyclerAnswers.getTitle(),position));


        holder.card.setText(recyclerAnswers.getTitle());
        if (chose == Persona.Game.TypeInputGame.SELEZIONE) {
            if (position == 0) {
                holder.card.requestFocus();
            }

        } else if (chose == Persona.Game.TypeInputGame.VOCALE) {
            holder.card.setEnabled(false);
        }

    }

    @Override
    public int getItemCount() {
        return courseDataArrayList.size();
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    // View Holder Class to handle Recycler View.
    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        public final Button card;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            // courseTV = itemView.findViewById(R.id.idTVCourse);
            card = itemView.findViewById(R.id.cardanswer);

        }
    }

}