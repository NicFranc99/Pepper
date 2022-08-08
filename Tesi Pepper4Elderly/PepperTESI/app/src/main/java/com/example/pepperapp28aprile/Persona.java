package com.example.pepperapp28aprile;


import android.content.Context;
import android.util.Log;

import com.example.pepperapp28aprile.utilities.Util;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Persona implements Serializable {

    private String image;
    private int id;
    private String name;
    private String surname; //surname
    private ArrayList<String> training;
    private ArrayList<Game> esercizi;
    private static Persona paziente;
    private int anni;
    private String dataNascita;
    private int numeroLetto;
    private String citta;
    private String provincia;
    private String sesso;

    public Persona() {
        image = new String();
        id = 0;
        name = new String();
        surname = new String();
        anni = 0;
        dataNascita = new String();
        numeroLetto = 0;
        citta = new String();
        provincia = new String();
        sesso = new String();
        paziente = this;
        esercizi = new ArrayList<Game>();
    }

    public Persona(String image, int id, String name, String status, ArrayList<String> training) {
        this.image = image;
        this.id = id;
        this.name = name;
        surname = status;
        this.training = training;
    }

    public Persona(String image, int id, String name, String status,String cittaNascita,String dataNascita,String gender,int room) {
        this.image = image;
        this.id = id;
        this.name = name;
        surname = status;
        training = new ArrayList<>();
        esercizi = new ArrayList<Game>();
        this.citta = cittaNascita;
        this.dataNascita = dataNascita;
        this.numeroLetto = room;
        this.sesso = gender;
        this.anni = getAnni(dataNascita);
    }

    public static Persona getIstance() {
        if (paziente == null) {
            paziente = new Persona();
        }
        return paziente;
    }

    public Persona(String image, int id, String name, String status) {
        this.image = image;
        this.id = id;
        this.name = name;
        surname = status;
        training = new ArrayList<>();
    }

    public Persona(int id, String name, String surname){
        this.id = id;
        this.name = name;
        this.surname = surname;
    }

    public String getImage() {
        return image;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAnni(){
        return anni;
    }

    public int getAnni(String dataNascita){
        try {
            Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(dataNascita);
            Date date = new Date();

            Calendar calendar2 = new GregorianCalendar();
            calendar2.setTime(date1);

            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            int year = calendar.get(Calendar.YEAR);
            int dateFromInput = calendar2.get(Calendar.YEAR);
            return year - dateFromInput;
        }catch(Exception ex){

        }
        return 0;
    }

    public String getDataNascita(){
        return dataNascita;
    }

    public String getCitta(){
        return citta;
    }

    public String getProvincia(){
        return provincia;
    }

    public String getSesso(){
        return sesso;
    }

    public int getNumeroLetto(){
        return numeroLetto;
    }


    public String getSurname() {
        return surname;
    }

    public String toString() {
        return name+"_"+surname;
    }

    public ArrayList<Game> getEsercizi(){
        return esercizi;
    }

    public String urlFullName() {
        String complete = name + "_" + surname.replace(" ", "_");
        complete.replace(" ", "_");
        return complete;
    }

    public abstract static class Game implements Serializable{

        public enum TypeInputGame {
            SELEZIONE, SCRITTURA, VOCALE
        }

        private int setIndexInCategory = 0;

        private String descrizioneGioco = "";
        private String titleCategory = "Titolo Categoria";
        private String titleGame = "Titolo Categoria";
        private int nameIcon = R.drawable.appartenenza;
        private int livello = 0;
        private ArrayList<TypeInputGame> inputType = new ArrayList<>();

        private List<Domanda> domande = new ArrayList<>();

        public int getLivello() {
            return livello;
        }

        public String getDescrizioneGioco() {
            return descrizioneGioco;
        }

        public void setDescrizioneGioco(String descrizione,Persona paziente) {
            String d = descrizione.replace("%nomeutente%", paziente.getName()).replace("%categoria%",
                    titleGame.toUpperCase());
            this.descrizioneGioco = d;
        }

        public String getTitleCategory() {
            return titleCategory;
        }

        public String getTitleGame() {
            return titleGame.toLowerCase();
        }

        public int getNameIcon() {
            return nameIcon;
        }

        public int getSetIndexInCategory() {
            return setIndexInCategory;
        }

        public void setSetIndexInCategory(int setIndexInCategory) {
            this.setIndexInCategory = setIndexInCategory;
        }

        public void setTitleCategory(String titleCategory) {
            this.titleCategory = titleCategory.replaceAll("(\\p{Ll})(\\p{Lu})", "$1 $2");
        }

        public void setTitleGame(String titleGame) {
            this.titleGame = titleGame.replaceAll("(\\p{Ll})(\\p{Lu})", "$1 $2");
        }

        public void setNameIcon(int nameIcon) {
            this.nameIcon = nameIcon;
        }

        public void setLivello(int livello) {
            this.livello = livello;
        }

        public ArrayList<TypeInputGame> getInputType() {
            return inputType;
        }

        public void addInputType(TypeInputGame inputType) {
            this.inputType.add(inputType);
        }

        public List<Domanda> getListaDomandeGioco() {
            return domande;
        }

        public void setDomandaGioco(Domanda domanda) {
            this.domande.add(domanda);
        }

        public static class Domanda implements Serializable{
            public List<String> getLparole() {
                return lparole;
            }

            private  List<String> lparole;
            private String testoRacconto = null;

            public typeMedia getTypeMedia() {
                return media;
            }

            private typeMedia media = typeMedia.IMAGE;

            public void setTypeMedia(typeMedia media) {
                this.media = media;
            }

            public enum typeMedia {
                AUDIO, IMAGE
            }

            public String getTestoRacconto() {
                return testoRacconto;
            }

            public void setTestoRacconto(String testoRacconto) {
                this.testoRacconto = testoRacconto;
            }

            private String testoParola = "";
            private List<String> phatMedia = new ArrayList<>();
            private String testoDomanda = "";
            private List<String> listaRispose = new ArrayList<>();
            private short positionRispostaEsatta = -1;

            private Domanda(String testoDomanda, String testoParola, List<String> risposte,
                            short positionRispostaEsatta) {
                this.testoDomanda = testoDomanda;
                this.testoParola = testoParola;
                this.listaRispose = risposte;
                this.positionRispostaEsatta = positionRispostaEsatta;
                phatMedia = null;
            }

            private Domanda(String testoDomanda, String testoParola) {
                this.testoDomanda = testoDomanda;
                this.testoParola = testoParola;
                phatMedia = null;
            }

            private Domanda(String testoDomanda, String testoParola, List<String> risposte,
                            short positionRispostaEsatta, List<String> phatMedia) {
                this.testoDomanda = testoDomanda;
                this.listaRispose = risposte;
                this.testoParola = testoParola;

                this.positionRispostaEsatta = positionRispostaEsatta;
                this.phatMedia = phatMedia;
            }

            public List<String> getPhatMedia() {
                return phatMedia;
            }

            public String getTestoParola() {
                return testoParola;
            }

            public String getTestoDomanda() {
                return testoDomanda;
            }

            public List<String> getListaRispose() {
                return listaRispose;
            }

            public short getPositionRispostaEsatta() {
                return positionRispostaEsatta;
            }

           public void checkDomandaOnline(Context c, String response, Util.RicercaParoleListener l) {

                Util.esistenzaParola(c, response, l);

            }

            public void checkDomandaSimiliarita(Context c, String category,String word, Util.SimilaritaParoleListener l) {

                Util.similaritaParole(c, category, word, l);

            }

            public boolean chekResponse(String rispostaData) {

                return (listaRispose. get(positionRispostaEsatta).trim().equalsIgnoreCase(rispostaData.trim()));
            }
        }
    }

    /**
     * gioco Appartenenza
     */
    public static class Appartenenza extends Game implements Serializable{

        private final String titologioco;

        private HashMap<String, String> domande = new HashMap<>();

        public Appartenenza(String titoloGioco) {
            this.titologioco = titoloGioco;
            super.setNameIcon(R.drawable.appartenenza);
            super.setTitleCategory(this.getClass().getSimpleName());
            super.setTitleGame(titoloGioco);
        }

        public void setDomanda(String categoria, String parola) {

            domande.put(parola, categoria);
            String testoDomanda = "Appartiene alla categoria  " + titologioco.toUpperCase()
                    + "  la parola";
            List<String> listaRispose = new ArrayList<>();
            listaRispose.add("Sì");
            listaRispose.add("No");
            short positionRispostaEsatta = -1;

            if (categoria.equalsIgnoreCase(titologioco)) {
                positionRispostaEsatta = 0;
            } else {
                positionRispostaEsatta = 1;
            }

            Game.Domanda dom = new Game.Domanda(testoDomanda, parola.toUpperCase(), listaRispose,
                    positionRispostaEsatta);
            setDomandaGioco(dom);

        }

        /**
         * Ricorda che la chiave é la parola da mostrare e il valore é la categoria da
         * confrontare
         *
         * @return HashMap<String,String> key=paola, value=categoria
         */
        public HashMap<String, String> getDomande() {
            return domande;
        }
    }
    /**
     * gioco categorizzazione
     */
    public static class Categorizzazione extends Game implements Serializable{
        private final String titoloGioco;
        private final HashMap<String, String> domande = new HashMap<>();

        private final List<String> catRiferimento = new ArrayList<>();

        public Categorizzazione(String titoloGioco) {

            this.titoloGioco = titoloGioco;
            super.setTitleCategory(this.getClass().getSimpleName());
            super.setNameIcon(R.drawable.categorizzazione);
            super.setTitleGame(titoloGioco);
        }

        public void setDomanda(String rispostaCorretta, String parola) {
            domande.put(parola, rispostaCorretta.trim());

            String testoDomanda = "A quale categoria tra " + titoloGioco.toUpperCase() + " appartiene la parola";

            short positionRispostaEsatta = -1;

            for (int i = 0; i < catRiferimento.size(); i++) {
                if (catRiferimento.get(i).trim().equalsIgnoreCase(rispostaCorretta)) {
                    positionRispostaEsatta = (short) i;
                    break;
                }
            }
            Game.Domanda dom = new Game.Domanda(testoDomanda, parola.toUpperCase(), catRiferimento,
                    positionRispostaEsatta);
            setDomandaGioco(dom);

        }

        public HashMap<String, String> getDomande() {
            return domande;
        }

        public List<String> getCatRiferimento() {
            return catRiferimento;
        }

        public void addcatRiferimento(String category) {
            catRiferimento.add(category);
        }

    }

    /**
     * gioco combinazione lettere
     */
    public static class CombinazioniLettere extends Game implements Serializable{

        public List<String> getLetter() {
            return letter;
        }

        private final List<String> letter = new ArrayList<>();

        public CombinazioniLettere(String titoloGioco) {
            super.setTitleCategory(this.getClass().getSimpleName());
            super.setNameIcon(R.drawable.combinazionilettere);
            super.setTitleGame(titoloGioco);

        }

        public void setLettera(String lettere) {
            letter.add(lettere);
        }

        public void setDomandaGioco() {
            String testo = "Ricorda: per terminare il gioco basta dire STOP, FINE GIOCO oppure FERMA GIOCO.\n Forma quante piú parole possibili con queste lettere";
            String elencoLettere = "";
            for (String l : letter) {
                elencoLettere += l.toUpperCase() + "  ";
            }
            Domanda dom = new Domanda(testo, elencoLettere);
            setDomandaGioco(dom);
        }
    }
    public static class EsistenzaParole extends Game implements Serializable{

        private final List<String> parole = new ArrayList<>();

        public EsistenzaParole(String titoloGioco) {
            super.setTitleCategory(this.getClass().getSimpleName());
            super.setNameIcon(R.drawable.esistenzaparole);
            super.setTitleGame(titoloGioco);
        }

        public List<String> getParole() {
            return parole;
        }

        public void setParole(Context c, String parola) {

            parole.add(parola);

            String testoDomanda = "Esiste o Non esiste la parola";
            List<String> listaRispose = new ArrayList<>();
            listaRispose.add("ESISTE");
            listaRispose.add("NON ESISTE");

          /*  Util.esistenzaParola(c, parola, new Util.RicercaParoleListener() {
                @Override
                public void esiste() {
                    Game.Domanda dom = new Game.Domanda(testoDomanda, parola.toUpperCase(), listaRispose, (short) 0);
                    setDomandaGioco(dom);
                }

                @Override
                public void nonesiste() {
                    Game.Domanda dom = new Game.Domanda(testoDomanda, parola.toUpperCase(), listaRispose, (short) 1);
                    setDomandaGioco(dom);
                }
            });*/
        }
    }

    public static class FinaliParole extends Game implements Serializable{

        private final List<String> parole = new ArrayList<>();
        String titoloGioco;
        public FinaliParole(String titoloGioco) {
            super.setTitleCategory(this.getClass().getSimpleName());
            super.setNameIcon(R.drawable.finaliparole);
            this.titoloGioco=titoloGioco;
            super.setTitleGame(titoloGioco);

        }

        public List<String> getParole() {
            return parole;
        }

        public void setParole(String value) {
            parole.add(value);
            String testo = "Completa la parola che inizia con: ";
            Domanda dom = new Domanda(testo,value.toUpperCase());
            setDomandaGioco(dom);
        }
    }
    public static class FluenzeFonologiche extends Game implements Serializable{

        private final String titoloGioco;

        public FluenzeFonologiche(String titoloGioco) {
            super.setTitleCategory(this.getClass().getSimpleName());
            super.setNameIcon(R.drawable.fluenzefonologiche);
            super.setTitleGame(titoloGioco);
            this.titoloGioco=titoloGioco;

        }

        public void setDomandaGioco() {
            String testo = "Ricorda: per terminare il gioco basta dire STOP, FINE GIOCO oppure FERMA GIOCO.\n Forma quante piú parole possibili che iniziano con la lettera";
            Domanda dom = new Domanda(testo, titoloGioco.toUpperCase());
            setDomandaGioco(dom);
        }
    }

    public static class FluenzeSemantiche extends Game implements Serializable{

        private final String titoloGioco;
        private String categoria;

        public FluenzeSemantiche(String titoloGioco) {
            super.setTitleCategory(this.getClass().getSimpleName());
            super.setNameIcon(R.drawable.fluenzesemantiche);
            this.titoloGioco=titoloGioco;
            super.setTitleGame(titoloGioco);

        }

        public void setCategory(String categoria) {
            this.categoria = categoria;
        }

        public String getCategoria() {
            return categoria;
        }

        public void setDomandaGioco() {
            String testo = "Ricorda: per terminare il gioco basta dire STOP, FINE GIOCO oppure FERMA GIOCO.\n Indica piú parole possibili relative alla categoria";
            Domanda dom = new Domanda(testo, titoloGioco.toUpperCase());
            setDomandaGioco(dom);
        }
    }

    public static class FluenzeVerbali extends Game implements Serializable{
        private final String titoloGioco;

        private final List<String> parole = new ArrayList<>();

        public FluenzeVerbali(String titoloGioco) {
            super.setTitleCategory(this.getClass().getSimpleName());
            super.setNameIcon(R.drawable.fluenzeverbali);
            this.titoloGioco=titoloGioco;
            super.setTitleGame(titoloGioco);
        }

        public void setParole(String parola) {
            this.parole.add(parola);
            String testo = "Indica la prima cosa che ti viene in mente quanto senti la parola";
            Domanda dom = new Domanda(testo,parola.toUpperCase());
            setDomandaGioco(dom);
        }

        public List<String> getParole() {
            return parole;
        }
    }
    public static class LettereMancanti extends Game implements Serializable{

        private final String titologioco;
        private final List<String> parole = new ArrayList<>();

        public LettereMancanti(String titoloGioco) {
            this.titologioco = titoloGioco;
            super.setTitleCategory(this.getClass().getSimpleName());
            super.setNameIcon(R.drawable.letteremancanti);
            super.setTitleGame(titoloGioco);
        }

        public void setParole(String parola) {
            this.parole.add(parola);

            String testoDomanda = "Inserisci le lettere mancanti della  parola appartenente alla categoria  "
                    + titologioco.toUpperCase();

            List<String> listaRispose = new ArrayList<>();

            Random rand = new Random();

            int max = (parola.length() / 2);
            int min = 2;

            int maxCarDaTogliere = rand.nextInt((max - min) + 1) + min;// genero un massimo di caratteri da togliere

            List<Integer> posizioneRandomUscite = new ArrayList<>();

            // genero le posizione da togliere casualmente
            for (int i = 0; i < maxCarDaTogliere; i++) {
                int posizioneRandom;

                do {
                    posizioneRandom = (int) (Math.random() * parola.length() - 1);
                } while (posizioneRandomUscite.contains(posizioneRandom));

                posizioneRandomUscite.add(posizioneRandom);

            }

            // ordino le posizioni generate dal numero piú piccolo al piú grande
            Collections.sort(posizioneRandomUscite);

            String rispostaGiusta = "";

            StringBuffer sb = new StringBuffer(parola);

            // mi memorizzo i caratteri tolti dalla parola e li sostituisco con i trattini
            for (int i = 0; i < posizioneRandomUscite.size(); i++) {
                rispostaGiusta = rispostaGiusta + parola.charAt(posizioneRandomUscite.get(i));
                sb.setCharAt(posizioneRandomUscite.get(i), '_');
            }

            listaRispose.addAll(generaRisposte(3, rispostaGiusta));

            Collections.shuffle(listaRispose);

            short positionRispostaEsatta = (short) listaRispose.indexOf(rispostaGiusta);

            Log.i("ParoleMassimo",
                    "Parola: " + parola.toUpperCase() + " lung parole:  " + parola.length() + "  max da togliere:  "
                            + maxCarDaTogliere + " Paola generata: " + sb.toString().toUpperCase()
                            + " Risposta Giusta: " + rispostaGiusta + " Risposte: " + listaRispose.toString()
                            + "posizione rispo esatta" + positionRispostaEsatta

            );

            Game.Domanda dom = new Game.Domanda(testoDomanda, sb.toString().toUpperCase(), listaRispose,
                    positionRispostaEsatta);
            setDomandaGioco(dom);

        }

        private List<String> generaRisposte(int numeroDiStringeDaGenerare, String rispostaGiusta) {

            List<String> risposte = new ArrayList<>();
            risposte.add(rispostaGiusta.toUpperCase());

            final String LETTER_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            int lunghezzaMax = rispostaGiusta.length();

            int stringGenerate = numeroDiStringeDaGenerare;

            do {
                String generate = "";

                for (int i = 0; i < lunghezzaMax; i++) {
                    char c = LETTER_STRING.charAt((new Random()).nextInt(LETTER_STRING.length()));
                    generate += c;
                }
                if (!risposte.contains(generate)) {
                    stringGenerate--;
                    risposte.add(generate);
                }

            } while (stringGenerate > 0);

            return risposte;

        }

        public List<String> getParole() {
            return parole;
        }
    }

    public static class Mesi extends Game implements Serializable{

        public static class Domanda {
            private final List<String> risposteSbagliate = new ArrayList<>();
            private String testoDomanda = "";
            private String rispostaCorretta = "";

            public String getTestoDomanda() {
                return testoDomanda;
            }

            public void setTestoDomanda(String testoDomanda) {
                this.testoDomanda = testoDomanda;
            }

            public String getRispostaCorretta() {
                return rispostaCorretta;
            }

            public void setRispostaCorretta(String rispostaCorretta) {
                this.rispostaCorretta = rispostaCorretta;
            }

            public List<String> getRisposteSbagliate() {
                return risposteSbagliate;
            }

            public void setRispostaSbagliata(String rispostaSbagliata) {
                this.risposteSbagliate.add(rispostaSbagliata);
            }
        }

        public List<Domanda> getListaDomande() {
            return listaDomande;
        }

        public void setListaDomande(Domanda listaDomande) {
            this.listaDomande.add(listaDomande);

            String testoDomanda = listaDomande.testoDomanda;
            List<String> listaRispose = listaDomande.getRisposteSbagliate();

            listaRispose.add(listaDomande.rispostaCorretta);
            Collections.shuffle(listaRispose);

            int positionRispostaEsatta = listaRispose.indexOf(listaDomande.rispostaCorretta);
            Game.Domanda dom = new Game.Domanda(testoDomanda, "", listaRispose, (short) positionRispostaEsatta);
            setDomandaGioco(dom);
        }

        private final List<Domanda> listaDomande = new ArrayList<>();

        public Mesi(String titoloGioco) {
            super.setTitleCategory(this.getClass().getSimpleName());
            super.setNameIcon(R.drawable.mesi);
            super.setTitleGame(titoloGioco);

        }
    }
    public static class Musica extends Game implements Serializable{
        private final List<Musica.Domanda> listaDomande = new ArrayList<>();

        public List<Musica.Domanda> getListaDomande() {
            return listaDomande;
        }

        public void setListaDomande(Musica.Domanda listaDomande) {
            this.listaDomande.add(listaDomande);

            String testoDomanda = "Chi canta questa canzone?";
            List<String> listaRispose = listaDomande.getRisposteSbagliate();

            listaRispose.add(listaDomande.rispostaCorretta);
            Collections.shuffle(listaRispose);

            int positionRispostaEsatta = listaRispose.indexOf(listaDomande.rispostaCorretta);

            List<String> media = new ArrayList<>();
            media.add(listaDomande.getUrlMedia());

            Game.Domanda dom = new Game.Domanda(testoDomanda, "", listaRispose, (short) positionRispostaEsatta, media);
            dom.setTypeMedia(Game.Domanda.typeMedia.AUDIO);
            setDomandaGioco(dom);

        }

        public Musica(String titoloGioco) {
            super.setTitleGame(titoloGioco);
            super.setTitleCategory(this.getClass().getSimpleName());
            super.setNameIcon(R.drawable.musica);
        }

        public static class Domanda implements Serializable{
            private final List<String> risposteSbagliate = new ArrayList<>();
            private String testoDomanda = "";
            private String urlMedia = "";
            private String rispostaCorretta = "";

            public String getUrlMedia() {
                return urlMedia;
            }

            public void setUrlMedia(String urlMedia) {
                this.urlMedia = urlMedia;
            }

            public String getTestoDomanda() {
                return testoDomanda;
            }

            public void setTestoDomanda(String testoDomanda) {
                this.testoDomanda = testoDomanda;
            }

            public String getRispostaCorretta() {
                return rispostaCorretta;
            }

            public void setRispostaCorretta(String rispostaCorretta) {
                this.rispostaCorretta = rispostaCorretta;
            }

            public List<String> getRisposteSbagliate() {
                return risposteSbagliate;
            }

            public void setRispostaSbagliata(String rispostaSbagliata) {
                this.risposteSbagliate.add(rispostaSbagliata);
            }

        }
    }
    public static class Racconti extends Game implements Serializable{
        public List<Domanda> getListaDomande() {
            return listaDomande;
        }

        private final List<Racconti.Domanda> listaDomande = new ArrayList<>();

        public List<String> getListUrlsMedia() {
            return listUrlsMedia;
        }

        private final List<String> listUrlsMedia = new ArrayList<>();

        public String getTestoRacconto() {
            return testoRacconto;
        }

        public void setTestoRacconto(String testoRacconto) {
            this.testoRacconto = testoRacconto;
        }

        private String testoRacconto = null;

        public Racconti(String titoloGioco) {
            super.setTitleGame(titoloGioco);
            super.setTitleCategory(this.getClass().getSimpleName());
            super.setNameIcon(R.drawable.racconti);

        }

        public void setListaDomande(Racconti.Domanda listaDomande) {
            this.listaDomande.add(listaDomande);

            String testoDomanda = listaDomande.testoDomanda;
            List<String> listaRispose = listaDomande.getRisposteSbagliate();

            listaRispose.add(listaDomande.rispostaCorretta);
            Collections.shuffle(listaRispose);

            setTestoRacconto(getTestoRacconto());

            int positionRispostaEsatta = listaRispose.indexOf(listaDomande.rispostaCorretta);
            Game.Domanda dom = new Game.Domanda(testoDomanda, "", listaRispose, (short) positionRispostaEsatta);
            setDomandaGioco(dom);
        }

        public void setUrlsMedia(String value) {
            this.listUrlsMedia.add(value);
        }

        // ++++++++++++++++++++++++++++++++

        public static class Domanda implements Serializable{

            private String testoDomanda = "";
            private String rispostaCorretta = "";
            private final List<String> risposteSbagliate = new ArrayList<>();

            public String getTestoDomanda() {
                return testoDomanda;
            }

            public void setTestoDomanda(String testoDomanda) {
                this.testoDomanda = testoDomanda;
            }

            public String getRispostaCorretta() {
                return rispostaCorretta;
            }

            public void setRispostaCorretta(String rispostaCorretta) {
                this.rispostaCorretta = rispostaCorretta;
            }

            public List<String> getRisposteSbagliate() {
                return risposteSbagliate;
            }

            public void setRispostaSbagliata(String risposteSbagliate) {
                this.risposteSbagliate.add(risposteSbagliate);
            }
        }
    }

    public static class Volti extends Game implements Serializable{

        private final List<Volti.Domanda> listaDomande = new ArrayList<>();
        private String media = "";

        public List<Volti.Domanda> getListaDomande() {
            return listaDomande;
        }

        public void setListaDomande(Volti.Domanda listaDomande) {
            this.listaDomande.add(listaDomande);

            String testoDomanda = listaDomande.getTestoDomanda();
            List<String> listaRispose = listaDomande.getRisposteSbagliate();

            listaRispose.add(listaDomande.rispostaCorretta);

            Collections.shuffle(listaRispose);

            int positionRispostaEsatta = listaRispose.indexOf(listaDomande.rispostaCorretta);

            List<String> media = new ArrayList<>();
            media.add(getMedia());
            Game.Domanda dom = new Game.Domanda(testoDomanda, "", listaRispose, (short) positionRispostaEsatta, media);
            dom.setTypeMedia(Game.Domanda.typeMedia.IMAGE);
            setDomandaGioco(dom);

        }

        public Volti(String titoloGioco) {
            super.setTitleCategory(this.getClass().getSimpleName());
            super.setNameIcon(R.drawable.volti);
            super.setTitleGame(titoloGioco);

        }

        public String getMedia() {
            return media;
        }

        public void urlMedia(String media) {
            this.media = media;
        }

        public static class    Domanda implements Serializable{
            private final List<String> risposteSbagliate = new ArrayList<>();
            private String testoDomanda = "";
            private String rispostaCorretta = "";

            public String getTestoDomanda() {
                return testoDomanda;
            }

            public void setTestoDomanda(String testoDomanda) {
                this.testoDomanda = testoDomanda;
            }

            public String getRispostaCorretta() {
                return rispostaCorretta;
            }

            public void setRispostaCorretta(String rispostaCorretta) {
                this.rispostaCorretta = rispostaCorretta;
            }

            public List<String> getRisposteSbagliate() {
                return risposteSbagliate;
            }

            public void setRispostaSbagliata(String rispostaSbagliata) {
                this.risposteSbagliate.add(rispostaSbagliata);
            }

        }

    }
}