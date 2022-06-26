package com.example.pepperapp28aprile.utilities;

import androidx.annotation.NonNull;

import com.example.pepperapp28aprile.Persona;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DataManager {
    public interface onDownloadDataListener {
        /**
         * caricamento riuscito e ritorna la classe Paziente
         *
         * @param paziente classe paziente con tutti i dati
         */
        void onDataSuccess(Persona paziente);
        /**
         * caricamento non riuscito per qualche strano errore
         */
        void onDataFailed();

        /**
         * l'utente cercato non esiste
         */
        void notFoundUser();
    }

    public interface onUploadDataListener {
        /**
         * Caricamento dati nel database riuscito
         *
         * @param s stringa
         */

        void onDataSuccess(String s);

        /**
         * caricamento non riuscito per qualche strano errore
         */
        void onDataFailed();

    }
    public onDownloadDataListener downloadDataListener;
    private FirebaseDatabase db;
    private DatabaseReference databaseReference;
    private String path;
    private boolean pazienteTrovato = false;


    public DataManager(String path,int id,onDownloadDataListener listener){
        db = FirebaseDatabase.getInstance();
        this.path = path;
        this.downloadDataListener = listener;
        databaseReference = db.getReference(path);
        //getCancellamiById("-MXs9cz_FkJUpa7DbpEv");
        getCancellamiById(id);
    }

    public void getCancellamiById(int id){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot nodo: dataSnapshot.getChildren()) {
                    if(nodo.getKey().equals(id)){
                        pazienteTrovato = true;
                        String nome = nodo.child("nome").getValue(String.class);
                        String cognome = nodo.child("cognome").getValue(String.class);
                        downloadDataListener.onDataSuccess(new Persona(id,nome,cognome));
                    }
                    // do what you want with key and value
                }
                if(!pazienteTrovato)
                    downloadDataListener.notFoundUser();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

