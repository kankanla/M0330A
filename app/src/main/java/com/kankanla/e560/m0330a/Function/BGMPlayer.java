package com.kankanla.e560.m0330a.Function;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by E560 on 2017/04/01.
 */

public class BGMPlayer {
    private final String TAG = "-----BGMPlayer-------";
    private float maxVolume = (float) 1;
    private float minVolume = (float) 0.001;
    private int intNum;
    private boolean loop;
    private Context context;
    private File fileDir;
    private String fileName;
    private ArrayList<String> fileArray;
    private MediaPlayer mediaPlayer;

    public BGMPlayer(Context context, MediaPlayer mediaPlayer) {
        this.context = context;
        this.mediaPlayer = mediaPlayer;
        Log.i(TAG, String.valueOf(Build.VERSION.SDK_INT));
    }

    public void setMaxVolume(float maxVolume) {
        this.maxVolume = maxVolume;
    }

    public void setMinVolume(float minVolume) {
        this.minVolume = minVolume;
    }

    public float getMaxVolume() {
        return maxVolume;
    }

    public float getMinVolume() {
        return minVolume;
    }

    public void BGMStop() {
        while (mediaPlayer.isPlaying()){
            System.out.println("---------------------------isPlaying------------------------------");
            mediaPlayer.pause();
        }
    }

    public void BGMpause() {
        mediaPlayer.pause();
    }

    public void BGMStart() {
        mediaPlayer.start();
    }

    public void BGMrelease() {
        mediaPlayer.release();
    }

    public void BGMLoop(boolean bl) {
        loop = bl;
    }

    /*
    设置播放文件
     */
    public void PlayList(File file) {
        if (file.isFile()) {
            if (file.isFile()) {
                fileDir = file.getParentFile();
                fileName = file.getName();
            } else {
                fileDir = file;
                fileName = null;
            }
            fileArray = new ArrayList<String>();
            fileDir.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    if (name.endsWith(".mp3") || name.endsWith(".mp4") || name.endsWith(".wav")) {
                        if ((new File(dir, name)).isFile()) {
                            fileArray.add(name);
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            });

            PlayFile(fileName);
        } else {
            Log.i(TAG, "nofindthefile...................................................");
        }
    }

    /*
    播放文件
     */
    public void PlayFile(final String fileName) {
        Log.i(TAG, "FileName" + fileName);
        intNum = fileArray.indexOf(fileName);
        if (intNum == -1) {
            if (loop) {
                intNum = 0;
            } else {
                BGMStop();
            }
        }

        final Uri uri = Uri.fromFile(new File(fileDir, fileArray.get(intNum)));

        if (intNum < fileArray.size() - 1) {
            intNum++;
        } else {
            intNum = 0;
        }

        Thread bak = new Thread(new Runnable() {
            @Override
            public void run() {
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.reset();
                try {
                    mediaPlayer.setDataSource(context, uri);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                    PlayFile(fileArray.get(intNum));
                }
                mediaPlayer.start();
            }
        });
        bak.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
//                 嵌套，所以循环播放
                System.out.println("-------------onCompletion----------------");
                PlayFile(fileArray.get(intNum));
            }
        });
    }

    public void BGMVolume(float L_Volume, float R_Volume) {
        if (L_Volume > getMinVolume() && L_Volume < getMaxVolume() && R_Volume > getMinVolume() && R_Volume < getMaxVolume()) {
            mediaPlayer.setVolume(L_Volume, R_Volume);
        }
    }

    public static void SinglFile(final Context context, final Uri uri) throws IOException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final MediaPlayer mediaPlayer1 = new MediaPlayer();
                mediaPlayer1.reset();
                mediaPlayer1.setAudioStreamType(AudioManager.STREAM_MUSIC);
                try {
                    mediaPlayer1.setDataSource(context, uri);
                    mediaPlayer1.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer1.start();
                mediaPlayer1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mediaPlayer1.stop();
                        mediaPlayer1.release();
                    }
                });
            }
        }).start();
    }
}
