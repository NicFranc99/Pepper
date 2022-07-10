package com.example.pepperapp28aprile.utilities;

import androidx.annotation.NonNull;

import com.example.pepperapp28aprile.Persona;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

    public interface onSaveDataListener {
        /**
         * Salvataggio di un paziente riuscito con successo!
         *
         * @param p Persona
         */

        void onDataSuccess(Persona p);

        /**
         * Salvataggio non riuscito per qualche strano errore
         */
        void onDataFailed();

    }
    public onDownloadDataListener downloadDataListener;
    private FirebaseDatabase db;
    private DatabaseReference databaseReference;
    private String path;
    private onSaveDataListener onSaveDataListener;

    public DataManager(String path,int id,onDownloadDataListener listener){
        db = FirebaseDatabase.getInstance();
        this.path = path;
        this.downloadDataListener = listener;
        databaseReference = db.getReference(path);
        getPazienteById(id);
    }

    public DataManager(String path,Persona p,onSaveDataListener listener){
        db = FirebaseDatabase.getInstance();
        this.path = path;
        this.onSaveDataListener = listener;
        databaseReference = db.getReference(path);
        savePaziente(p);
    }

    /**
     * Salva un oggetto della classe persona nel Database Firebase (lasciando al provider la liberà di definire un id per il nodo aggiunto).
     * Il nodo lo si recupera tramite attributo "id"
     * @param p Persona da aggiungere al database Firebase
     */
    public void savePaziente(Persona p){
                // inside the method of on Data change we are setting
                // our object class to our database reference.
                // data base reference will sends data to firebase.
                databaseReference.push().setValue(p);

                //databaseReference.child(String.valueOf(p.getId())).setValue(p);

        // after adding this data we are showing toast message.
                onSaveDataListener.onDataSuccess(p);

    }

    /***
     * Questo metodo prende un id e trova il paziente nel Firebase DB.
     * Se lo trova lo restituisce altrimenti lo aggiunge nel DB.
     * @param id del paziente da cercare nel Firebase Database.
     */
    public void getPazienteById(int id){
        /*databaseReference.child(String.valueOf(id)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                    downloadDataListener.onDataSuccess(dataSnapshot.getValue(Persona.class));
                else
                    downloadDataListener.notFoundUser();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        databaseReference.orderByChild("id").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot nodo : dataSnapshot.getChildren()){
                    System.out.println(nodo);
                    if(nodo.child("id").getValue(Integer.class) != null)
                        if(nodo.child("id").getValue(Integer.class) == id){
                            downloadDataListener.onDataSuccess(nodo.getValue(Persona.class));
                            return;
                        }
                }
                downloadDataListener.notFoundUser();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

