package com.example.pepperapp28aprile.map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.aldebaran.qi.Future;
import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.builder.AnimateBuilder;
import com.aldebaran.qi.sdk.builder.AnimationBuilder;
import com.aldebaran.qi.sdk.builder.ExplorationMapBuilder;
import com.aldebaran.qi.sdk.builder.LocalizeAndMapBuilder;
import com.aldebaran.qi.sdk.builder.LocalizeBuilder;
import com.aldebaran.qi.sdk.object.actuation.ExplorationMap;
import com.aldebaran.qi.sdk.object.actuation.Localize;
import com.aldebaran.qi.sdk.object.actuation.LocalizeAndMap;
import com.aldebaran.qi.sdk.object.image.EncodedImage;
import com.aldebaran.qi.sdk.object.streamablebuffer.StreamableBuffer;

import java.util.ArrayList;
import java.util.List;

import com.example.pepperapp28aprile.R;

/***
 * <p>
 * This helper simplifies the use of the Localize and LocalizeAndMap actions.
 * It will hide the build of those actions, and make sure to keep you updated when the robot
 * is mapping, localizing. And make sure you don't run both at the same time.
 * </p><br/><p>
 * <strong>Usage:</strong><br/>
 * 1) Create an instance in "onCreate"<br/>
 * 2) Call onRobotFocusGained in "onRobotFocusGained"<br/>
 * 3) Call onRobotFocusLost in "onRobotFocusLost"<br/>
 * 4) Call localize or localizeAndMap whenever you want the robot to locate itself.<br/>
 * </p>
 */
public class LocalizeAndMapHelper {

    private static final String TAG = "MSILocalizeAndMapHelper";
    public ExplorationMap explorationMap;
    private QiContext qiContext; // The QiContext provided by the QiSDK.
    private Future<Void> currentlyRunningLocalize;
    private LocalizeAndMap currentLocalizeAndMap;
    private String currentExplorationMapData;
    private StreamableBuffer streamableExplorationMap;
    private List<onStartedLocalizingListener> startedLocalizingListeners;
    private List<onFinishedLocalizingListener> finishedLocalizingListeners;
    private List<onStartedMappingListener> startedMappingListeners;
    private List<onFinishedMappingListener> finishedMappingListeners;
    private Localize builtLocalize;

    /**
     * Constructor: call me in your `onCreate`
     */
    LocalizeAndMapHelper() {
        startedLocalizingListeners = new ArrayList<>();
        finishedLocalizingListeners = new ArrayList<>();
        startedMappingListeners = new ArrayList<>();
        finishedMappingListeners = new ArrayList<>();
        currentExplorationMapData = "";
        streamableExplorationMap = null;
        builtLocalize = null;
    }

    /**
     * Call me in your `onRobotFocusGained`
     *
     * @param qc the qiContext provided to your Activity
     */
    void onRobotFocusGained(QiContext qc) {
        qiContext = qc;
        builtLocalize = null;
    }

    /**
     * Call me in your `onRobotFocusLost`
     */
    void onRobotFocusLost() {
        stopCurrentAction();
        // Remove the QiContext.
        qiContext = null;
    }

    /**
     * Force-feed the map in case you saved it into a file.
     *
     * @param map the map you previously saved from `getStreamableMap`
     */
    public void setStreamableMap(StreamableBuffer map) {
        builtLocalize = null;
        streamableExplorationMap = map;
    }

    /**
     * Get the current map.
     *
     * @return the map as a StreamableBuffer, for you to backup into a file.
     */
    public StreamableBuffer getStreamableMap() {
        return streamableExplorationMap;
    }

    /**
     * Checks if a previous localize or localizeAndMap was running and cancel it. This is to
     * make sure you don't run both at the same time (not possible on Pepper).
     *
     * @return Future that will complete when the action is cancelled.
     */
    private Future<Void> checkAndCancelCurrentLocalize() {
        if (currentlyRunningLocalize == null) return Future.of(null);
        currentlyRunningLocalize.requestCancellation();
        Log.d(TAG, "checkAndCancelCurrentLocalize");
        return currentlyRunningLocalize;
    }

