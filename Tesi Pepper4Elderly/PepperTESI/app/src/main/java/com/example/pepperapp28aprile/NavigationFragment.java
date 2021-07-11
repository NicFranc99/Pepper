package com.example.pepperapp28aprile;

import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Parcel;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.fragment.NavHostFragment;

import com.airbnb.lottie.LottieAnimationView;
import com.aldebaran.qi.Future;
import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.builder.ChatBuilder;
import com.aldebaran.qi.sdk.builder.ListenBuilder;
import com.aldebaran.qi.sdk.builder.PhraseSetBuilder;
import com.aldebaran.qi.sdk.builder.QiChatbotBuilder;
import com.aldebaran.qi.sdk.builder.SayBuilder;
import com.aldebaran.qi.sdk.builder.TopicBuilder;
import com.aldebaran.qi.sdk.object.actuation.AttachedFrame;
import com.aldebaran.qi.sdk.object.actuation.Frame;
import com.aldebaran.qi.sdk.object.actuation.OrientationPolicy;
import com.aldebaran.qi.sdk.object.conversation.Chat;
import com.aldebaran.qi.sdk.object.conversation.Chatbot;
import com.aldebaran.qi.sdk.object.conversation.Listen;
import com.aldebaran.qi.sdk.object.conversation.ListenResult;
import com.aldebaran.qi.sdk.object.conversation.Phrase;
import com.aldebaran.qi.sdk.object.conversation.PhraseSet;
import com.aldebaran.qi.sdk.object.conversation.QiChatExecutor;
import com.aldebaran.qi.sdk.object.conversation.QiChatbot;
import com.aldebaran.qi.sdk.object.conversation.Say;
import com.aldebaran.qi.sdk.object.conversation.Topic;
import com.aldebaran.qi.sdk.object.geometry.Transform;
import com.aldebaran.qi.sdk.object.locale.Language;
import com.aldebaran.qi.sdk.object.locale.Locale;
import com.aldebaran.qi.sdk.object.locale.Region;
import com.aldebaran.qi.sdk.util.PhraseSetUtil;
import com.example.pepperapp28aprile.map.GoToHelper;
import com.example.pepperapp28aprile.map.RobotHelper;
import com.example.pepperapp28aprile.request.EmergencyRequest;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import com.example.pepperapp28aprile.models.*;
import com.example.pepperapp28aprile.request.core.RequestListener;
import com.example.pepperapp28aprile.request.core.*;
import static com.example.pepperapp28aprile.Globals.myAppID;

/*
import it.uniba.di.sysag.pepper4rsa.utils.map.GoToHelper;
import it.uniba.di.sysag.pepper4rsa.utils.map.PointsOfInterestView;
import it.uniba.di.sysag.pepper4rsa.utils.models.Emergency;
import it.uniba.di.sysag.pepper4rsa.utils.request.EmergencyRequest;
import it.uniba.di.sysag.pepper4rsa.utils.request.core.RequestException;
import it.uniba.di.sysag.pepper4rsa.utils.request.core.RequestListener;*/

public class NavigationFragment extends Fragment implements EmergencyListener {
    public static final String FRAGMENT_TAG = "navigation_fragment";
    private static final String MAP_FRAME = "mapFrame";

    private MainActivity ma;
    private CheckMattutino mainActivity;
    private RobotHelper robotHelper;
    private QiContext qiContext;
    private ScheduledExecutorService scheduledExecutorService;

    private Emergency emergency;
    private TreeMap<String, AttachedFrame> savedLocations;

    private EmergencyListener emergencyListener;

    private TextView gotoText;
    public static TextView risposta;
    public static TextView domanda;
    private LottieAnimationView gotoLoader;
    private TextView gotoFinishedText;
    private Frame robotFrame;
    private Frame mapFrame;
    private List<PointF> poiPositions;
    private String label;
    private Button btnStopNavigation;
    public static boolean end;
    private boolean arrivatoDestinazione;
    public static boolean startTele;

    private Chat chat;
    private Stack<String> topics = new Stack<>();

    private Boolean atMapFrame = false;

    private Boolean stopGoToHuman = false;

    public static final HashMap<String, Integer> topicsMap = new HashMap<>();
    public static final HashMap<String, String> questionario = new HashMap<>();

