package com.kankanla.e560.m0330a.Function;

import android.media.MediaPlayer;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.test.InstrumentationRegistry;
import android.util.ArrayMap;
import android.webkit.MimeTypeMap;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

/**
 * Created by E560 on 2017/04/02.
 */
public class BGMPlayerTest {
    @Test
    public void test() throws Exception {
        File file = new File(Environment.getExternalStorageDirectory(), "Earth, Wind & Fire - September.mp3");
        BGMPlayer bgmPlayer = new BGMPlayer(InstrumentationRegistry.getTargetContext());
    }


}