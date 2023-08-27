package com.example.baitap.mp3player.Utils;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import com.example.baitap.mp3player.Interface.MicrophoneListener;



public class MicrophoneInput implements Runnable {
    int mSampleRate = 8000;
    int mAudioSource = MediaRecorder.AudioSource.VOICE_RECOGNITION;
    final int mChannelConfig = AudioFormat.CHANNEL_IN_MONO;
    final int mAudioFormat = AudioFormat.ENCODING_PCM_16BIT;

    private final MicrophoneListener mListener;
    private Thread mThread;
    private boolean mRunning;

    AudioRecord recorder;

    int mTotalSamples = 0;

    private static final String TAG = "MicrophoneInput";

    public MicrophoneInput(MicrophoneListener mListener) {
        this.mListener = mListener;
    }
    public void setSampleRate(int sampleRate) {
        mSampleRate = sampleRate;
    }

    public void setAudioSource(int audioSource) {
        mAudioSource = audioSource;
    }

    public void start() {
        if (false == mRunning) {
            mRunning = true;
            mThread = new Thread(this);
            mThread.start();
        }
    }

    public void stop() {
        try {
            if (mRunning) {
                mRunning = false;
                mThread.join();
            }
        } catch (InterruptedException e) {
            Log.v(TAG, "InterruptedException.", e);
        }
    }


    @Override
    public void run() {
        // Bộ đệm cho 20 ms dữ liệu
        short[] buffer20ms = new short[mSampleRate / 50];
        // Kích thước bộ đệm của bộ đệm AudioRecord, ít nhất 1 giây.
        int buffer1000msSize = bufferSize(mSampleRate, mChannelConfig,
                mAudioFormat);

        try {
            recorder = new AudioRecord(
                    mAudioSource,
                    mSampleRate,
                    mChannelConfig,
                    mAudioFormat,
                    buffer1000msSize);
            recorder.startRecording();

            while (mRunning) {
                int numSamples = recorder.read(buffer20ms, 0, buffer20ms.length);
                mTotalSamples += numSamples;
                mListener.processAudioFrame(buffer20ms);

            }
            recorder.stop();
        } catch(Throwable x) {
            Log.v(TAG, "Error reading audio", x);
        } finally {
        }
    }

    public int totalSamples() {
        return mTotalSamples;
    }

    public void setTotalSamples(int totalSamples) {
        mTotalSamples = totalSamples;
    }



    private int bufferSize(int sampleRateInHz, int channelConfig,
                           int audioFormat) {
        int buffSize = AudioRecord.getMinBufferSize(sampleRateInHz, channelConfig,
                audioFormat);
        if (buffSize < sampleRateInHz) {
            buffSize = sampleRateInHz;
        }
        return buffSize;
    }
}