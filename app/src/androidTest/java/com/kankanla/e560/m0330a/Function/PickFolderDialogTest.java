package com.kankanla.e560.m0330a.Function;

import android.content.Context;
import android.os.Environment;
import android.support.test.InstrumentationRegistry;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by E560 on 2017/04/15.
 */
public class PickFolderDialogTest {
    @Test
    public void test1() throws Exception {
        Log.i("4444444444444444444","333333333333333333");
        System.out.println("4444444444444444444444444444444444444444444");
        File f = new File(String.valueOf(Environment.getExternalStorageDirectory()));
        List<File> fs = new ArrayList<File>();
        System.out.println("4444444444444444444444444444444444444444444");
    }

    @Test
    public void createDialogLayout() throws Exception {

    }

}