package com.example.asfchoosefile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.example.mylibrary.ChooseDocumentView;

public class MainActivity extends AppCompatActivity {
    // https://hanjie-oos.oss-cn-shenzhen.aliyuncs.com/%E5%B7%A5%E4%BD%9C%E4%BA%A4%E6%8E%A5%E6%B8%85%E5%8D%95.docx
    private ChooseDocumentView chooseDocumentView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chooseDocumentView=findViewById(R.id.chooseDocumentView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        chooseDocumentView.handleActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        chooseDocumentView.handleRequestPermissionsResult(requestCode,permissions,grantResults);
    }
    public void downLoadFile(View v){
        chooseDocumentView.openFileByUrl("https://hanjie-oos.oss-cn-shenzhen.aliyuncs.com/%E5%B7%A5%E4%BD%9C%E4%BA%A4%E6%8E%A5%E6%B8%85%E5%8D%95.docx");
    }
    public void openUri(View v){
        chooseDocumentView.openFileByUri(MainActivity.this, Uri.parse("content://com.android.externalstorage.documents/document/primary%3AAndroid%2Fdata%2Fcom.tencent.mm%2FMicroMsg%2FDownload%2F%E6%B7%B1%E5%9C%B3%E9%A1%B9%E7%9B%AE%E8%BF%9B%E5%BA%A6.docx"));
    }
    public void openAbsolute(View v){
        chooseDocumentView.openFileByAbsolutePath(MainActivity.this,"/storage/emulated/0/Android/data/com.example.asfchoosefile/files/Documents/深圳项目进度.docx");
    }
}