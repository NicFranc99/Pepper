package com.example.pepperapp28aprile.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.pepperapp28aprile.Persona;
import com.example.pepperapp28aprile.R;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    private static final float WORDS_SIMILARITY_THRESHOLD = .5F;

    public interface RicercaParoleListener {
        void esiste();

        void nonesiste();
    }

    public interface SimilaritaParoleListener {
        void valida(float value);
        void nonvalida(float value);
        void errore();

    }
    public static boolean isCodiceFiscale(final String input) {
        final Pattern pattern = Pattern.compile("[a-zA-Z]+[0-9]+[a-zA-Z][0-9]+[a-zA-Z][0-9]+[a-zA-Z]",
                Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    public static void showKyboard(Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public static void closeKeyboard(Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

//            Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), card.getImg());
//            RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getContext().getResources(), bitmap);
//            drawable.setAntiAlias(true);
//            drawable.setCornerRadius(Math.max(bitmap.getWidth(), bitmap.getHeight()) / 2.0f);
    // drawable.setCornerRadius(40f);

    /**
     * Converte il file json in stringa
     *
     * @param inputStream phat della risorsa
     * @return stringa del contenuto json
     */
    public static String inputStreamToString(InputStream inputStream) {
        try {
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes, 0, bytes.length);
            return new String(bytes);
        } catch (IOException e) {
            return null;
        }
    }

    public static void stampaLogDati(Persona paziente) {
        List<Persona.Game> listGame = paziente.getEsercizi();

        for (Persona.Game game : listGame) {

            if (game instanceof Persona.Appartenenza) {
                Persona.Appartenenza g = (Persona.Appartenenza) game;
                Log.i("Appartenenza", g.getTitleGame() + " Livello: " + g.getLivello() + " Domande"
                        + g.getDomande().toString() + "descrizione Gioco :" + g.getDescrizioneGioco());
            }
            if (game instanceof Persona.Categorizzazione) {
                Persona.Categorizzazione g = (Persona.Categorizzazione) game;

                String msg = g.getTitleGame() + " Livello: " + g.getLivello() + " Domande" + g.getDomande().toString()
                        + "descrizione Gioco :" + g.getDescrizioneGioco() +

                        "Category: " + Arrays.toString(g.getCatRiferimento().toArray());

                Log.i("Categorizzazione", msg);
            }

            if (game instanceof Persona.CombinazioniLettere) {
                Persona.CombinazioniLettere g = (Persona.CombinazioniLettere) game;
                String lettere = Arrays.toString(g.getLetter().toArray());

                Log.i("CombinazioniLettere", g.getTitleGame() + " Livello: " + g.getLivello() + "descrizione Gioco :"
                        + g.getDescrizioneGioco() + " lettere" + lettere);
            }

            if (game instanceof Persona.EsistenzaParole) {
                Persona.EsistenzaParole g = (Persona.EsistenzaParole) game;
                String parole = Arrays.toString(g.getParole().toArray());

                Log.i("EsistenzaParole", g.getTitleGame() + " Livello: " + g.getLivello() + "descrizione Gioco :"
                        + g.getDescrizioneGioco() + " parole" + parole);
            }
            if (game instanceof Persona.FinaliParole) {
                Persona.FinaliParole g = (Persona.FinaliParole) game;
                String parole = Arrays.toString(g.getParole().toArray());

                Log.i("FinaliParole", g.getTitleGame() + " Livello: " + g.getLivello() + "descrizione Gioco :"
                        + g.getDescrizioneGioco() + " parole" + parole);
            }

            if (game instanceof Persona.FluenzeFonologiche) {
                Persona.FluenzeFonologiche g = (Persona.FluenzeFonologiche) game;

                Log.i("FluenzeFonologiche", g.getTitleGame() + " Livello: " + g.getLivello() + "descrizione Gioco :"
                        + g.getDescrizioneGioco() + "");
            }
            if (game instanceof Persona.FluenzeSemantiche) {
                Persona.FluenzeSemantiche g = (Persona.FluenzeSemantiche) game;

                Log.i("FluenzeSemantiche", g.getTitleGame() + " Livello: " + g.getLivello() + "descrizione Gioco :"
                        + g.getDescrizioneGioco() + " Categoria: " + g.getCategoria());
            }
            if (game instanceof Persona.FluenzeVerbali) {
                Persona.FluenzeVerbali g = (Persona.FluenzeVerbali) game;
                String parole = Arrays.toString(g.getParole().toArray());

                Log.i("FluenzeVerbali", g.getTitleGame() + " Livello: " + g.getLivello() + "descrizione Gioco :"
                        + g.getDescrizioneGioco() + " parole: " + parole);
            }

            if (game instanceof Persona.LettereMancanti) {
                Persona.LettereMancanti g = (Persona.LettereMancanti) game;
                String parole = Arrays.toString(g.getParole().toArray());

                Log.i("LettereMancanti", g.getTitleGame() + " Livello: " + g.getLivello() + "descrizione Gioco :"
                        + g.getDescrizioneGioco() + " parole: " + parole);
            }

            if (game instanceof Persona.Mesi) {
                Persona.Mesi g = (Persona.Mesi) game;
                List<Persona.Mesi.Domanda> ldomande = g.getListaDomande();
                String stringdomanda = "";
                int i = 0;

                for (Persona.Mesi.Domanda domanda : ldomande) {
                    stringdomanda += "Domanda " + i + " :{ " + " Testo: " + domanda.getTestoDomanda()
                            + " RispostaCorretta: " + domanda.getRispostaCorretta() + " RisposteSbagliate: "
                            + Arrays.toString(domanda.getRisposteSbagliate().toArray()) + "}\n";
                    i++;
                }

                Log.i("Mesi", g.getTitleGame() + " Livello: " + g.getLivello() + "descrizione Gioco :"
                        + g.getDescrizioneGioco() +

                        " listaDomande: " + stringdomanda);
            }

            if (game instanceof Persona.Musica) {
                Persona.Musica g = (Persona.Musica) game;

                List<Persona.Musica.Domanda> ldomande = g.getListaDomande();
                String stringdomanda = "";
                int i = 0;

                for (Persona.Musica.Domanda domanda : ldomande) {
                    stringdomanda += "Domanda " + i + " :{ " + " Testo: " + domanda.getTestoDomanda() + " UrlMedia: "
                            + domanda.getUrlMedia() + " RispostaCorretta: " + domanda.getRispostaCorretta()
                            + " RisposteSbagliate: " + Arrays.toString(domanda.getRisposteSbagliate().toArray())
                            + "}\n";
                    i++;
                }

                Log.i("Musica", g.getTitleGame() + " Livello: " + g.getLivello() + "descrizione Gioco :"
                        + g.getDescrizioneGioco() +

                        " listaDomande: " + stringdomanda);
            }

            if (game instanceof Persona.Racconti) {
                Persona.Racconti g = (Persona.Racconti) game;

                String stringdomanda = "";
                List<Persona.Racconti.Domanda> ldomande = g.getListaDomande();

                int i = 0;
                for (Persona.Racconti.Domanda domanda : ldomande) {
                    stringdomanda += "Domanda " + i + " :{ " + " Testo: " + domanda.getTestoDomanda()
                            + " RispostaCorretta: " + domanda.getRispostaCorretta() + " RisposteSbagliate: "
                            + Arrays.toString(domanda.getRisposteSbagliate().toArray()) + "}\n";
                    i++;
                }

                String stringMedia = Arrays.toString(g.getListUrlsMedia().toArray());

                Log.i("Racconti", g.getTitleGame() + " Livello: " + g.getLivello() + "descrizione Gioco :"
                        + g.getDescrizioneGioco() +

                        " Testo Racconto: " + g.getTestoRacconto() + "\n" + " ListaDomande: " + stringdomanda
                        + " ListaMedia: " + stringMedia);
            }

            if (game instanceof Persona.Volti) {
                Persona.Volti g = (Persona.Volti) game;
                List<Persona.Volti.Domanda> ldomande = g.getListaDomande();
                String stringdomanda = "";
                int i = 0;

                for (Persona.Volti.Domanda domanda : ldomande) {
                    stringdomanda += "Domanda " + i + " :{ " + " Testo: " + domanda.getTestoDomanda()
                            + " RispostaCorretta: " + domanda.getRispostaCorretta() + " RisposteSbagliate: "
                            + Arrays.toString(domanda.getRisposteSbagliate().toArray()) + "}\n";
                    i++;
                }

                Log.i("Volti", g.getTitleGame() + " Livello: " + g.getLivello() + "descrizione Gioco :"
                        + g.getDescrizioneGioco() +

                        " Media: " + g.getMedia() + "\n listaDomande: " + stringdomanda);
            }

        }
    }

    /**
     * ritorna il valore di un elemento presente in quella categoria
     *
     * @param c         context applicazione
     * @param categoria nome categoria
     * @param elemento  elemento da ricercare nel file
     * @return il valore dell'elemento dato
     */
    public static String getElementJSON(Context c, String categoria, String elemento) throws ExceptionCategoryNotFount {
        String datidafile = Util.inputStreamToString(c.getResources().openRawResource(R.raw.dati));
        JsonElement app = new JsonParser().parse(datidafile).getAsJsonObject().get(categoria);

        String value = "";
        if (app == null) {
            throw new ExceptionCategoryNotFount("La Categoria: " + categoria + " non presente new File Json");
        } else {
            JsonElement element = app.getAsJsonObject().get(elemento);
            if (element == null) {
                throw new ExceptionCategoryNotFount(
                        "L'elemento: " + elemento + " della categoria: " + categoria + " NON é presente nel File Json");

            } else {
                value = element.getAsString();
            }
        }
        return value;
    }

    public static void circleImage(Context c, ImageView imgvire, int icon) {

        Bitmap bitmap = BitmapFactory.decodeResource(c.getResources(), icon);
        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(c.getResources(), bitmap);
        drawable.setAntiAlias(true);
        drawable.setCircular(true);
        drawable.setCornerRadius(Math.max(bitmap.getWidth(), bitmap.getHeight()) / 2.0f);
        // drawable.setCornerRadius(40f);

        imgvire.setImageDrawable(drawable);
    }
//
//    public static boolean esistenzaParola(Context c, String parola, RicercaParoleListener listener) {
//        boolean risultato = false;
//
//        RequestQueue queue = Volley.newRequestQueue(c);
//
//        // String url =" https://it.wikipedia.org/api/rest_v1/page/summary/"+parola;
//        String url = c.getResources().getString(R.string.URL_dictionary) + parola;
//        // Request a string response from the provided URL.
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
//
//            Log.i("RISPOSTA URL", "Parola: " + parola.toLowerCase());
//
//            Log.i("RISPOSTA URL", " Response is: " + response);
//            listener.esiste();
//
//        }, error -> {
//            Log.i("RISPOSTA URL", "Parola: " + parola.toUpperCase());
//            Log.i("RISPOSTA URL", "Non Esiste");
//            listener.nonesiste();
//        });
//
//// Add the request to the RequestQueue.
//        queue.add(stringRequest);
//
//        return risultato;
//    }
//


    public static void esistenzaParola(Context c, String parola , RicercaParoleListener l){
        RequestQueue queue = Volley.newRequestQueue(c);
        String url = c.getResources().getString(R.string.URLWORDNETexist);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("RISPOSTA",response);
                        JsonElement app = new JsonParser().parse(response).getAsJsonObject().get("wordExistence");
                        Boolean f= Boolean.parseBoolean(app.getAsString());

                        if(f){
                            l.esiste();
                        }else{
                            l.nonesiste();
                        }

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("RISPOSTA",error.toString());
                        l.nonesiste();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("word", parola);
                return params;
            }

        };
        queue.add(stringRequest);

    }



    public static void similaritaParole(Context c, String categoria, String parola , SimilaritaParoleListener l){
        RequestQueue queue = Volley.newRequestQueue(c);
        String url = c.getResources().getString(R.string.URLWORDNET);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("RISPOSTA",response);
                        JsonElement app = new JsonParser().parse(response).getAsJsonObject().get("wordSimilarity");
                        Float f= Float.parseFloat(app.getAsString());

                        if(f>WORDS_SIMILARITY_THRESHOLD){
                            l.valida(f);
                        }else{
                            l.nonvalida(f);
                        }

                    }
                },
                
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("RISPOSTA",error.toString());
                        l.errore();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("category", categoria);
                params.put("word", parola);
                return params;
            }

        };
        queue.add(stringRequest);

    }

    public static String milliSecondsToTimer(long milliSeconds) {
        String timerString = "";
        String secondsString;
        int ore = (int) (milliSeconds / (1000 * 60 * 60));
        int minuti = (int) (milliSeconds % (1000 * 60 * 60)) / (1000 * 60);
        int secondi = (int) ((milliSeconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);

        if (ore > 0) {
            timerString = ore + ":";
        }
        if (secondi < 10) {
            secondsString = "0" + secondi;
        } else {
            secondsString = "" + secondi;
        }
        timerString = timerString + minuti + ":" + secondsString;
        return timerString;
    }

}