    public static boolean free;
    public static boolean bottone;
    public static ArrayList<Emergency> buttonEmergency;

    public static ArrayList<Emergency> allTheChecks;
    public static boolean tornaACasa;

  /*  static{
        topicsMap.put("lux-", R.string.lux_minus_say);
        topicsMap.put("lux+", R.string.lux_plus_say);
        topicsMap.put("voc+", R.string.voc_plus_say);
        topicsMap.put("degree-", R.string.degree_minus_say);
        topicsMap.put("degree+", R.string.degree_plus_say);
        topicsMap.put("humidity+", R.string.humidity_plus_say);
    }*/


    public NavigationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allTheChecks = checksFromServer();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.mainActivity = (CheckMattutino) getActivity();
        this.ma = mainActivity.getMainActivity();
        end = false;
        System.out.println("end settato false");

        /***TEST***/
        /*View fragmentLayout = inflater.inflate(R.layout.fragment_navigation, container, false);

        return fragmentLayout;*/


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        View view = inflater.inflate(R.layout.fragment_navigation, container, false);
        System.out.println("ON CREATE VIEW NAVIGATION");

        risposta = view.findViewById(R.id.textYesOrNo);
        risposta.setVisibility(View.GONE);
        domanda = view.findViewById(R.id.question);
        domanda.setVisibility(View.GONE);
        gotoText = view.findViewById(R.id.goto_text);
        gotoText.setVisibility(View.GONE);
        gotoLoader = view.findViewById(R.id.goto_loader);
        gotoLoader.setVisibility(View.GONE);
        gotoFinishedText = view.findViewById(R.id.goto_finished_text);

        btnStopNavigation = view.findViewById(R.id.stop_navigation_button);

        btnStopNavigation.setVisibility(View.GONE);
        btnStopNavigation.setOnClickListener(v -> {
            stopGoToHuman = true;
            mainActivity.getRobotHelper().goToHelper.checkAndCancelCurrentGoto();
        });

        if(mainActivity.getQiContext()==null){
            System.out.println("Exception grave");
        } else{
            // Retrieve the robot Frame
            robotFrame = (mainActivity.getQiContext().getActuationAsync()).getValue().async().robotFrame().getValue();

            // Retrieve the origin of the map Frame
            mapFrame = (mainActivity.getQiContext().getMappingAsync()).getValue().async().mapFrame().getValue();
        }
        /***la seguente dovrebbe essere la parte di kotlin***/
       /* PointsOfInterestView explorationMapView = view.findViewById(R.id.explorationMapViewPopup);

        mainActivity.getRobotHelper().localizeAndMapHelper.buildStreamableExplorationMap().andThenConsume(value -> {
            poiPositions = new ArrayList<>(mainActivity.getSavedLocations().size() + 1);
            for (Map.Entry<String, AttachedFrame> stringAttachedFrameEntry : mainActivity.getSavedLocations().entrySet()) {
                Transform transform = (((stringAttachedFrameEntry.getValue()).async().frame()).getValue().async().computeTransform(mapFrame)).getValue().getTransform();
                poiPositions.add(new PointF(((float) transform.getTranslation().getX()), (float) transform.getTranslation().getY()));
            }

            explorationMapView.setExplorationMap(value.getTopGraphicalRepresentation());
            explorationMapView.setMapFramPosition();
            explorationMapView.setPoiPositions(poiPositions);
        }).andThenConsume(value -> {
            int delay = 0;
            int period = 500;  // repeat every sec.
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                public void run() {
                    // Compute the position of the robot relatively to the map Frame
                    Transform robotPos = robotFrame.computeTransform(mapFrame).getTransform();
                    // Set the position in the ExplorationMapView widget, it will be displayed as a red circle
                    explorationMapView.setRobotPosition(robotPos);
                }
            }, delay, period);
        });

        */
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        this.emergencyListener = this;
        //Request routine for emergency check
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
       /* scheduledExecutorService.scheduleAtFixedRate(() -> {
            EmergencyRequest emergencyRequest = new EmergencyRequest();
            emergencyRequest.getNext(new RequestListener<Emergency>() {
                @Override
                public void successResponse(Emergency response) {
                    if ((response == null) && (emergency != null)) {

                    }
                    else if (response != null && emergencyListener != null) {
                        emergencyListener.onNewEmergency(response);
                    }
                }

                @Override
                public void errorResponse(RequestException error) {
                    Log.d(CheckMattutino.CONSOLE_TAG, error.getMessage());
                }
            });
        }, 0, 1, TimeUnit.MINUTES); */


