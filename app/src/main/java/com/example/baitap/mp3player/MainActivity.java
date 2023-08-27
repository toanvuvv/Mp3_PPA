package com.example.baitap.mp3player;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baitap.mp3player.Fragment.ListNowPlayingFragment;
import com.example.baitap.mp3player.Fragment.MainFragment;
import com.example.baitap.mp3player.Fragment.NowPlayingFragment;
import com.example.baitap.mp3player.Fragment.PlaySongFragment;
import com.example.baitap.mp3player.Fragment.SongInAlbumFragment;
import com.example.baitap.mp3player.Fragment.SongInArtistFragment;
import com.example.baitap.mp3player.Fragment.SongInPlayListFragment;
import com.example.baitap.mp3player.FragmentCotroller.FragmentController;
import com.example.baitap.mp3player.Interface.MicrophoneListener;
import com.example.baitap.mp3player.Utils.BarLevelDrawable;
import com.example.baitap.mp3player.Utils.Common;
import com.example.baitap.mp3player.Utils.Constants;
import com.example.baitap.mp3player.Utils.MicrophoneInput;

import java.text.DecimalFormat;

import static com.example.baitap.mp3player.Utils.Constants.PAUSE_PLAY_ACTION;
import static com.example.baitap.mp3player.Utils.Constants.UPDATE;


public class MainActivity extends AppCompatActivity implements  MicrophoneListener {

    public static boolean isLoop = false;
    public static boolean isSuffle = false;
    public static boolean isPlay = false;


//    public static boolean

    TextView dBTextView;
    TextView dBFractionTextView;
    Switch aSwitch;

    BarLevelDrawable mBarLevel;

    MicrophoneInput micInput;
    double mOffsetdB = 10;  // Offset for bar, i.e. 0 lit LEDs at 10 dB.
    // The Google ASR input requirements state that audio input sensitivity
    // should be set such that 90 dB SPL at 1000 Hz yields RMS of 2500 for
    // 16-bit samples, i.e. 20 * log_10(2500 / mGain) = 90.
    double mGain = 2500.0 / Math.pow(10.0, 90.0 / 20.0);
    // For displaying error in calibration.
    double mRmsSmoothed;  // Temporally filtered version of RMS.
    double mAlpha = 0.9;  // Coefficient of IIR smoothing filter for RMS.
    private int mSampleRate;  // The audio sampling rate to use.
    private int mAudioSource;  // The audio source to use.

    // Variables to monitor UI update and check for slow updates.
    private volatile boolean mDrawing;
    private volatile int mDrawingCollided;

    public static final String TAG = "LevelMeterActivity";
    AudioManager audioManager;
    public int db;

    RelativeLayout topLayout;
    BroadcastReceiver broadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initControls();


        //đổi màu statusbar
        Common.setStatusBarTranslucent(true, this);

        FragmentController.replaceFragment(this, new MainFragment(), "MainFragment");


