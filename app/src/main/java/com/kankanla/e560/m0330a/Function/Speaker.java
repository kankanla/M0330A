package com.kankanla.e560.m0330a.Function;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.media.audiofx.AcousticEchoCanceler;
import android.media.audiofx.AutomaticGainControl;
import android.media.audiofx.NoiseSuppressor;
import android.widget.Toast;

/**
 * Created by E560 on 2017/04/01.
 */

public class Speaker {
    private AudioRecord audioRecord;
    private AudioTrack audioTrack;
    private AcousticEchoCanceler acousticEchoCanceler;
    private AutomaticGainControl automaticGainControl;
    protected NoiseSuppressor noiseSuppressor;
    private Context context;
    private int sampleRateInHz;
    private int audioSource;
    private int audioRecord_sessionId;
    private int audioTrack_sessionId;
    private byte[] readbufer;
    private boolean micSwitch;
    private final String noAECSupport = "noAECSupport";
    private final String started = "started";
    private final String stopped = "stopped";
    private final String noAutomaticGainControl = "noAutomaticGainControl";
    private final String noNoiseSuppressor = "noNoiseSuppressor";

    public Speaker(Context context) {
        this.context = context;
        this.sampleRateInHz = 44100;
        this.audioSource = MediaRecorder.AudioSource.MIC;
    }

    public void setAGC(boolean bl) {
        if (AutomaticGainControl.isAvailable()) {
            automaticGainControl.setEnabled(bl);
        } else {
            Toast.makeText(context, noAutomaticGainControl, Toast.LENGTH_SHORT).show();
        }
    }

    public void setAEC(boolean bl) {
        if (AcousticEchoCanceler.isAvailable()) {
            acousticEchoCanceler.setEnabled(bl);
        } else {
            Toast.makeText(context, noAECSupport, Toast.LENGTH_SHORT).show();
        }
    }

    public void setNoise(boolean bl) {
        if (NoiseSuppressor.isAvailable()) {
            noiseSuppressor.setEnabled(bl);
        } else {
            Toast.makeText(context, noNoiseSuppressor, Toast.LENGTH_SHORT).show();
        }
    }

    public Boolean getmicSwitch() {
        return micSwitch;
    }

    public void sp_start() {
        if (!micSwitch) {
            micSwitch = true;
            eng_start();
        } else {
            Toast.makeText(context, started, Toast.LENGTH_SHORT).show();
        }
    }

    public void sp_stop() {
        if (micSwitch) {
            this.micSwitch = false;
        } else {
            Toast.makeText(context, stopped, Toast.LENGTH_SHORT).show();
        }
    }

    private void eng_start() {
        audioRecord.startRecording();
        audioTrack.play();
        System.out.println(Thread.currentThread().getName());
        if (getmicSwitch()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_AUDIO);
                    while (getmicSwitch()) {
                        audioRecord.read(readbufer, 0, readbufer.length);
                        audioTrack.write(readbufer, 0, readbufer.length);
                    }
                    audioRecord.stop();
                    audioTrack.stop();
                }
            }).start();
        }
    }

    public void MicSpeaker() {
        int audioRecordbufferSize = AudioRecord.getMinBufferSize(
                sampleRateInHz,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);
        audioRecord = new AudioRecord(
                audioSource,
                sampleRateInHz,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                audioRecordbufferSize);
        int audioTrackbufferSize = AudioTrack.getMinBufferSize(
                sampleRateInHz,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT);
        audioTrack = new AudioTrack(
                AudioManager.STREAM_MUSIC,
                sampleRateInHz,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                audioTrackbufferSize,
                AudioTrack.MODE_STREAM);
        readbufer = new byte[(Math.min(audioRecordbufferSize, audioTrackbufferSize)) / 10];
        audioRecord_sessionId = audioRecord.getAudioSessionId();

        if (AcousticEchoCanceler.isAvailable()) {
            acousticEchoCanceler = AcousticEchoCanceler.create(audioRecord_sessionId);
        } else {
            Toast.makeText(context, noAECSupport, Toast.LENGTH_SHORT).show();
        }

        if (AutomaticGainControl.isAvailable()) {
            automaticGainControl = AutomaticGainControl.create(audioRecord_sessionId);
        } else {
            Toast.makeText(context, noAutomaticGainControl, Toast.LENGTH_SHORT).show();
        }

        if (NoiseSuppressor.isAvailable()) {
            noiseSuppressor = NoiseSuppressor.create(audioRecord_sessionId);
        } else {
            Toast.makeText(context, noNoiseSuppressor, Toast.LENGTH_SHORT).show();
        }
    }
}
