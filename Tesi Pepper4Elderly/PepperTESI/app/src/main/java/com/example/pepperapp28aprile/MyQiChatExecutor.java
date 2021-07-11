package com.example.pepperapp28aprile;


import android.util.Log;
import android.view.View;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.builder.AnimateBuilder;
import com.aldebaran.qi.sdk.builder.AnimationBuilder;
import com.aldebaran.qi.sdk.object.actuation.Animate;
import com.aldebaran.qi.sdk.object.actuation.Animation;
import com.aldebaran.qi.sdk.object.conversation.BaseQiChatExecutor;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

class MyQiChatExecutor extends BaseQiChatExecutor {
    private final QiContext qiContext;
    public static CheckMattutino mainActivity;
    public static String idBed;

    MyQiChatExecutor(QiContext context) {
        super(context);
        this.qiContext = context;
    }

    @Override
    public void runWith(List<String> params) {
        // This is called when execute is reached in the topic
        //animate(qiContext);
        switch (params.get(0)){
            case "operatore" : {
                try{
                    URL urlSetFalse = new URL("https://bettercallpepper.altervista.org/api/callOperatore.php"+
                            "/?room="+idBed);
                    HttpURLConnection conn = null;
                    conn = (HttpURLConnection) urlSetFalse.openConnection();
                    conn.setRequestMethod("GET");
                    conn.getInputStream();
                }  catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            case "interrompi" : {
                mainActivity.runOnUiThread(() -> {
                    NavigationFragment.domanda.setText("Vuoi interrompere il questionario?");
                    NavigationFragment.risposta.setText("Sì - No");
                });
                break;
            }
            case "introduce" : {
                mainActivity.runOnUiThread(() -> {
                    NavigationFragment.domanda.setText("Sono qui per il questionario");
                    NavigationFragment.risposta.setText("");
                });
                break;
            }
            case "joke" : {
                mainActivity.runOnUiThread(() -> {
                    NavigationFragment.domanda.setText("Facciamoci due risate!");
                    NavigationFragment.risposta.setText(" :-) ");
                });
                break;
            }
            case "whatelse" : {
                mainActivity.runOnUiThread(() -> {
                    NavigationFragment.domanda.setText("Cos'altro posso fare per te?");
                    NavigationFragment.risposta.setText("'Operatore' - 'Barzelletta' - 'Telepresenza' - 'Puoi andare'");
                });
                break;
            }
            case "cosafare" : {
                mainActivity.runOnUiThread(() -> {
                    NavigationFragment.domanda.setText("Cosa posso fare per te?");
                    NavigationFragment.risposta.setText("'Operatore' - 'Barzelletta' - 'Telepresenza' - 'Puoi andare'");
                });
                break;
            }
            case "abilities" : {
                mainActivity.runOnUiThread(() -> {
                    NavigationFragment.domanda.setText("Posso videochiamare un contatto o far venire l'operatore,");
                    NavigationFragment.risposta.setText("oppure intrattenerti con una barzelletta!");
                });
                break;
            }
            case "startCheck" : {
                mainActivity.runOnUiThread(() -> {
                    NavigationFragment.domanda.setText("Stiamo per cominciare,");
                    NavigationFragment.risposta.setText("se non capisci una domanda dimmelo pure!");
                });
                break;
            }
            case "print1" : {
                mainActivity.runOnUiThread(() -> {
                    NavigationFragment.domanda.setText("Sei ben riposato?");
                    NavigationFragment.risposta.setText("Sì - No");
                });
                break;
            }
            case "print2" : {
                mainActivity.runOnUiThread(() -> {
                    NavigationFragment.domanda.setText("Sereno e di buon umore?");
                    NavigationFragment.risposta.setText("Sì - No");
                });
                break;
            }
            case "print3" : {
                mainActivity.runOnUiThread(() -> {
                    NavigationFragment.domanda.setText("Dolori fisici?");
                    NavigationFragment.risposta.setText("Sì - No");
                });
                break;
            }
            case "print4" : {
                mainActivity.runOnUiThread(() -> {
                    NavigationFragment.domanda.setText("Triste o scoraggiato?");
                    NavigationFragment.risposta.setText("Sì - No");
                });
                break;
            }
            case "print5" : {
                mainActivity.runOnUiThread(() -> {
                    NavigationFragment.domanda.setText("Nervoso?");
                    NavigationFragment.risposta.setText("Sì - No");
                });
                break;
            }
            case "print6" : {
                mainActivity.runOnUiThread(() -> {
                    NavigationFragment.domanda.setText("Felice?");
                    NavigationFragment.risposta.setText("Molto - Abbastanza - Poco - Per nulla");
                });
                break;
            }
            case "print7" : {
                mainActivity.runOnUiThread(() -> {
                    NavigationFragment.domanda.setText("Annoiato?");
                    NavigationFragment.risposta.setText("Sì - No");
                });
                break;
            }
            case "print8" : {
                mainActivity.runOnUiThread(() -> {
                    NavigationFragment.domanda.setText("Tranquillo?");
                    NavigationFragment.risposta.setText("Sì - No");
                });
                break;
            }
            case "print9" : {
                mainActivity.runOnUiThread(() -> {
                    NavigationFragment.domanda.setText("Teso?");
                    NavigationFragment.risposta.setText("Sì - No");
                });
                break;
            }
            case "print10" : {
                mainActivity.runOnUiThread(() -> {
                    NavigationFragment.domanda.setText("Energico?");
                    NavigationFragment.risposta.setText("Sì - No");
                });
                break;
            }
            case "telepresenza" : {
                NavigationFragment.startTele = true;
                break;
            }
            case "noQuestionario" : {
                System.out.println("noQuestionario");
                NavigationFragment.questionario.put("noQuestionario","noQuestionario");
                break;
            }
            case "domanda1" : {

                System.out.println("domanda1" + params.get(1));
                NavigationFragment.questionario.put("domanda1",params.get(1));
                break;
            }
            case "domanda2" : {
                System.out.println("domanda2" + params.get(1));
                NavigationFragment.questionario.put("domanda2",params.get(1));
                break;
            }
            case "domanda3" : {
                System.out.println("domanda3" + params.get(1));
                NavigationFragment.questionario.put("domanda3",params.get(1));
                break;
            }
            case "domanda4" : {
                System.out.println("domanda4" + params.get(1));
                NavigationFragment.questionario.put("domanda4",params.get(1));
                break;
            }
            case "domanda5" : {
                System.out.println("domanda5" + params.get(1));
                NavigationFragment.questionario.put("domanda5",params.get(1));
                break;
            }
            case "domanda6" : {
                System.out.println("domanda6" + params.get(1));
                NavigationFragment.questionario.put("domanda6",params.get(1));
                break;
            }
            case "domanda7" : {
                System.out.println("domanda7" + params.get(1));
                NavigationFragment.questionario.put("domanda7",params.get(1));
                break;
            }
            case "domanda8" : {
                System.out.println("domanda8" + params.get(1));
                NavigationFragment.questionario.put("domanda8",params.get(1));
                break;
            }
            case "domanda9" : {
                System.out.println("domanda9" + params.get(1));
                NavigationFragment.questionario.put("domanda9",params.get(1));
                break;
            }
            case "domanda10" : {
                System.out.println("domanda10" + params.get(1));
                NavigationFragment.questionario.put("domanda10",params.get(1));
                break;
            }
        }
    }

    @Override
    public void stop() {
        // This is called when chat is canceled or stopped
        Log.i("MyQiChatExecutor", "QiChatExecutor stopped");
    }

}

