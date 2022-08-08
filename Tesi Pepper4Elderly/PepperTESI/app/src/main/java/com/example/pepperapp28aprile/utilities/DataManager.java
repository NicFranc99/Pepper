package com.example.pepperapp28aprile.utilities;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.pepperapp28aprile.Globals;
import com.example.pepperapp28aprile.Persona;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private Context c;


    public DataManager(Context c, Persona.Game game, RisultatiManager risultatiManager, onUploadDataListener l) {
        String phat = "pazienti/" + Globals.idPaziente + "/esercizi/"
                + game.getClass().getSimpleName().toLowerCase() + "/" + game.getSetIndexInCategory() + "/risultati";

        DatabaseReference dbUpdate = FirebaseDatabase.getInstance().getReference().child(phat);
        HashMap<String, Object> values = new HashMap<>();
        values.put("tempoInizio", risultatiManager.getStartGameTimeClock());
        values.put("tempoFine", risultatiManager.getStopGameTimeClock());
        values.put("durataQuiz", risultatiManager.getTempoTotale());
        dbUpdate.setValue(values);

        if(risultatiManager.getParoleList()==null){
            int nRisposteSbagliate = 0;
            for (int r : risultatiManager.getListError()) {
                if (r != 0) {
                    nRisposteSbagliate++;
                }
            }
            values.put("nRisposteSbagliate", nRisposteSbagliate);
        }else{
            HashMap<String, String> risp = new HashMap<>();

            for(RisultatiManager.Parole r:risultatiManager.getParoleList()){
                risp.put(r.parola, r.getRisposta().toString());
            }
            dbUpdate.child("singoleRisposte").push().setValue(risp);
        }

        dbUpdate.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                l.onDataSuccess(snapshot.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                l.onDataFailed();
            }
        });

    }

    public DataManager(Context c,String path,String id,onDownloadDataListener listener){
        db = FirebaseDatabase.getInstance();
        this.path = path;
        this.c = c;
        this.downloadDataListener = listener;
        databaseReference = db.getReference(path);
        getPazienteById(Integer.valueOf(id));
    }

    public DataManager(Context c,String path,Persona p,onSaveDataListener listener){
        db = FirebaseDatabase.getInstance();
        this.c = c;
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
        databaseReference.orderByChild("id").equalTo(p.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()) {
                    databaseReference.push().setValue(p);
                    onSaveDataListener.onDataSuccess(p);
                }
                else
                    onSaveDataListener.onDataFailed();
            }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

              }
                //databaseReference.child(String.valueOf(p.getId())).setValue(p);

        // after adding this data we are showing toast message.

        });
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
                    if(nodo.child("id").getValue(Integer.class) != null)
                        if(nodo.child("id").getValue(Integer.class) == id){
                            //anni,città,dataNascita,id,image,name,numeroletto,sesso,surname
                            Persona paziente = new Persona(nodo.child("image").getValue(String.class),
                                    nodo.child("id").getValue(Integer.class),
                                    nodo.child("name").getValue(String.class),
                                    nodo.child("surname").getValue(String.class),
                                    nodo.child("citta").getValue(String.class),
                                    nodo.child("dataNascita").getValue(String.class),
                                    nodo.child("sesso").getValue(String.class),
                                    nodo.child("numeroLetto").getValue(Integer.class));
                            Globals.idPaziente = nodo.getKey();
                            ArrayList<Persona.Game> listaGiochi = paziente.getEsercizi();
                            if (nodo.child("esercizi").getChildrenCount() != 0) {
                                for (DataSnapshot exercise : nodo.child("esercizi").getChildren()) {
                                    // istanzio una classe in base al nome della categoria trovata;
                                    String keycat = exercise.getKey();

                                    if (keycat != null) {
                                        //Se l'esercizio ciclato nel nodo esercizi è di categoria appartenenza :
                                        if (keycat.equalsIgnoreCase(Persona.Appartenenza.class.getSimpleName())) {
                                            int indexCount = 0;
                                            for (DataSnapshot quest : exercise.getChildren()) {
                                                Persona.Appartenenza appartenenza = new Persona.Appartenenza(
                                                        quest.child("titolo").getValue(String.class));
                                                setValoriFromJSON(appartenenza, keycat,paziente);
                                                appartenenza.setSetIndexInCategory(indexCount);
                                                indexCount++;

                                                appartenenza.setLivello(quest.child("livello").getValue(Integer.class));
                                                for (DataSnapshot domande : quest.child("parole").getChildren()) {
                                                    String categoria = domande.child("categoria").getValue(String.class);
                                                    String parola = domande.child("parola").getValue(String.class);
                                                    appartenenza.setDomanda(categoria, parola);
                                                }
                                                listaGiochi.add(appartenenza);
                                            }

                                        } else if (keycat
                                                .equalsIgnoreCase(Persona.Categorizzazione.class.getSimpleName())) {
                                            int indexCount = 0;
                                            for (DataSnapshot quest : exercise.getChildren()) {

                                                Persona.Categorizzazione categorizzazione = new Persona.Categorizzazione(
                                                        quest.child("titolo").getValue(String.class));
                                                categorizzazione.setSetIndexInCategory(indexCount);
                                                indexCount++;
                                                setValoriFromJSON(categorizzazione, keycat,paziente);

                                                for (DataSnapshot cate : quest.child("categorie").getChildren()) {
                                                    categorizzazione.addcatRiferimento(cate.getValue(String.class));
                                                }

                                                categorizzazione.setLivello(quest.child("livello").getValue(Integer.class));

                                                for (DataSnapshot domande : quest.child("parole").getChildren()) {
                                                    String rispostaCorretta = domande.child("rispostaCorretta")
                                                            .getValue(String.class);
                                                    String parola = domande.child("parola").getValue(String.class);
                                                    categorizzazione.setDomanda(rispostaCorretta, parola);
                                                }
                                                listaGiochi.add(categorizzazione);
                                            }
                                        } else if (keycat
                                                .equalsIgnoreCase(Persona.CombinazioniLettere.class.getSimpleName())) {
                                            int indexCount = 0;
                                            for (DataSnapshot quest : exercise.getChildren()) {

                                                Persona.CombinazioniLettere combinazioniLettere = new Persona.CombinazioniLettere(
                                                        (quest.child("titolo").getValue(String.class)));

                                                combinazioniLettere.setSetIndexInCategory(indexCount);
                                                indexCount++;
                                                combinazioniLettere
                                                        .setLivello(quest.child("livello").getValue(Integer.class));
                                                setValoriFromJSON(combinazioniLettere, keycat,paziente);
                                                for (DataSnapshot chara : quest.child("lettere").getChildren()) {
                                                    combinazioniLettere.setLettera(chara.getValue(String.class));
                                                }
                                                combinazioniLettere.setDomandaGioco();
                                                listaGiochi.add(combinazioniLettere);
                                            }
                                        } else if (keycat
                                                .equalsIgnoreCase(Persona.EsistenzaParole.class.getSimpleName())) {
                                            int indexCount = 0;

                                            for (DataSnapshot quest : exercise.getChildren()) {

                                                Persona.EsistenzaParole esistenzaParole = new Persona.EsistenzaParole(
                                                        (quest.child("titolo").getValue(String.class)));

                                                esistenzaParole.setSetIndexInCategory(indexCount);
                                                indexCount++;

                                                esistenzaParole.setLivello(quest.child("livello").getValue(Integer.class));
                                                setValoriFromJSON(esistenzaParole, keycat,paziente);

                                                for (DataSnapshot parole : quest.child("parole").getChildren()) {
                                                    String p = parole.child("parola").getValue(String.class);
                                                    esistenzaParole.setParole(c, p);
                                                    // Log.i("RISPOSTA,PAROLE",p+""+Util.esistenzaParola(c,p)+"");
                                                }
                                                listaGiochi.add(esistenzaParole);
                                            }
                                        } else if (keycat.equalsIgnoreCase(Persona.FinaliParole.class.getSimpleName())) {
                                            int indexCount = 0;
                                            for (DataSnapshot quest : exercise.getChildren()) {

                                                Persona.FinaliParole finaliParole = new Persona.FinaliParole(
                                                        (quest.child("titolo").getValue(String.class)));
                                                finaliParole.setLivello(quest.child("livello").getValue(Integer.class));
                                                setValoriFromJSON(finaliParole, keycat,paziente);
                                                finaliParole.setSetIndexInCategory(indexCount);
                                                indexCount++;
                                                for (DataSnapshot parole : quest.child("parole").getChildren()) {
                                                    finaliParole.setParole(parole.child("parola").getValue(String.class));
                                                }
                                                listaGiochi.add(finaliParole);
                                            }
                                        } else if (keycat
                                                .equalsIgnoreCase(Persona.FluenzeFonologiche.class.getSimpleName())) {
                                            int indexCount = 0;
                                            for (DataSnapshot quest : exercise.getChildren()) {

                                                Persona.FluenzeFonologiche fluenzeFonologiche = new Persona.FluenzeFonologiche(
                                                        (quest.child("titolo").getValue(String.class)));

                                                fluenzeFonologiche.setSetIndexInCategory(indexCount);
                                                indexCount++;
                                                fluenzeFonologiche .setLivello(quest.child("livello").getValue(Integer.class));
                                                setValoriFromJSON(fluenzeFonologiche, keycat,paziente);
                                                fluenzeFonologiche.setDomandaGioco();
                                                listaGiochi.add(fluenzeFonologiche);
                                            }
                                        } else if (keycat
                                                .equalsIgnoreCase(Persona.FluenzeSemantiche.class.getSimpleName())) {
                                            int indexCount = 0;
                                            for (DataSnapshot quest : exercise.getChildren()) {

                                                Persona.FluenzeSemantiche fluenzeSemantiche = new Persona.FluenzeSemantiche(
                                                        (quest.child("titolo").getValue(String.class)));

                                                fluenzeSemantiche.setSetIndexInCategory(indexCount);
                                                indexCount++;
                                                fluenzeSemantiche.setLivello(quest.child("livello").getValue(Integer.class));
                                                fluenzeSemantiche.setCategory(quest.child("categoria").getValue(String.class));
                                                setValoriFromJSON(fluenzeSemantiche, keycat,paziente);

                                                listaGiochi.add(fluenzeSemantiche);
                                                fluenzeSemantiche.setDomandaGioco();
                                            }
                                        } else if (keycat.equalsIgnoreCase(Persona.FluenzeVerbali.class.getSimpleName())) {
                                            int indexCount = 0;
                                            for (DataSnapshot quest : exercise.getChildren()) {

                                                Persona.FluenzeVerbali fluenzeVerbali = new Persona.FluenzeVerbali(
                                                        (quest.child("titolo").getValue(String.class)));

                                                fluenzeVerbali.setSetIndexInCategory(indexCount);
                                                indexCount++;
                                                fluenzeVerbali.setLivello(quest.child("livello").getValue(Integer.class));
                                                setValoriFromJSON(fluenzeVerbali, keycat,paziente);

                                                for (DataSnapshot parole : quest.child("parole").getChildren()) {
                                                    fluenzeVerbali.setParole(parole.child("parola").getValue(String.class));


                                                }
                                                listaGiochi.add(fluenzeVerbali);
                                            }
                                        } else if (keycat
                                                .equalsIgnoreCase(Persona.LettereMancanti.class.getSimpleName())) {
                                            int indexCount = 0;
                                            for (DataSnapshot quest : exercise.getChildren()) {

                                                Persona.LettereMancanti lettereMancanti = new Persona.LettereMancanti(
                                                        (quest.child("titolo").getValue(String.class)));

                                                lettereMancanti.setSetIndexInCategory(indexCount);
                                                indexCount++;
                                                lettereMancanti.setLivello(quest.child("livello").getValue(Integer.class));
                                                setValoriFromJSON(lettereMancanti, keycat,paziente);

                                                for (DataSnapshot parole : quest.child("parole").getChildren()) {
                                                    lettereMancanti.setParole(
                                                            (parole.child("parola").getValue(String.class)).toUpperCase());

                                                }
                                                listaGiochi.add(lettereMancanti);
                                            }
                                        } else if (keycat.equalsIgnoreCase(Persona.Mesi.class.getSimpleName())) {
                                            int indexCount = 0;
                                            for (DataSnapshot quest : exercise.getChildren()) {

                                                Persona.Mesi mesi = new Persona.Mesi(
                                                        (quest.child("titolo").getValue(String.class)));

                                                mesi.setSetIndexInCategory(indexCount);
                                                indexCount++;
                                                mesi.setLivello(quest.child("livello").getValue(Integer.class));
                                                setValoriFromJSON(mesi, keycat,paziente);

                                                for (DataSnapshot elemento : quest.child("domande").getChildren()) {
                                                    Persona.Mesi.Domanda domanda = new Persona.Mesi.Domanda();
                                                    domanda.setTestoDomanda(
                                                            elemento.child("domanda").getValue(String.class));
                                                    domanda.setRispostaCorretta(
                                                            elemento.child("rispostaCorretta").getValue(String.class));

                                                    for (DataSnapshot rsbagliata : elemento.child("risposteSbagliate")
                                                            .getChildren()) {
                                                        String s = rsbagliata.getValue(String.class);
                                                        domanda.setRispostaSbagliata(s);
                                                    }
                                                    mesi.setListaDomande(domanda);

                                                }
                                                listaGiochi.add(mesi);
                                            }
                                        } else if (keycat.equalsIgnoreCase(Persona.Musica.class.getSimpleName())) {
                                            int indexCount = 0;
                                            for (DataSnapshot quest : exercise.getChildren()) {

                                                Persona.Musica musica = new Persona.Musica(
                                                        (quest.child("titolo").getValue(String.class)));

                                                musica.setSetIndexInCategory(indexCount);
                                                indexCount++;
                                                musica.setLivello(quest.child("livello").getValue(Integer.class));
                                                setValoriFromJSON(musica, keycat,paziente);

                                                for (DataSnapshot elemento : quest.child("domande").getChildren()) {
                                                    Persona.Musica.Domanda domanda = new Persona.Musica.Domanda();

                                                    domanda.setUrlMedia(elemento.child("media").getValue(String.class));
                                                    domanda.setRispostaCorretta(
                                                            elemento.child("rispostaCorretta").getValue(String.class));

                                                    for (DataSnapshot rsbagliata : elemento.child("risposteSbagliate")
                                                            .getChildren()) {
                                                        String s = rsbagliata.getValue(String.class);
                                                        domanda.setRispostaSbagliata(s);
                                                    }
                                                    musica.setListaDomande(domanda);

                                                }
                                                listaGiochi.add(musica);
                                            }
                                        } else if (keycat.equalsIgnoreCase(Persona.Racconti.class.getSimpleName())) {
                                            int indexCount = 0;
                                            for (DataSnapshot quest : exercise.getChildren()) {

                                                Persona.Racconti racconti = new Persona.Racconti(
                                                        (quest.child("titolo").getValue(String.class)));

                                                racconti.setSetIndexInCategory(indexCount);
                                                indexCount++;
                                                racconti.setLivello(quest.child("livello").getValue(Integer.class));
                                                racconti.setTestoRacconto(quest.child("testo").getValue(String.class));
                                                setValoriFromJSON(racconti, keycat,paziente);

                                                for (DataSnapshot elemento : quest.child("media").getChildren()) {
                                                    racconti.setUrlsMedia(elemento.getValue(String.class));
                                                }

                                                for (DataSnapshot elemento : quest.child("domande").getChildren()) {
                                                    Persona.Racconti.Domanda domanda = new Persona.Racconti.Domanda();

                                                    domanda.setRispostaCorretta(
                                                            elemento.child("rispostaCorretta").getValue(String.class));
                                                    domanda.setTestoDomanda(
                                                            elemento.child("domanda").getValue(String.class));

                                                    for (DataSnapshot rsbagliata : elemento.child("risposteSbagliate")
                                                            .getChildren()) {
                                                        String s = rsbagliata.getValue(String.class);
                                                        domanda.setRispostaSbagliata(s);
                                                    }
                                                    racconti.setListaDomande(domanda);
                                                }
                                                listaGiochi.add(racconti);
                                            }
                                        } else if (keycat.equalsIgnoreCase(Persona.Volti.class.getSimpleName())) {
                                            int indexCount = 0;
                                            for (DataSnapshot quest : exercise.getChildren()) {

                                                Persona.Volti volti = new Persona.Volti(
                                                        (quest.child("titolo").getValue(String.class)));

                                                volti.setSetIndexInCategory(indexCount);
                                                indexCount++;
                                                volti.setLivello(quest.child("livello").getValue(Integer.class));
                                                setValoriFromJSON(volti, keycat,paziente);
                                                volti.urlMedia(quest.child("media").getValue(String.class));

                                                for (DataSnapshot elemento : quest.child("domande").getChildren()) {
                                                    Persona.Volti.Domanda domanda = new Persona.Volti.Domanda();
                                                    domanda.setTestoDomanda(
                                                            elemento.child("domanda").getValue(String.class));
                                                    domanda.setRispostaCorretta(
                                                            elemento.child("rispostaCorretta").getValue(String.class));

                                                    for (DataSnapshot rsbagliata : elemento.child("risposteSbagliate")
                                                            .getChildren()) {
                                                        String s = rsbagliata.getValue(String.class);
                                                        domanda.setRispostaSbagliata(s);
                                                    }
                                                    volti.setListaDomande(domanda);
                                                }
                                                listaGiochi.add(volti);
                                            }
                                        }
                                    }
                                }
                            } else {
                                listaGiochi.clear();
                            }
                            downloadDataListener.onDataSuccess(paziente);
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
    private void setValoriFromJSON(Persona.Game game, String cat,Persona paziente) {
        String descrizione;
        try {
            descrizione = Util.getElementJSON(c, cat, "descGioco");
            game.setDescrizioneGioco(descrizione,paziente);
        } catch (ExceptionCategoryNotFount exceptionCategoryNotFount) {
            Log.e("JSONERRORE", exceptionCategoryNotFount.toString());
        }

        String inputType;
        try {
            inputType = Util.getElementJSON(c, cat, "inputType");

            for (String type : inputType.split(",")) {

                switch (type) {
                    case "SCRITTURA":
                        game.addInputType(Persona.Game.TypeInputGame.SCRITTURA);
                        break;
                    case "SELEZIONE":
                        game.addInputType(Persona.Game.TypeInputGame.SELEZIONE);
                        break;
                    case "VOCALE":
                        game.addInputType(Persona.Game.TypeInputGame.VOCALE);
                        break;
                    default:
                        game.addInputType(Persona.Game.TypeInputGame.SELEZIONE);

                }

            }

        } catch (ExceptionCategoryNotFount exceptionCategoryNotFount) {
            Log.e("JSONERRORE", exceptionCategoryNotFount.toString());
        }
    }
}