        /***original***/
        /*scheduledExecutorService.scheduleAtFixedRate(() -> {
           Emergency response = getNextEmergency();
           if((response == null) && (emergency != null)){
               emergency = null;
               goToLocation(MAP_FRAME, null);
           }
           else if (response != null && emergencyListener != null) {
               emergencyListener.onNewEmergency(response);
               setDoneEmergency();
           }
        }, 0, 1, TimeUnit.MINUTES);*/

        if(tornaACasa){
            goToEnd();
            tornaACasa = false;
            bottone = false;
            Intent intent = new Intent(getActivity(),MainActivity.class);
            startActivity(intent);
        }




        free = true;
        if(bottone){
            System.out.println("BOTTONE PREMUTO, SONO NELL'IF BOTTONE");
            System.out.println("LISTA DELLE EMERGENZE BOTTONE:" + buttonEmergency);

            Emergency singleButtonEmergency = getNextButtonEmergency();
            startTele = false;
            while(singleButtonEmergency != null && !startTele){
                System.out.println(buttonEmergency);
                free = false;
                if (singleButtonEmergency != null && emergencyListener != null) {
                    emergencyListener.onNewEmergency(singleButtonEmergency);
                    System.out.println("bed label " + singleButtonEmergency.getBedLabel());
                    while (!free){ }
                    System.out.println("START TELE = " + startTele);
                    if(!startTele){
                        setDoneButtonEmergency();
                        singleButtonEmergency = getNextButtonEmergency();
                        System.out.println("singleButtonEmergency:" + singleButtonEmergency);
                    }
                }

            }
            if(startTele) {
                System.out.println("ENTRATO IN START TELE");
                setDoneButtonEmergency();


                System.out.println("IL CCCCCCCC " + emergency.getElderId());
                myAppID = Integer.parseInt(emergency.getElderId());
                ProfileActivity.name = emergency.getName();
                ProfileActivity.tornaNav = true;
                Intent intent = new Intent(getActivity(),ProfileActivity.class);
                startActivity(intent);
             } else{
                goToEnd();
                //goToLocationTester(MAP_FRAME, null, null);
                bottone = false;
                Intent intent = new Intent(getActivity(),MainActivity.class);
                startActivity(intent);
            }
        }
        else{
            /***mio prova tour completo***/
            /***cosa fa: controlla ogni secondo, se libero si dirige a nuova dest ***/
            scheduledExecutorService.scheduleAtFixedRate(() -> {

                System.out.println("prova" + free);
                if(free){
                    Emergency response = getNextEmergency();
                    if(getNextEmergency() == null){
                        System.out.println("GO TO MAP FRAMEEEEEEEEEEE");
                        emergency = null;
                        goToEnd();
                        //goToLocationTester(MAP_FRAME, null,null);




                       Intent intent = new Intent(getActivity(),MainActivity.class);
                        startActivity(intent);
                        scheduledExecutorService.shutdown();

                        /***SE TORNA AL MAP FRAME SIGNIFICA CHE HA FINITO IL TOUR***/
                    }
                    System.out.println("next emerg (response)" + response.getName());
                    System.out.println("emerg listener:"+ emergencyListener);
                    System.out.println("emerg:"+ emergencyListener);
                    if((response == null) && (emergency != null)){
                        System.out.println("map frame");
                        emergency = null;
                        goToLocation(MAP_FRAME, null);
                        //goToLocationTester(MAP_FRAME, null, null);
                    }
                    else if (response != null && emergencyListener != null) {
                        free = false;
                        System.out.println("new emergency");
                        emergencyListener.onNewEmergency(response);

                        setDoneEmergency();
                    }
                }
            }, 0, 2, TimeUnit.SECONDS);

        }
    }

    private Emergency getNextButtonEmergency(){

        try{
            for(int i=0; i<buttonEmergency.size(); i++)
                if(buttonEmergency.get(i) == null)
                    continue;
                else if(buttonEmergency.get(i).getDone() == false)
                    return buttonEmergency.get(i);
        }catch (Exception e){ System.out.println("exception get next button emergency"); }
        return null;
    }

    private void setDoneButtonEmergency(){

       /* for(int i=0; i<buttonEmergency.size(); i++)
            if(buttonEmergency.get(i).getDone() == false){
                buttonEmergency.get(i).setDone(true);
                return;
            }
*/


        try{
            for(int i=0; i<buttonEmergency.size(); i++)
                if(buttonEmergency.get(i) == null)
                    continue;
                else if(buttonEmergency.get(i).getDone() == false){
                    buttonEmergency.get(i).setDone(true);
                return;
            }
        }catch (Exception e){ System.out.println("exception set done button emergency"); }

        return;
    }

    private Emergency getNextEmergency(){

        for(int i=0; i<allTheChecks.size(); i++)
            if(allTheChecks.get(i).getDone() == false)
                return allTheChecks.get(i);

        return null;
    }

    private void setDoneEmergency(){

        for(int i=0; i<allTheChecks.size(); i++)
            if(allTheChecks.get(i).getDone() == false){
                allTheChecks.get(i).setDone(true);
                return;
            }
    }

    private ArrayList<Emergency> checksFromServer(){
        ArrayList<Emergency> list = mainActivity.getAnzianiList();
        /*ArrayList<Emergency> test = new ArrayList<>();
        test.add(list.get(1));
        return test;*/
        return list;
    }

  /*  private void chiamaMain(){
        ma.onDestroy();
        System.out.println("chiama Main");
        Intent intent = new Intent(getActivity(),CheckMattutino.class);
        startActivity(intent);
    }*/

    @Override
    public void onAttach(@NonNull Context context) {
       super.onAttach(context);
        if(context instanceof CheckMattutino){
            mainActivity = (CheckMattutino) context;
            robotHelper = mainActivity.getRobotHelper();
            qiContext = mainActivity.getQiContext();
            savedLocations = mainActivity.getSavedLocations();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        scheduledExecutorService.shutdownNow();
    }

    /**
     * Init the listeners for robot movement
     */
    private void registerListener(){
        robotHelper.goToHelper.addOnStartedMovingListener(() -> mainActivity.runOnUiThread(() -> {
            Log.d(CheckMattutino.CONSOLE_TAG, "Movement started");
            mainActivity.runOnUiThread(() -> {
                gotoText.setVisibility(View.VISIBLE);
                btnStopNavigation.setVisibility(View.VISIBLE);
                if(emergency == null){
                    gotoText.setText("Ritorno al map_frame");
                }
                else {
                    if (emergency.getType() == 0) {
                        gotoText.setText("Sto andando a..." + emergency.getEnvData().getRoom().getName());
                    } else {
                        gotoText.setText("Sto andando a..." + emergency.getBedLabel());
                    }
                }
                gotoLoader.setVisibility(View.VISIBLE);
            });
            robotHelper.goToHelper.removeOnStartedMovingListeners();
        }));

        robotHelper.goToHelper.addOnFinishedMovingListener((goToStatus) -> {
            mainActivity.runOnUiThread(() -> {
                gotoText.setVisibility(View.GONE);
                gotoLoader.setVisibility(View.GONE);
                btnStopNavigation.setVisibility(View.GONE);
            });

            if(goToStatus == GoToHelper.GoToStatus.FINISHED) {
                robotHelper.releaseAbilities().andThenConsume(value -> {
                    Log.d(CheckMattutino.CONSOLE_TAG, "Navigation Finished");
                    arrivatoDestinazione = true;
                    if (emergencyListener == null) {
                        if(emergency != null) {

                            Thread thread = new Thread(() -> handleEmergency(emergency));
                            thread.start();
                        }
                    }
                });
            }
            else if(goToStatus == GoToHelper.GoToStatus.CANCELLED){
                Log.d(CheckMattutino.CONSOLE_TAG, "Navigation Cancelled");
                if(stopGoToHuman) {
                    stopGoToHuman = false;
                    mainActivity.getSupportFragmentManager().popBackStack(NavigationFragment.FRAGMENT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    robotHelper.say("Navigazione cancellata dall'umano");
                }
                else{
                    robotHelper.say("Navigazione cancellata");
                }
            }
            else{
                Log.d(CheckMattutino.CONSOLE_TAG, "Navigation Failed");
                robotHelper.say("Navigazione fallita");
            }

            robotHelper.goToHelper.removeOnFinishedMovingListeners();
        });
    }


    public void goToEnd() {
        end = false;
        arrivatoDestinazione = false;

        String locationLabel = "MAP_FRAME";
        AttachedFrame location = null;
        this.label = locationLabel;
        Log.d(CheckMattutino.CONSOLE_TAG, "goToLocation: " + locationLabel);
        registerListener();
        robotHelper.goToHelper.checkAndCancelCurrentGoto().thenConsume(aVoid -> {
            robotHelper.holdAbilities(true);
            robotHelper.goToHelper.goToMapFrame(false, false, OrientationPolicy.FREE_ORIENTATION);
            atMapFrame = false;
        });

        while(!arrivatoDestinazione){
            //System.out.println("not at End");
        }

        end = true;

        if(bottone){
            System.out.println("NAVIGATION FRAGMENT : Chiamata del pulsante gestita!");
            // Create a phrase.
           /* Phrase phrase = new Phrase("Chiamata del pulsante gestita!");
            // Build the action.
            Say say = SayBuilder.with(qiContext)
                    .withPhrase(phrase)
                    .build();
            // Run the action synchronously.
            say.run();*/
        }
        else if(!tornaACasa){
            // Create a phrase.
            Phrase phrase = new Phrase("Check mattutino completato!");
            // Build the action.
            Say say = SayBuilder.with(qiContext)
                    .withPhrase(phrase)
                    .build();
            // Run the action synchronously.
            say.run();
        }

    }


    public void goToLocationTester(String locationLabel, AttachedFrame location, Emergency e){
        System.out.println("im going to " + locationLabel);
        System.out.println("locationLabel : " + locationLabel + " MAP_FRAME : " + MAP_FRAME);
        emergencyListener = this;
       // emergencyListener.onEmergencyHandled();
        //this.free = true;

        if(locationLabel.equals(MAP_FRAME)){
            end = true;
            System.out.println("end settato true");
        }
        else handleEmergency(e);

    }
    /**
     * Send the robot to the desired position.
     */
    public void goToLocation(String locationLabel, AttachedFrame location) {
        this.label = locationLabel;
        Log.d(CheckMattutino.CONSOLE_TAG, "goToLocation: " + locationLabel);
        registerListener();
        robotHelper.goToHelper.checkAndCancelCurrentGoto().thenConsume(aVoid -> {
            robotHelper.holdAbilities(true);
            if (locationLabel.equalsIgnoreCase(MAP_FRAME)) {
                robotHelper.goToHelper.goToMapFrame(false, false, OrientationPolicy.FREE_ORIENTATION);
                atMapFrame = false;
            }
            else {
                robotHelper.goToHelper.goTo(location, false, false, OrientationPolicy.FREE_ORIENTATION);
                atMapFrame = true;

            }
        });
    }

    @Override
    public void onNewEmergency(Emergency emergency) {
        this.emergencyListener = null;
        this.emergency = emergency;
        String locationLabel;
        if(emergency.getType() == 0){
            locationLabel = emergency.getEnvData().getRoom().getName();
        }
        else{
            locationLabel = emergency.getBedLabel();
        }
        System.out.println("gestisco new emergency" + emergency.getName());
      // goToLocationTester(locationLabel,null, emergency);

        if(locationLabel != null) {
            AttachedFrame location = savedLocations.get(locationLabel);
            if(location != null){
                goToLocation(locationLabel, location);
            }
        }


    }

    @Override
    public void onEmergencyHandled() {
        EmergencyRequest emergencyRequest = new EmergencyRequest();
        emergencyRequest.setAsDone(emergency.getId(), new RequestListener<Boolean>() {
            @Override
            public void successResponse(Boolean response) {
                Log.d(CheckMattutino.CONSOLE_TAG, "SetAdDone success");
            }

            @Override
            public void errorResponse(RequestException error) {
                Log.d(CheckMattutino.CONSOLE_TAG, "SetAdDone failed");
            }
        });
    }

    private void handleEmergency(Emergency emergency)  {

        //chiamato da registerListener quando la destinazione è raggiunta

        /***ESPLETARE IL COMPITO DELL'EMERGENZA***/

        /***
         * OCCHIO A QUESTO QUANDO TERMINA:
         * startTopic(R.raw.emergency, endReason -> {
         *                     robotHelper.releaseAbilities();
         *                     emergencyListener = this;
         *                     emergencyListener.onEmergencyHandled();
         *                     this.emergency = null;
         *                     goToLocation(MAP_FRAME, null);
         *                 });
         */

        /***MIO***/
    /*    robotHelper.saySync("Ciao!").run();
        robotHelper.saySync("Porto a termine il mio compito").run();
        startTopic(R.raw.emergency, endReason -> {
            robotHelper.releaseAbilities();
            emergencyListener = this;
            emergencyListener.onEmergencyHandled();
            this.emergency = null;
            goToLocation(MAP_FRAME, null);
        });*/
        /***END MIO***/

        /***ancora mio, prova tour completo ***/






        //robotHelper.say("Ciao " + emergency.getName());
        /*robotHelper.releaseAbilities();
        emergencyListener = this;
        emergencyListener.onEmergencyHandled();
        this.emergency = null;
        this.free = true;*/

        Integer topicResource;

        mainActivity.runOnUiThread(() -> {
            domanda.setVisibility(View.VISIBLE);
            risposta.setVisibility(View.VISIBLE);
        });


        if(bottone){
            topicResource = R.raw.buttonemergency;
            /***TEST***/

            //startTele = true;
            //this.free = true;
            /***TEST***/
            System.out.println("sono qui");

            if(emergency.getPulsante() == 2){
                startTele = true;
                this.free = true;
                mainActivity.runOnUiThread(() -> {
                    domanda.setVisibility(View.GONE);
                    risposta.setVisibility(View.GONE);
                });
            }else{
                /*try{
                    System.out.println("ciao" + emergency.getName());
                    TimeUnit.SECONDS.sleep(30);
                    this.free = true;
                }
                catch(Exception e){ System.out.println("timeccezz");}*/
                //TODO DA RIMETTERE:
                robotHelper.saySync("Ciao" + emergency.getName() ).run();
                mainActivity.runOnUiThread(() -> {
                    domanda.setText("Ciao " + emergency.getName());
                    risposta.setText("Dimmi 'Ciao' o chiedimi cosa posso fare");
                });

                MyQiChatExecutor.idBed = emergency.getValBed();
                //chat compagnia, bisogni vari, telepresenza

                //mainActivity.runOnUiThread(() -> {
                    startTopic(emergency.getName(), topicResource, endReason -> {
                        if(startTele){
                            System.out.println("startTele");
                        }
                        mainActivity.runOnUiThread(() -> {
                            domanda.setText("...");
                            risposta.setText("");
                            domanda.setVisibility(View.GONE);
                            risposta.setVisibility(View.GONE);
                        });
                        robotHelper.releaseAbilities();
                        emergencyListener = this;
                        emergencyListener.onEmergencyHandled();
                        this.free = true;
                        //this.emergency = null;
                    });
                //});
            }

        }
        else {
            robotHelper.saySync("Ciao" + emergency.getName() ).run();
            mainActivity.runOnUiThread(() -> {
                domanda.setText("Ciao " + emergency.getName());
                risposta.setText("Sono qui per te, dimmi 'Ciao'!");
            });

            if(emergency.isButtonEmergency){
                MyQiChatExecutor.idBed = emergency.getValBed();
                topicResource = R.raw.buttonemergency;
                //TODO DA RIMETTERE:
                startTopic(emergency.getName(), topicResource, endReason -> {
                 if(startTele){
                     mainActivity.runOnUiThread(() -> {
                         domanda.setText("Mi dispiace, adesso non posso");
                         risposta.setText("");
                     });
                     robotHelper.saySync("Mi dispiace," + emergency.getName() +" adesso sto portando a termine il check mattutino, chiamami più tardi per la telepresenza" ).run();
                 }
                mainActivity.runOnUiThread(() -> {
                    domanda.setText("...");
                    risposta.setText("");
                    domanda.setVisibility(View.GONE);
                    risposta.setVisibility(View.GONE);
                });
                 robotHelper.releaseAbilities();
                 emergencyListener = this;
                 emergencyListener.onEmergencyHandled();
                 this.free = true;
                 this.emergency = null;
                 });
            }else{
            topicResource = R.raw.emergency;
            questionario.clear();
            //chat questionario
            startTopic(emergency.getName(), topicResource, endReason -> {
                robotHelper.releaseAbilities();
                sendQuestionarioToServer(emergency.getName(),emergency.getSurname(), emergency.getValBed());
                mainActivity.runOnUiThread(() -> {
                    domanda.setText("...");
                    risposta.setText("");
                    domanda.setVisibility(View.GONE);
                    risposta.setVisibility(View.GONE);
                });
                emergencyListener = this;
                emergencyListener.onEmergencyHandled();
                this.emergency = null;
                this.free = true;

                });
            }
        }

    }

    private void sendQuestionarioToServer(String name, String surname, String bed){
        String statement;
        statement = "name="+name.replace(" ","_")+"&surname="+surname.replace(" ","_")+"&room="+bed;

        System.out.println("size questionario: " + questionario.size());

        if(questionario.containsKey("noQuestionario")){
            statement += "&effettuato=0&";

            for(int i=1; i<=10; i++){
                statement += "domanda" + (i) + "=_";
                System.out.println("inserita nello statement domanda " +(i) );
                if(i!=10)
                    statement += "&";
            }

        }else{

            statement += "&effettuato=1";
            statement +="&domanda1="+questionario.get("domanda1").replace(" ","_")+"&";
            for(int i=2; i<=questionario.size(); i++){
                statement += "domanda" + (i) + "=" + questionario.get("domanda"+(i)).replace(" ","_");
                System.out.println("inserita nello statement domanda " +(i) );
                if(i!=questionario.size())
                    statement += "&";
            }
        }

        String urlstr = "https://bettercallpepper.altervista.org/api/setQuestionario.php?"+statement;
        System.out.println("url: " +urlstr);

        try{
            URL urlSetFalse = new URL("https://bettercallpepper.altervista.org/api/setQuestionario.php?"+statement);
            HttpURLConnection conn = null;
            conn = (HttpURLConnection) urlSetFalse.openConnection();
            conn.setRequestMethod("GET");
            conn.getInputStream();
        }  catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void startTopic(String name, Integer topicResource, QiChatbot.OnEndedListener chatEndedListener){

        startTele = false;
        //Locale locale = new Locale(Language.ITALIAN, Region.ITALY);
        try{

            final Topic topic = TopicBuilder.with(qiContext)
             .withResource(topicResource).build();

            /*NOOOOOOOOfinal Future<Topic> topicFuture = TopicBuilder.with(qiContext)
                    .withResource(topicResource).buildAsync();

            Topic topic = topicFuture.get();*/


            // Create a qiChatbot
            QiChatbot qiChatbot = QiChatbotBuilder.with(qiContext).withTopic(topic).build();

            Map<String, QiChatExecutor> executors = new HashMap<>();

            // Map the executor name from the topic to our qiChatExecutor
            executors.put("myExecutor", new MyQiChatExecutor(qiContext));
            MyQiChatExecutor.mainActivity = mainActivity;

            // Set the executors to the qiChatbot
            qiChatbot.setExecutors(executors);
            List<Chatbot> chatbots = new ArrayList<>();
            chatbots.add(qiChatbot);

            // Build chat with the chatbotBuilder
            Chat chat = ChatBuilder.with(qiContext).withChatbot(qiChatbot).build();

            // Run an action asynchronously.
            //chat.async().run();

            Future<Void> chatFuture = chat.async().run();

            qiChatbot.addOnEndedListener(endReason -> {
                chatFuture.requestCancellation();
            });
            qiChatbot.addOnEndedListener(chatEndedListener);

            chatFuture.thenConsume(value -> {
                if (value.hasError()) {
                    System.out.println("Discussion finished with error.");
                }
            });


        }catch(Exception e){ System.out.println("eccezione topic");
        e.printStackTrace();
        }

    }
}
