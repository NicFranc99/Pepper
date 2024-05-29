package com.example.pepperapp28aprile.map;

import android.content.Context;
import android.util.Log;

import com.aldebaran.qi.Future;
import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.builder.AnimateBuilder;
import com.aldebaran.qi.sdk.builder.AnimationBuilder;
import com.aldebaran.qi.sdk.builder.DiscussBuilder;
import com.aldebaran.qi.sdk.builder.HolderBuilder;
import com.aldebaran.qi.sdk.builder.ListenBuilder;
import com.aldebaran.qi.sdk.builder.PhraseSetBuilder;
import com.aldebaran.qi.sdk.builder.QiChatbotBuilder;
import com.aldebaran.qi.sdk.builder.SayBuilder;
import com.aldebaran.qi.sdk.builder.TopicBuilder;
import com.aldebaran.qi.sdk.object.actuation.Actuation;
import com.aldebaran.qi.sdk.object.actuation.Animate;
import com.aldebaran.qi.sdk.object.actuation.Animation;
import com.aldebaran.qi.sdk.object.actuation.AttachedFrame;
import com.aldebaran.qi.sdk.object.actuation.Frame;
import com.aldebaran.qi.sdk.object.actuation.Mapping;
import com.aldebaran.qi.sdk.object.conversation.BodyLanguageOption;
import com.aldebaran.qi.sdk.object.conversation.Chatbot;
import com.aldebaran.qi.sdk.object.conversation.Discuss;
import com.aldebaran.qi.sdk.object.conversation.Listen;
import com.aldebaran.qi.sdk.object.conversation.PhraseSet;
import com.aldebaran.qi.sdk.object.conversation.QiChatbot;
import com.aldebaran.qi.sdk.object.conversation.Say;
import com.aldebaran.qi.sdk.object.conversation.Topic;
import com.aldebaran.qi.sdk.object.geometry.TransformTime;
import com.aldebaran.qi.sdk.object.holder.AutonomousAbilitiesType;
import com.aldebaran.qi.sdk.object.holder.Holder;
import com.aldebaran.qi.sdk.object.locale.Language;
import com.aldebaran.qi.sdk.object.locale.Locale;
import com.aldebaran.qi.sdk.object.locale.Region;
import com.aldebaran.qi.sdk.object.power.FlapSensor;
import com.aldebaran.qi.sdk.object.power.Power;

import com.example.pepperapp28aprile.Globals;
import com.example.pepperapp28aprile.R;
import com.example.pepperapp28aprile.utilities.Phrases;

import java.util.ArrayList;
import java.util.Random;

public class RobotHelper {
    private static final String TAG = "MSI_RobotHelper";
    public Holder holder; // for holding autonomous abilities when required
    public GoToHelper goToHelper;
    public LocalizeAndMapHelper localizeAndMapHelper;
    private Actuation actuation; // Store the Actuation service.
    private Mapping mapping; // Store the Mapping service.
    private QiContext qiContext; // The QiContext provided by the QiSDK.
    private FlapSensor chargingFlap;

    public RobotHelper() {
        goToHelper = new GoToHelper();
        localizeAndMapHelper = new LocalizeAndMapHelper();
    }

    public void onRobotFocusGained(QiContext qc) {
        qiContext = qc;
        actuation = qiContext.getActuation();
        mapping = qiContext.getMapping();
        goToHelper.onRobotFocusGained(qiContext);
        localizeAndMapHelper.onRobotFocusGained(qiContext);
        Power power = qiContext.getPower();
        chargingFlap = power.getChargingFlap();
    }

    /**
     * Get the charging flap state.
     *
     * @return Charging flap state: "True" if opened, "False" if closed
     */
    public boolean getFlapState() {
        boolean flapState = chargingFlap.async().getState().getValue().getOpen();
        Log.d(TAG, "getFlapState: Is opened ? :" + flapState);
        return flapState;
    }

    /**
     * Get the charging flap state and ask to close it if opened.
     *
     * @return Charging flap state: "True" if opened, "False" if closed
     */
    public boolean askToCloseIfFlapIsOpened(Context context) {
        boolean isFlapOpened = getFlapState();
        if (isFlapOpened)
            say(context.getString(R.string.close_charging_flat));
        return isFlapOpened;
    }

