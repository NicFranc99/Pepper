package com.example.pepperapp28aprile.models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pepperapp28aprile.R;
import com.example.pepperapp28aprile.utilities.RisultatiManager;
import com.example.pepperapp28aprile.utilities.TimerManager;

import java.util.List;

public class StatisticsAdapter extends RecyclerView.Adapter<StatisticsAdapter.ItemHolder> {

    private List<Long> listTimeQuestions = null;
    private List<Integer> listError = null;

    private List<RisultatiManager.Parole> paroleList = null;

    public StatisticsAdapter(List<Long> listTimeQuestions, List<Integer> listError) {
        this.listTimeQuestions = listTimeQuestions;
        this.listError = listError;
    }

    public StatisticsAdapter(List<RisultatiManager.Parole> paroleList) {
        this.paroleList = paroleList;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemstat, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        if (paroleList == null) {
            holder.txtDomanda.setText("Domanda " + (position + 1));

            holder.txtTitoloSezione1.setText("Tempo Domanda");
            holder.txtTestoSezione1.setText(TimerManager.convert(listTimeQuestions.get(position)));

            holder.txtTitoloSezione2.setText("Risposte Sbagliate");
            holder.txtTestoSezione2.setText(listError.get(position).toString());
        } else {
            RisultatiManager.Parole p = paroleList.get(position);

            holder.txtDomanda.setText("Parola " + (position + 1));

            holder.txtTitoloSezione1.setText("Parola");
            holder.txtTestoSezione1.setText(p.getParola());

            holder.txtTitoloSezione2.setText("Esito");
            holder.txtTestoSezione2.setText(p.getRisposta().toString());
        }

    }

    @Override
    public int getItemCount() {
        if (paroleList == null) {
            return listTimeQuestions.size();
        } else {
            return paroleList.size();
        }
    }

    static class ItemHolder extends RecyclerView.ViewHolder {
        TextView txtDomanda, txtTitoloSezione1, txtTestoSezione1, txtTitoloSezione2, txtTestoSezione2;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            txtTitoloSezione1 = itemView.findViewById(R.id.txtTitoloSezione1);
            txtTestoSezione1 = itemView.findViewById(R.id.txtTestoSezione1);

            txtTitoloSezione2 = itemView.findViewById(R.id.txtTitoloSezione2);
            txtTestoSezione2 = itemView.findViewById(R.id.txtTestoSezione2);

            txtDomanda = itemView.findViewById(R.id.txtTestoDomanda);

        }
    }
}
