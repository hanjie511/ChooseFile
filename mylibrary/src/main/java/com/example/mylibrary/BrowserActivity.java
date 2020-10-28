package com.example.mylibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.tencent.smtt.sdk.TbsReaderView;

import java.io.File;

public class BrowserActivity extends AppCompatActivity {
    private FrameLayout frameLayout_chooseDocument_hj511;
    private TbsReaderView readerView;
    private String path="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        frameLayout_chooseDocument_hj511=findViewById(R.id.frameLayout_chooseDocument_hj511);
        Intent intent=getIntent();
        if(intent.getStringExtra("path")!=null){
            path=intent.getStringExtra("path");
            openFile(path);
        }else{
            Toast.makeText(BrowserActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * 打开文件
     */
    private void openFile(String path) {
         readerView = new TbsReaderView(this, new TbsReaderView.ReaderCallback() {
            @Override
            public void onCallBackAction(Integer integer, Object o, Object o1) {

            }
        });
        //通过bundle把文件传给x5,打开的事情交由x5处理
        Bundle bundle = new Bundle();
        //传递文件路径
        bundle.putString("filePath", path);
        //加载插件保存的路径
        bundle.putString("tempPath", Environment.getExternalStorageDirectory() + File.separator + "temp");
        //加载文件前的初始化工作,加载支持不同格式的插件
        boolean b = readerView.preOpen(getFileType(path), false);
        if (b) {
            readerView.openFile(bundle);
        }
        frameLayout_chooseDocument_hj511.addView(readerView);
    }
    private String getFileType(String path) {
        String str = "";

        if (TextUtils.isEmpty(path)) {
            return str;
        }
        int i = path.lastIndexOf('.');
        if (i <= -1) {
            return str;
        }
        str = path.substring(i + 1);
        return str;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(readerView!=null){
            readerView.onStop();
        }
    }
}