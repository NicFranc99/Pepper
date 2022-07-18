package com.example.pepperapp28aprile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pepperapp28aprile.models.StatisticsAdapter;
import com.example.pepperapp28aprile.utilities.RisultatiManager;

public class StatisticheFragment extends Fragment {
    private final RisultatiManager risultatiManager;
    private View v;

    public StatisticheFragment(RisultatiManager risultatiManager) {
        this.risultatiManager = risultatiManager;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.statistiche_fragment, container, false);
        RecyclerView stat = v.findViewById(R.id.reStatistiche);
        StatisticsAdapter adapter;
        if (risultatiManager.getParoleList() == null) {
            adapter = new StatisticsAdapter(risultatiManager.getListTimeQuestions(), risultatiManager.getListError());
        } else {
            adapter = new StatisticsAdapter(risultatiManager.getParoleList());
        }

        TextView risultatoTotale = v.findViewById(R.id.txtTempoTotale);
        risultatoTotale.setText(risultatiManager.getTempoTotale());

        TextView risultatoMedia = v.findViewById(R.id.txtTempoMedio);
        risultatoMedia.setText(risultatiManager.getTempoMedio());

        Button fine = v.findViewById(R.id.btnMenuPrincipale2);
        fine.setOnClickListener(v -> getActivity().finish());
        fine.requestFocus();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        stat.setLayoutManager(layoutManager);
        stat.setAdapter(adapter);

        return v;
    }
}
