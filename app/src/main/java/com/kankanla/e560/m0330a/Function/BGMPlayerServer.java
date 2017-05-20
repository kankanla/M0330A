package com.kankanla.e560.m0330a.Function;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.kankanla.e560.m0330a.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by E560 on 2017/04/30.
 */

public class BGMPlayerServer extends Service {
    private MediaPlayer mediaPlayer;
    private View view;
    private ArrayList<File> fileArrayList;
    private File now_playing_filename;

    public final int LOOP_CYCLE_mode = 1;
    public final int ONE_CYCLE_mode = 2;
    public final int LOOP_SINGLE_mode = 3;
    private int cycle_mode;

    public final int PLAYER_STOP = 1;
    public final int PLAYER_NEXT = 2;
    public final int PLAYER_PAUSE = 3;
    public int player_status;

    public int getMode() {
        return cycle_mode;
    }

    public void setMode(int cycle_mode) {
        this.cycle_mode = cycle_mode;
    }

    public void setPlayer_status(int player_status) {
        this.player_status = player_status;
    }

    public int getPlayer_status() {
        return player_status;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    public boolean setMediaPlayer(MediaPlayer mediaPlayer, View view) {
        this.mediaPlayer = mediaPlayer;
        this.view = view;
        return true;
    }

    public interface onAddFileArrayListSucceeded {
        void ListSucceeded();
    }

    public void setFileArrayList(File file, onAddFileArrayListSucceeded callback) {
        File selectedFile = file;
        File[] temp = file.getParentFile().listFiles();
        fileArrayList.clear();
        for (File file1 : temp) {
            fileArrayList.add(file1);
        }
        callback.ListSucceeded();
    }

    public void startPlayer(final File file) {
        if (fileArrayList.size() > 0) {
            final int x = fileArrayList.indexOf(file);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mediaPlayer.reset();
                    try {
                        if (getPlayer_status() == PLAYER_NEXT) {
                            now_playing_filename = fileArrayList.get(x);
                            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                            mediaPlayer.setDataSource(now_playing_filename.toString());
                            mediaPlayer.prepareAsync();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            TextView textView = (TextView) view.findViewById(R.id.nowplaying_name);
                            textView.setText(now_playing_filename.getName());

                            switch (getPlayer_status()) {
                                case PLAYER_STOP:
                                    mp.stop();
                                    break;
                                case PLAYER_PAUSE:
                                    mp.pause();
                                    break;
                                case PLAYER_NEXT:
                                    mp.start();
                                    break;
                            }
                        }
                    });
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mp.reset();
                            switch (cycle_mode) {
                                case ONE_CYCLE_mode:
                                    if (fileArrayList.size() > x + 1) {
                                        File temp = fileArrayList.get(x + 1);
                                        startPlayer(temp);
                                        break;
                                    }
                                    mp.stop();
                                    break;

                                case LOOP_SINGLE_mode:
                                    File temp2 = fileArrayList.get(x);
                                    startPlayer(temp2);
                                    break;

                                case LOOP_CYCLE_mode:
                                    File temp3 = fileArrayList.get(x + 1);
                                    if (temp3.isFile()) {
                                        startPlayer(temp3);
                                    } else {
                                        startPlayer(fileArrayList.get(0));
                                    }
                                    break;

                                default:
                                    if (fileArrayList.size() < x + 1) {
                                        File temp = fileArrayList.get(x + 1);
                                        startPlayer(temp);
                                        return;
                                    }
                                    mp.stop();
                                    break;
                            }
                        }
                    });
                }
            }).start();
        }
    }

    public ArrayList<File> getFileArrayList() {
        if (fileArrayList.size() > 0) {
            return fileArrayList;
        } else {
            return null;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        fileArrayList = new ArrayList<File>();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public class MyBinder extends Binder {
        public BGMPlayerServer getBGMPlayerServer() {
            return BGMPlayerServer.this;
        }
    }
}