    /**
     * Start localizing: the robot will look around and will try to find out where it is.<br/>
     * <strong>Important:</strong> your need to load a Map before being able to localize, so don't
     * forget to call setStreamableMap or run a localizeAndMap.
     *
     * @return Future that will complete when you stop localize
     */
    public Future<Void> localize() {
        raiseStartedLocalizing();

        return checkAndCancelCurrentLocalize()
                .thenConsume(aUselessFuture -> {
                    buildStreamableExplorationMap().andThenConsume(value -> {
                        explorationMap = value;
                        LocalizeBuilder.with(qiContext).withMap(explorationMap).buildAsync()
                                .andThenCompose(localize -> {
                                    builtLocalize = localize;
                                    Log.d(TAG, "localize: localize built successfully");
                                    Log.d(TAG, "localize: addOnStatusChangedListener");
                                    builtLocalize.addOnStatusChangedListener(status -> checkStatusAndRaiseLocalized(status));
                                    currentlyRunningLocalize = builtLocalize.async().run();
                                    Log.d(TAG, "localize running...");
                                    return currentlyRunningLocalize;
                                })
                                .thenConsume(finishedLocalize -> {
                                    Log.d(TAG, "localize: removeAllOnStatusChangedListeners");
                                    builtLocalize.removeAllOnStatusChangedListeners();
                                    if (finishedLocalize.isCancelled()) {
                                        Log.d(TAG, "localize cancelled.");
                                        raiseFinishedLocalizing(LocalizationStatus.CANCELLED);
                                    } else if (finishedLocalize.hasError()) {
                                        Log.d(TAG, "Failed to localize in map : ", finishedLocalize.getError());
                                        //The error below could happen when trying to run multiple Localize action with the same Localize object (called builtLocalize here).
                                        if (finishedLocalize.getError().toString().equals("com.aldebaran.qi.QiException: tr1::bad_weak_ptr") || finishedLocalize.getError().toString().equals("com.aldebaran.qi.QiException: Animation failed.")) {
                                            Log.d(TAG, "localize: com.aldebaran.qi.QiException: tr1::bad_weak_ptr");
                                            builtLocalize = null;
                                            localize().get();
                                        } else raiseFinishedLocalizing(LocalizationStatus.FAILED);
                                    } else {
                                        Log.d(TAG, "localize finished.");
                                        raiseFinishedLocalizing(LocalizationStatus.FINISHED);
                                    }
                                });
                    });
                });
    }


    /**
     * Build the ExplorationMap from a StreamableBuffer.
     *
     * @return the future of the ExplorationMap
     */
    public Future<ExplorationMap> buildStreamableExplorationMap() {
        if (explorationMap == null) {
            Log.d(TAG, "buildStreamableExplorationMap: Building map from StreamableBuffer");
            return ExplorationMapBuilder.with(qiContext).withStreamableBuffer(streamableExplorationMap).buildAsync();
        }
        return Future.of(explorationMap);
    }

    /**
     * Start localizeAndMap: the robot will record its current position and continue mapping its
     * environment. In this mode, any obstacle, such as a human on the way, will be counted as a
     * definitive obstacle in the map, similarly as a wall.
     * <br/>
     * Use this mode to push the robot around to make it learn its environment. Make sure to stay
     * behind the robot.
     *
     * @return Future that will complete when you stop localizeAndMap
     */
    public Future<Void> localizeAndMap(boolean withExistingMap) {
        raiseStartedMapping();
        return checkAndCancelCurrentLocalize()
                .thenCompose(aUselessVoid -> {
                    if (withExistingMap) {
                        return LocalizeAndMapBuilder.with(qiContext).withMap(explorationMap).buildAsync();
                    } else return LocalizeAndMapBuilder.with(qiContext).buildAsync();
                }).andThenCompose(localizeAndMap -> {
                    Log.d(TAG, "localizeAndMap built successfully, running...");
                    localizeAndMap.addOnStatusChangedListener(status -> checkStatusAndRaiseLocalized(status));
                    currentLocalizeAndMap = localizeAndMap;
                    currentlyRunningLocalize = localizeAndMap.async().run();
                    return currentlyRunningLocalize;
                })
                .thenConsume(finishedLocalizeAndMap -> {
                    if (finishedLocalizeAndMap.hasError()) {
                        Log.w(TAG, "LocalizeAndMap finished with error: ", finishedLocalizeAndMap.getError());
                        raiseFinishedMapping(false);
                    } else {
                        Log.d(TAG, "LocalizeAndMap finished with success.");

                        try {
                            currentLocalizeAndMap.async().dumpMap()
                                    .andThenConsume(dumpedMap -> {
                                        explorationMap = dumpedMap;
                                        setStreamableMap(dumpedMap.serializeAsStreamableBuffer());

                                    })
                                    .thenConsume(f -> {
                                        if (f.hasError()) {
                                            Log.w(TAG, "map dump finished with error: ", f.getError());
                                            raiseFinishedMapping(false);
                                        } else {
                                            Log.d(TAG, "map dump finished with success.");
                                            raiseFinishedMapping(true);
                                        }
                                    });
                        } catch (Exception e) {
                            Log.d(TAG, "localizeAndMap: dump exception : " + e.toString());
                        } catch (OutOfMemoryError e) {
                            Log.d(TAG, "localizeAndMap: dump/outOfMemoryError : " + e.toString());
                        }
                    }
                    currentLocalizeAndMap.removeAllOnStatusChangedListeners();
                    currentLocalizeAndMap = null;
                });
    }

