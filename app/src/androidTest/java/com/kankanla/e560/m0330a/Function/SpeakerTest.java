package com.kankanla.e560.m0330a.Function;

import android.support.test.InstrumentationRegistry;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by E560 on 2017/04/01.
 */
public class SpeakerTest {
    @Test
    public void micSpeaker() throws Exception {
        Speaker speaker = new Speaker(InstrumentationRegistry.getContext());
        speaker.MicSpeaker();
    }

}