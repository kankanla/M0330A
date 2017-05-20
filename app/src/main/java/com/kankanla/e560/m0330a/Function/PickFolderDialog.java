package com.kankanla.e560.m0330a.Function;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.kankanla.e560.m0330a.R;

import java.io.File;
import java.io.FilenameFilter;


/**
 * Created by E560 on 2017/04/15.
 */

public class PickFolderDialog extends DialogFragment {
    private final File defultPath = new File(Environment.getExternalStorageDirectory().toString());
    private View view;
    private ListView listView;
    private File[] temp2;
    private int screen_x, screen_y;
    private Button previous_button, cancel_button;
    private File absolutePath;
    private onSelectFileListener mOnSelectFileListener;

    public void setPickFolderDialog(onSelectFileListener mOnSelectFileListener) {
        this.mOnSelectFileListener = mOnSelectFileListener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        set_screen();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater linearInterpolator = getActivity().getLayoutInflater();
        view = linearInterpolator.inflate(R.layout.pickfolderdialog_layout, null);
        builder.setView(view);
        return builder.create();
    }

    /*
    得到屏幕大小
     */
    private void set_screen() {
        WindowManager manager = getActivity().getWindowManager();
        Display display = manager.getDefaultDisplay();
        Point point = new Point(screen_x, screen_y);
        display.getSize(point);
    }

    @Override
    public void onStart() {
        super.onStart();
        pickFolder(defultPath);
    }

    private void pickFolder(File file) {
        temp2 = file.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                absolutePath = dir;
                File temp = new File(dir, name);
                if (temp.isDirectory()) {
                    return true;
                }
                if (name.endsWith(".mp3") || name.endsWith(".mp4") || name.endsWith(".wav")) {
                    return true;
                }
                return false;
            }
        });

        list_adapter la = new list_adapter(temp2);
        listView = (ListView) view.findViewById(R.id.picListView);
        listView.setAdapter(la);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (temp2[position].isDirectory()) {
//                    如果是文件继续打开
                    pickFolder(temp2[position]);
                } else {
//                    如果是文件，文件File返回给调用者
                    mOnSelectFileListener.gfile(temp2[position]);
                }
            }
        });

        previous_button = (Button) view.findViewById(R.id.pick_folder_return);
        previous_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (absolutePath == null) {
                    absolutePath = defultPath;
                }
                if (!absolutePath.getPath().equals(defultPath.getPath())) {
                    pickFolder(absolutePath.getParentFile());
                } else {
                    pickFolder(defultPath);
                }
            }
        });

        cancel_button = (Button) view.findViewById(R.id.folder_cancel);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    /*
    返回结果的回调接口
     */
    public interface onSelectFileListener {
        void gfile(File file);
    }

    private class list_adapter extends BaseAdapter {
        private File[] files;

        public list_adapter(File[] files) {
            this.files = files;
        }

        @Override
        public int getCount() {
            return files.length;
        }

        @Override
        public Object getItem(int position) {
            return files[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LinearLayout linearLayout;
            TextView textView;
            ImageView imageView;
            ListView.LayoutParams layoutParams = new ListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            linearLayout = new LinearLayout(getActivity());
            linearLayout.setLayoutParams(layoutParams);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setVerticalGravity(Gravity.CENTER_VERTICAL);
            linearLayout.setPadding(16, 16, 16, 16);
            linearLayout.setLayoutMode(View.LAYOUT_DIRECTION_LOCALE);
            textView = new TextView(getActivity());
            textView.setPadding(10, 0, 0, 0);
            imageView = new ImageView(getActivity());
            linearLayout.addView(imageView);
            linearLayout.addView(textView);
            textView.setTextSize(16);
            textView.setText(files[position].getName());
            if (files[position].isDirectory()) {
                imageView.setBackgroundResource(R.drawable.ic_folder_open_black_24dp);
            } else {
                imageView.setBackgroundResource(R.drawable.ic_play_circle_outline_black_24dp);
            }
            return linearLayout;
        }
    }
}
