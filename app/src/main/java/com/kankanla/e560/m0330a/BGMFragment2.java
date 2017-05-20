package com.kankanla.e560.m0330a;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

import com.kankanla.e560.m0330a.Function.BGMPlayerServer;
import com.kankanla.e560.m0330a.Function.PickFolderDialog;

import java.io.File;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by E560 on 2017/05/03.
 */

public class BGMFragment2 extends Fragment implements ServiceConnection {
    private View view;
    private Context context;
    private MediaPlayer mediaPlayer;
    private BGMPlayerServer bgmPlayerServer;
    private boolean isBundled;
    private boolean isDebug = true;
    private float volume_L = 0.3f;
    private float volume_R = 0.3f;
    private SharedPreferences sharedPreferences;



    public BGMFragment2(Context context) {
        this.context = context;
        mediaPlayer = new MediaPlayer();
    }

    public void button_PickFolder() {
        Button button = (Button) view.findViewById(R.id.BGMFolder);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PickFolderDialog pickFolderDialog = new PickFolderDialog();
                pickFolderDialog.setPickFolderDialog(new PickFolderDialog.onSelectFileListener() {
                    @Override
                    public void gfile(final File file) {
                        if (isDebug) {
                            System.out.println("____File____" + file);
                            System.out.println("____getParentFile____" + file.getParentFile());
                        }
                        if (isBundled) {
                            bgmPlayerServer.setFileArrayList(file, new BGMPlayerServer.onAddFileArrayListSucceeded() {
                                @Override
                                public void ListSucceeded() {
                                    bgmPlayerServer.setPlayer_status(bgmPlayerServer.PLAYER_NEXT);
                                    bgmPlayerServer.startPlayer(file);
                                }
                            });
                            pickFolderDialog.dismiss();
                        }
                    }
                });
                pickFolderDialog.show(getActivity().getFragmentManager(), "PickFolder");
            }
        });
    }

    public void button_setMode() {
        if (isBundled) {
            bgmPlayerServer.setMode(bgmPlayerServer.ONE_CYCLE_mode);
        }
    }

    public void button_start() {
        if (isBundled) {
            Button button = (Button) view.findViewById(R.id.button_start);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mediaPlayer.start();
                    bgmPlayerServer.setPlayer_status(bgmPlayerServer.PLAYER_NEXT);
                }
            });
        }
    }

    public void button_pause() {
        if (isBundled) {
            Button button = (Button) view.findViewById(R.id.button_pause);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mediaPlayer.pause();
                    bgmPlayerServer.setPlayer_status(bgmPlayerServer.PLAYER_PAUSE);
                }
            });
        }
    }

    public void button_stop() {
        if (isBundled) {
            Button button = (Button) view.findViewById(R.id.button_stop);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mediaPlayer.stop();
                    bgmPlayerServer.setPlayer_status(bgmPlayerServer.PLAYER_STOP);
                }
            });
        }
    }

    public void button_volume() {

        final SeekBar seekBar_R = (SeekBar) view.findViewById(R.id.seekBar_R);
        final SeekBar seekBar_L = (SeekBar) view.findViewById(R.id.seekBar_L);
        seekBar_R.setProgress(0);
        seekBar_L.setProgress(0);
        seekBar_R.setMax(1000);
        seekBar_L.setMax(1000);
        seekBar_L.setProgress((int) (volume_L * 1000));
        seekBar_R.setProgress((int) (volume_R * 1000));

        seekBar_R.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mediaPlayer.setVolume(volume_L, (float) progress / 1000);
                volume_R = (float) progress / 1000;
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putFloat("volume_R", volume_R);
                editor.commit();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBar_L.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mediaPlayer.setVolume((float) progress / 1000, volume_R);
                volume_L = (float) progress / 1000;
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putFloat("volume_L", volume_L);
                editor.commit();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_bgm, null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Intent intent = new Intent(getContext(), BGMPlayerServer.class);
        getContext().bindService(intent, this, Context.BIND_AUTO_CREATE);
    }

    public void setdefultvolume(){
        sharedPreferences = getContext().getSharedPreferences("text", MODE_PRIVATE);
        volume_L = sharedPreferences.getFloat("volume_L", (float) 0.3);
        volume_R = sharedPreferences.getFloat("volume_R", (float) 0.3);
        mediaPlayer.setVolume(volume_L, volume_R);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        BGMPlayerServer.MyBinder myBinder = (BGMPlayerServer.MyBinder) service;
        bgmPlayerServer = myBinder.getBGMPlayerServer();
        isBundled = bgmPlayerServer.setMediaPlayer(mediaPlayer, view);
        button_PickFolder();
        button_setMode();
        setdefultvolume();
        button_stop();
        button_start();
        button_pause();
        button_volume();
    }

    private void test_t1() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            mediaPlayer.setAudioSessionId(99);
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
}
