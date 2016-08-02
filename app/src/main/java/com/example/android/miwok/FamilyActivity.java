package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class FamilyActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;

    AudioManager.OnAudioFocusChangeListener afChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                public void onAudioFocusChange(int focusChange) {
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT || focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                        // Pause playback
                        mediaPlayer.pause();
                        mediaPlayer.seekTo(0);
                    } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                        // Resume playback
                        mediaPlayer.start();
                    } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        // Stop playback
                        releaseMediaPlayer();
                    }
                }
            };


    private MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMediaPlayer();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<word> familyArrayList = new ArrayList<>();
        familyArrayList.add(new word("Father", "әpә", R.drawable.family_father, R.raw.family_father));
        familyArrayList.add(new word("Mother", "әṭa", R.drawable.family_mother, R.raw.family_mother));
        familyArrayList.add(new word("Son", "angsi", R.drawable.family_son, R.raw.family_son));
        familyArrayList.add(new word("Daughter", "tune", R.drawable.family_daughter, R.raw.family_daughter));
        familyArrayList.add(new word("Older Brother", "taachi", R.drawable.family_older_brother, R.raw.family_older_brother));
        familyArrayList.add(new word("Younger Brother", "chalitti", R.drawable.family_younger_brother, R.raw.family_younger_brother));
        familyArrayList.add(new word("Older Sister", "teṭe", R.drawable.family_older_sister, R.raw.family_older_sister));
        familyArrayList.add(new word("Younger Sister", "kolliti", R.drawable.family_younger_sister, R.raw.family_younger_sister));
        familyArrayList.add(new word("Grandmother", "ama", R.drawable.family_grandmother, R.raw.family_grandmother));
        familyArrayList.add(new word("Grandfather", "paapa", R.drawable.family_grandfather, R.raw.family_grandfather));

        WordAdapter adapter = new WordAdapter(this, familyArrayList, R.color.category_family);
        ListView numbersListView = (ListView) findViewById(R.id.listView);
        numbersListView.setAdapter(adapter);

        numbersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                word item = familyArrayList.get(position);
                releaseMediaPlayer();
                int result = audioManager.requestAudioFocus(afChangeListener,
                        // Use the music stream.
                        AudioManager.STREAM_MUSIC,
                        // Request permanent focus.
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    mediaPlayer = MediaPlayer.create(FamilyActivity.this, item.getAudioResourseId());
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(mOnCompletionListener);
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }


    /**
         * Clean up the media player by releasing its resources.
         */
        private void releaseMediaPlayer() {
            // If the media player is not null, then it may be currently playing a sound.
            if (mediaPlayer != null) {
                // Regardless of the current state of the media player, release its resources
                // because we no longer need it.
                mediaPlayer.release();
                // Set the media player back to null. For our code, we've decided that
                // setting the media player to null is an easy way to tell that the media player
                // is not configured to play an audio file at the moment.
                mediaPlayer = null;
                //Abandons audio focus from the app when no longer needed.
                audioManager.abandonAudioFocus(afChangeListener);
            }
        }
    }

