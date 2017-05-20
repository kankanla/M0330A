package com.kankanla.e560.m0330a;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kankanla.e560.m0330a.Function.BGMPlayer;
import com.kankanla.e560.m0330a.Function.PickFolderDialog;

import java.io.File;

import static android.app.Activity.RESULT_OK;


public class BGMFragment extends Fragment {
    private BGMPlayer bgmPlayer;
    private ViewGroup view;
    private File selectedFile;
    private Uri uri;
    private Context context;
    private PickFolderDialog pickFolderDialog;
    private MediaPlayer mediaPlayer;

    public BGMFragment(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = (ViewGroup) inflater.inflate(R.layout.fragment_bgm, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mediaPlayer == null) {
            System.out.println("--------------!mediaPlayer.isPlaying()--------------!mediaPlayer.isPlaying()----");
            mediaPlayer = new MediaPlayer();
            bgmPlayer = new BGMPlayer(context, mediaPlayer);
        }

        bot();
        folder();
        playStop();
        localfolder();
    }

    public void bot() {
        Button player = (Button) view.findViewById(R.id.button_start);
        player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bgmPlayer.BGMStart();
            }
        });
    }

    public void playStop() {
        Button stop = (Button) getActivity().findViewById(R.id.button_stop);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bgmPlayer.BGMStop();
            }
        });
    }

    public void localfolder() {
        Button button = (Button) view.findViewById(R.id.localfolder);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFolderDialog = new PickFolderDialog();
                pickFolderDialog.setPickFolderDialog(new PickFolderDialog.onSelectFileListener() {
                    @Override
                    public void gfile(File file) {
                        System.out.println("------------onSelectFileListener--------------");
                        System.out.println(file.toString());
                        bgmPlayer.PlayList(file);
                        pickFolderDialog.dismiss();
                    }
                });
                pickFolderDialog.show(getActivity().getFragmentManager(), "xx");
            }
        });
    }

    public void folder() {
        Button folder = (Button) view.findViewById(R.id.BGMFolder);
        folder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                while (!mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                    System.out.println("------------------------------!mediaPlayer.isPlaying()--------------!mediaPlayer.isPlaying()------");
                }
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("audio/*");
                Intent chooser = Intent.createChooser(intent, "2222");
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(chooser, 99);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 99 && resultCode == RESULT_OK) {
            uri = data.getData();
        }
    }
}
