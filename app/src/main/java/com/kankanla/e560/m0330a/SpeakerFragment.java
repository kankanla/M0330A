package com.kankanla.e560.m0330a;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kankanla.e560.m0330a.Function.Speaker;

/**
 * Created by E560 on 2017/04/08.
 */

public class SpeakerFragment extends android.support.v4.app.Fragment {

    protected Context context;
    protected Speaker speaker;
    private Button bt1, bt2, bt3, bt4;

    public SpeakerFragment(Context context, Speaker speaker) {
        this.context = context;
        this.speaker = speaker;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_speaker, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        speaker_start();
    }

    private void speaker_start() {
        speaker.MicSpeaker();
        set_button();
    }

    private void set_button() {
        bt1 = (Button) getActivity().findViewById(R.id.sp_ACE);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speaker.setAEC(true);
            }
        });

        bt2 = (Button) getActivity().findViewById(R.id.sp_ACEoff);
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speaker.setAEC(false);
            }
        });

        bt3 = (Button) getActivity().findViewById(R.id.sp_stop);
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speaker.sp_stop();
            }
        });

        bt4 = (Button) getActivity().findViewById(R.id.sp_start);
        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speaker.sp_start();
            }
        });
    }
}