    /**
     * Stop the currently running localize or localizeAndMap.
     *
     * @return A Future that will complete when the action is cancelled.
     */
    public Future<Void> stopCurrentAction() {
        return checkAndCancelCurrentLocalize();
    }

    /**
     * Callback for localize status changes. When the status is "localized", it will raise the
     * callback for the UI.
     */
    private void checkStatusAndRaiseLocalized(com.aldebaran.qi.sdk.object.actuation.LocalizationStatus status) {
        Log.d(TAG, "checkStatusAndRaiseLocalized status: " + status.toString());
        if (status == com.aldebaran.qi.sdk.object.actuation.LocalizationStatus.LOCALIZED) {
            Log.d(TAG, "Robot is localized");
            raiseFinishedLocalizing(LocalizationStatus.LOCALIZED);
        }
    }

    public Future<Void> animationToLookInFront() {
        Log.d(TAG, "animationToLookInFront: started");
        return AnimationBuilder.with(qiContext) // Create the builder with the context.
                .withResources(R.raw.idle) // Set the animation resource.
                .buildAsync().andThenCompose(animation -> AnimateBuilder.with(qiContext)
                        .withAnimation(animation)
                        .buildAsync().andThenCompose(animate -> animate.async().run()));
    }


    /**
     * This function will build the ExplorationMap from a StreamableBuffer and then get a visual
     * representation of it.
     *
     * @return the Future of a Bitmap
     */
    public Future<Bitmap> getExplorationMapBitmap() {
        return buildStreamableExplorationMap().andThenConsume(value -> explorationMap = value)
                .thenCompose(uselessFuture -> explorationMap.async().getTopGraphicalRepresentation().andThenCompose(mapGraphicalRepresentation -> {
                    EncodedImage encodedImage = mapGraphicalRepresentation.getImage();
                    byte[] decodedString = encodedImage.getData().array();
                    final BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inMutable = true;
                    return Future.of(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length, options));
                }));
    }

    public void addOnStartedLocalizingListener(onStartedLocalizingListener f) {
        startedLocalizingListeners.add(f);
    }

    public void addOnFinishedLocalizingListener(onFinishedLocalizingListener f) {
        finishedLocalizingListeners.add(f);
    }

    public void addOnStartedMappingListener(onStartedMappingListener f) {
        startedMappingListeners.add(f);
    }

    public void addOnFinishedMappingListener(onFinishedMappingListener f) {
        finishedMappingListeners.add(f);
    }

    public void removeOnStartedLocalizingListeners() {
        startedLocalizingListeners.clear();
    }

    public void removeOnFinishedLocalizingListeners() {
        finishedLocalizingListeners.clear();
    }

    public void removeOnStartedMappingListeners() {
        startedMappingListeners.clear();
    }

    public void removeOnFinishedMappingListeners() {
        finishedMappingListeners.clear();
    }

    private void raiseStartedLocalizing() {
        for (onStartedLocalizingListener f : startedLocalizingListeners) {
            f.onStartedLocalizing();
        }
    }

    public void raiseFinishedLocalizing(LocalizationStatus result) {
        for (onFinishedLocalizingListener f : finishedLocalizingListeners) {
            f.onFinishedLocalizing(result);
        }
    }

    private void raiseStartedMapping() {
        for (onStartedMappingListener f : startedMappingListeners) {
            f.onStartedMapping();
        }
    }

    private void raiseFinishedMapping(boolean success) {
        for (onFinishedMappingListener f : finishedMappingListeners) {
            f.onFinishedMapping(success);
        }
    }

    public enum LocalizationStatus {
        LOCALIZED,
        MAP_MISSING,
        FAILED,
        CANCELLED,
        FINISHED
    }

    /**
     * Little helper for the UI to subscribe to the current state of localize/map.
     * This has nothing to do with the robot, but is for helping in the MainActivity to enable
     * or disable functions during an action.
     */
    public interface onStartedLocalizingListener {
        void onStartedLocalizing();
    }

    public interface onFinishedLocalizingListener {
        void onFinishedLocalizing(LocalizationStatus result);
    }

    public interface onStartedMappingListener {
        void onStartedMapping();
    }

    public interface onFinishedMappingListener {
        void onFinishedMapping(boolean success);
    }
}
