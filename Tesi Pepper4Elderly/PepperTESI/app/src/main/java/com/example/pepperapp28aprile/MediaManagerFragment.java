package com.example.pepperapp28aprile;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.pepperapp28aprile.animations.Animations;
import com.example.pepperapp28aprile.utilities.Util;
import com.example.pepperapp28aprile.utilities.VoiceManager;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.IOException;

public class MediaManagerFragment extends Fragment {
    private final String phatMedia;
    private final Persona.Game.Domanda.typeMedia typeMedia;
    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private ShapeableImageView play;
    private Handler handler = new Handler();
    private TextView start, finish;

    public MediaManagerFragment(String phatMedia, Persona.Game.Domanda.typeMedia typeMedia) {
        this.phatMedia = phatMedia;
        this.typeMedia = typeMedia;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View viewmedia = null;

        if (typeMedia.equals(Persona.Game.Domanda.typeMedia.AUDIO)) {
            viewmedia = setUiAudio(inflater, container);
        }
        if (typeMedia.equals(Persona.Game.Domanda.typeMedia.IMAGE)) {
            viewmedia = setUiImage(inflater, container);
        }

        return viewmedia;
    }

    @Override
    public void onPause() {

        // VoiceManager.getIstance(getContext()).stop();
        if (mediaPlayer != null && mediaPlayer.isPlaying() && typeMedia.equals(Persona.Game.Domanda.typeMedia.AUDIO)) {
            mediaPlayer.stop();
        }
        super.onPause();

    }

    private View setUiImage(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.image_view_fragment, container, false);
        ImageView imageView = view.findViewById(R.id.imagedom);
        Glide.with(this).load(phatMedia).centerCrop().apply(new RequestOptions().override(250, 250))
                .placeholder(R.drawable.ic_loop).into(imageView);

        imageView.setOnClickListener(v -> new ImageBigDialogFragment(getContext(), phatMedia).show());
        Animations.focusViewBackGround(getContext(), imageView, Animations.SCALING_BUTTON);

        return view;
    }

    private View setUiAudio(LayoutInflater inflater, ViewGroup container) {

        View view = inflater.inflate(R.layout.music_view_fragment, container, false);
         play = view.findViewById(R.id.play);
        Animations.focusViewBackGround(getContext(), play, Animations.SCALING_BUTTON);

        seekBar = view.findViewById(R.id.seek);
        seekBar.setEnabled(false);
        seekBar.setMax(100);

        start = view.findViewById(R.id.txtstart);
        finish = view.findViewById(R.id.txtfinish);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA).build());

        try {
            mediaPlayer.setDataSource(phatMedia);
            mediaPlayer.prepare();

            VoiceManager.getIstance(getContext()).setListener(new VoiceManager.StatusListener() {
                @Override
                public void onStart() {
                }

                @Override
                public void onDone() {
                    mediaPlayer.start();
                    updateSeekBar();
                }

                @Override
                public void onError() {
                    mediaPlayer.start();
                    updateSeekBar();
                }

                @Override
                public void onRangeStart(String utteranceId, int start, int end, int frame) {

                }
            });

            finish.setText(Util.milliSecondsToTimer(mediaPlayer.getDuration()));

        } catch (IOException e) {
            Toast.makeText(getContext(), "Qualcosa é andato storto con la riproduzione. Riprova piú tardi",
                    Toast.LENGTH_SHORT).show();
        }

        if (mediaPlayer.isPlaying()) {
            play.setImageDrawable(getContext().getDrawable(R.drawable.ic_pause));
            updateSeekBar();
        }

        mediaPlayer.setOnCompletionListener(mp -> play.setImageDrawable(getContext().getDrawable(R.drawable.ic_replay)));

        play.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                play.setImageDrawable(getContext().getDrawable(R.drawable.answer));
            } else {
                mediaPlayer.start();
                play.setImageDrawable(getContext().getDrawable(R.drawable.ic_pause));
                updateSeekBar();
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (typeMedia == Persona.Game.Domanda.typeMedia.AUDIO) {
            mediaPlayer.stop();
        }
    }

    private void updateSeekBar() {
        if (mediaPlayer.isPlaying()) {
            seekBar.setProgress((int) (((float) mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration()) * 100));
            handler.postDelayed(updater, 1000);
        }
    }

    private final Runnable updater = new Runnable() {
        @Override
        public void run() {
            updateSeekBar();
            long currentDuration = mediaPlayer.getCurrentPosition();
            start.setText(Util.milliSecondsToTimer(currentDuration));
        }
    };

    public void start(){
        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Operazioni dell'interfaccia utente
                mediaPlayer.start();
                updateSeekBar();
                play.setImageDrawable(getContext().getDrawable(R.drawable.ic_pause));
            }
        });


    }
}