    /**
     * Hold Autonomous abilities, BasicAwareness and BackgroundMovement if needed
     *
     * @param withBackgroundMovement "true" to hold BackgroundMovement or "false"
     * @return Future of the Holder
     */
    public Future<Void> holdAbilities(boolean withBackgroundMovement) {
        // Build the holder for the abilities.
        return releaseAbilities().thenCompose(voidFuture -> {
            if (withBackgroundMovement) {
                holder = HolderBuilder.with(qiContext)
                        .withAutonomousAbilities(
                                AutonomousAbilitiesType.BACKGROUND_MOVEMENT,
                                AutonomousAbilitiesType.BASIC_AWARENESS
                        )
                        .build();
            } else {
                holder = HolderBuilder.with(qiContext)
                        .withAutonomousAbilities(
                                AutonomousAbilitiesType.BASIC_AWARENESS
                        )
                        .build();
            }
            //Log.d(TAG, "holdAbilities");
            // Hold the abilities asynchronously.
            return holder.async().hold();
        });
    }

    /**
     * Release Autonomous abilities.
     *
     * @return Future of the Holder
     */
    public Future<Void> releaseAbilities() {
        // Release the holder asynchronously.
        if (holder != null) {
            //Log.d(TAG, "releaseAbilities");
            return holder.async().release().andThenConsume(aVoid -> {
                holder = null;
            });
        } else {
            //Log.d(TAG, "releaseAbilities: No holder to release");
            return Future.of(null);
        }
    }

    /**
     * Make Pepper say a sentence.
     *
     * @param text to be said by Pepper
     * @return
     */
    public Future<Void> say(final String text) {
        return SayBuilder.with(qiContext)
                .withText(text)
                .withLocale(new Locale(Language.ITALIAN, Region.ITALY))
                .withBodyLanguageOption(BodyLanguageOption.DISABLED)
                .buildAsync().andThenCompose(say -> {
                    Log.d(TAG, "Say started : " + text);
                    return say.async().run();
                });
    }

    /***
     * Utile per animare con un movimento pepper prendendo in input il raw id della animazione
     * @param rawAnimation
     * @return
     */
    public Future<Void> animation(final int rawAnimation) {
        return AnimationBuilder.with(qiContext)
                .withResources(rawAnimation)
                .buildAsync()
                .andThenCompose(animation ->
                        AnimateBuilder.with(qiContext)
                                .withAnimation(animation)
                                .buildAsync()
                )
                .andThenCompose(animate -> animate.async().run());
    }

    /***
     * Permette a pepper di dire una frase muovendosi
     * @return
     */
    public Future<Void> sayAndMove(final int pepperMotionAnimation, final String pepperPhrase){
        return say(pepperPhrase)
                .andThenCompose(result -> animation(pepperMotionAnimation));
    }

    /**
     * Questo metodo viene utilizzato quando si vuole che pepper parli con una velocità moderata
     * (attualmente utilizzato per il gioco di tipo racconto).
     * Viene utilizzato tale parametro di default, definito nella classe Globals.java (velocità a 80).
     * @param text Testo da far dire a pepper
     * @param isCustomSpeed Nel caso sia true, viene utilizzata la velocità di default definita in Globals.java
     * @return
     */
    public Future<Void> say(final String text,boolean isCustomSpeed) {

        if(isCustomSpeed){
            String textSlowly = Globals.PepperSpeekSpeed + text;
            return SayBuilder.with(qiContext)
                    .withText(textSlowly)
                    .withLocale(new Locale(Language.ITALIAN, Region.ITALY))
                    .withBodyLanguageOption(BodyLanguageOption.DISABLED)
                    .buildAsync().andThenCompose(say -> {
                        Log.d(TAG, "Say started : " + textSlowly);
                        return say.async().run();
                    });
        }

        return SayBuilder.with(qiContext)
                .withText(text)
                .withLocale(new Locale(Language.ITALIAN, Region.ITALY))
                .withBodyLanguageOption(BodyLanguageOption.DISABLED)
                .buildAsync().andThenCompose(say -> {
                    Log.d(TAG, "Say started : " + text);
                    return say.async().run();
                });
    }

    public Say saySync(final String text){
        return SayBuilder.with(qiContext)
                .withText(text)
                .withLocale(new Locale(Language.ITALIAN, Region.ITALY))
                .build();
    }
    /**
     * Get the Frame of the origin of the map.
     *
     * @return Frame of MapFrame
     */
    public Frame getMapFrame() {
        return mapping.async().mapFrame().getValue();
    }

    /**
     * Get an AttachedFrame of the actual robot position relatively to the MapFrame.
     *
     * @return AttachedFrame of the robot position
     */
    public Future<AttachedFrame> createAttachedFrameFromCurrentPosition() {
        // Get the robot frame asynchronously.
        return actuation.async()
                .robotFrame()
                .andThenApply(robotFrame -> {
                    Frame mapFrame = getMapFrame();

                    // Transform between the current robot location (robotFrame) and the mapFrame
                    TransformTime transformTime = robotFrame.computeTransform(mapFrame);

                    // Create an AttachedFrame representing the current robot frame relatively to the MapFrame
                    return mapFrame.makeAttachedFrame(transformTime.getTransform());
                });
    }
}