        micInput = new MicrophoneInput(this);
        audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
       // client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        aSwitch.setChecked(false);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    readPreferences();
                    micInput.setSampleRate(mSampleRate);
                    micInput.setAudioSource(mAudioSource);
                    micInput.start();
                    Toast.makeText(MainActivity.this, "Bật chế độ tự động thay đổi âm lượng", Toast.LENGTH_SHORT).show();
                } else {
                    micInput.stop();
                    dBFractionTextView.setText("0");
                    dBTextView.setText("0");
                    Toast.makeText(MainActivity.this, "Tắt chế độ tự động thay đổi âm lượng", Toast.LENGTH_SHORT).show();
                }

            }
        });
        Log.e(getClass().getName(),"onCreate");


    }

    public RelativeLayout getTopLayout() {
        return topLayout;
    }

    private void initControls() {

        dBTextView = (TextView) findViewById(R.id.dbTextView);
        dBFractionTextView = (TextView) findViewById(R.id.dbFractionTextView);
        aSwitch = (Switch) findViewById(R.id.switch_play);
        mBarLevel = (BarLevelDrawable) findViewById(R.id.bar_level_drawable_view);
        topLayout = (RelativeLayout) findViewById(R.id.top_layout);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(getClass().getName(),"onResume");
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }


    //xử lý bài hát


    @Override
    public void processAudioFrame(final short[] audioFrame) {
        if (!mDrawing) {
            mDrawing = true;
            // Compute the RMS value. (Note that this does not remove DC).
            double rms = 0;
            for (int i = 0; i < audioFrame.length; i++) {
                rms += audioFrame[i] * audioFrame[i];
            }
            rms = Math.sqrt(rms / audioFrame.length);

            // Compute a smoothed version for less flickering of the display.
            mRmsSmoothed = mRmsSmoothed * mAlpha + (1 - mAlpha) * rms;
            final double rmsdB = 20.0 * Math.log10(mGain * mRmsSmoothed);
            final int volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);


            // Set up a method that runs on the UI thread to update of the LED bar
            // and numerical display.
            mBarLevel.post(new Runnable() {
                @Override
                public void run() {
                    // Each LED corresponds to 6 dB.
                    mBarLevel.setLevel((mOffsetdB + rmsdB) / 60);
                    DecimalFormat df = new DecimalFormat("##");
                    dBTextView.setText(df.format(20 + rmsdB));

                    db = Integer.parseInt(df.format(20 + rmsdB));
                    int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                    int a = maxVolume/10;

                    if (db >= 75) {
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume - a, 0);
                    } else if (db >= 67 && db < 75) {
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume - 2*a, 0);
                    } else if (db >= 60 && db < 67) {
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume - 3*a, 0);
                    } else if (db >= 50 && db < 60) {
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume - 3*a - 2, 0);
                    } else if (db >= 40 && db < 50) {
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume - 4*a, 0);
                    } else if (db >= 30 && db < 40) {
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume - 5*a, 0);
                    } else if (db >= 20 && db < 30) {
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume - 6*a, 0);
                    } else if (db >= 15 && db < 20) {
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume - 7*a, 0);
                    } else {
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume - 8 *a, 0);
                    }

                    DecimalFormat df_fraction = new DecimalFormat("#");
                    int one_decimal = (int) (Math.round(Math.abs(rmsdB * 10))) % 10;

                    dBFractionTextView.setText(Integer.toString(one_decimal));

                    mDrawing = false;
                }
            });
        } else {
            mDrawingCollided++;
            Log.v(TAG, "Level bar update collision, i.e. update took longer " +
                    "than 20ms. Collision count" + Double.toString(mDrawingCollided));
        }
    }

    private void readPreferences() {
        SharedPreferences preferences = getSharedPreferences("LevelMeter",
                MODE_PRIVATE);
        mSampleRate = preferences.getInt("SampleRate", 8000);
        mAudioSource = preferences.getInt("AudioSource",
                MediaRecorder.AudioSource.VOICE_RECOGNITION);
        Log.e("main", "readPreferences");
    }


    @Override
    public void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();

        filter.addAction(UPDATE);
        filter.addAction(Constants.PAUSE_PLAY_ACTION);


        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                //UI update here
                if (intent.getAction().equals(UPDATE)) {

                    PlaySongFragment playSongFragment=(PlaySongFragment)getSupportFragmentManager().findFragmentByTag("PlaySongFragment");
                    if(playSongFragment!=null){

                        playSongFragment.showInfo();
                        playSongFragment.showTime();

                        NowPlayingFragment nowPlayingFragment=(NowPlayingFragment)playSongFragment.getNowplayingFragment();
                        if(nowPlayingFragment!=null){
                            nowPlayingFragment.setAvartar();
                        }


                        ListNowPlayingFragment listNowPlayingFragment=(ListNowPlayingFragment)playSongFragment.getListNowplayingFragment();
                        if(listNowPlayingFragment!=null){
                            listNowPlayingFragment.getListNowPlayingAdapter().notifyDataSetChanged();
                        }

                    }
                    MainFragment mainFragment=(MainFragment) getSupportFragmentManager().findFragmentByTag("MainFragment");
                    if(mainFragment!=null){
                        mainFragment.showInfo();

                    }

                    SongInPlayListFragment songInPlayListFragment = (SongInPlayListFragment) getSupportFragmentManager().findFragmentByTag("SongInPlayListFragment");
                    if(songInPlayListFragment!=null){
                        songInPlayListFragment.showInfo();
                    }
                    SongInAlbumFragment songInAlBumFragment = (SongInAlbumFragment) getSupportFragmentManager().findFragmentByTag("SongInAlbumFragment");
                    if(songInAlBumFragment!=null){
                        songInAlBumFragment.showInfo();
                    }

                    SongInArtistFragment songInArtistFragment = (SongInArtistFragment) getSupportFragmentManager().findFragmentByTag("SongInArtistFragment");
                    if(songInArtistFragment!=null){
                        songInArtistFragment.showInfo();
                    }


                }
                else if(intent.getAction().equals(PAUSE_PLAY_ACTION)){
                    PlaySongFragment playSongFragment=(PlaySongFragment)getSupportFragmentManager().findFragmentByTag("PlaySongFragment");
                    if(playSongFragment!=null){
                    playSongFragment.updatePlayPause();

                        NowPlayingFragment nowPlayingFragment=(NowPlayingFragment)playSongFragment.getNowplayingFragment();
                        if(nowPlayingFragment!=null){
                            nowPlayingFragment.checkAvartar();
                        }
                    }

                    MainFragment mainFragment=(MainFragment) getSupportFragmentManager().findFragmentByTag("MainFragment");
                    if(mainFragment!=null){
                        mainFragment.updatePlayPause();
                    }
                    SongInPlayListFragment songInPlayListFragment = (SongInPlayListFragment) getSupportFragmentManager().findFragmentByTag("SongInPlayListFragment");
                    if(songInPlayListFragment!=null){
                        songInPlayListFragment.updatePlayPause();
                    }
                    SongInAlbumFragment songInAlBumFragment = (SongInAlbumFragment) getSupportFragmentManager().findFragmentByTag("SongInAlbumFragment");
                    if(songInAlBumFragment!=null){
                        songInAlBumFragment.updatePlayPause();
                    }

                    SongInArtistFragment songInArtistFragment = (SongInArtistFragment) getSupportFragmentManager().findFragmentByTag("SongInArtistFragment");
                    if(songInArtistFragment!=null){
                        songInArtistFragment.updatePlayPause();
                    }



                }
            }
        };
        registerReceiver(broadcastReceiver,filter);

    }

    @Override
    public void onStop() {
        super.onStop();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }


}
