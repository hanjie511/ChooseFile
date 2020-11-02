package com.example.mylibrary;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.ValueCallback;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ChooseDocumentView extends LinearLayout{
    private ImageView addFile_hj511;
    private ListView listView_chooseFile_hj511;
    private Context context;
    private List<String> pathList=new ArrayList<>();
    private List<Uri> uriList=new ArrayList<>();
    private MyListAdapter adapter;
    private Activity activity;
    private String currentFilePath="";
    public final int REQUEST_PERMISSION_CODE=100;
    private FileChangedListener fileChangedListener;
    private ProgressBar progressBar;
    private TextView progressText;
    public ChooseDocumentView(Context context) {
        super(context);
        this.context=context;
        activity= (Activity) context;
        initView();
    }
    public ChooseDocumentView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        activity= (Activity) context;
        initView();
    }
    public void setFileChangedListener(FileChangedListener fileChangedListener){
        this.fileChangedListener=fileChangedListener;
    }
    private void initView(){
        View view= LayoutInflater.from(context).inflate(R.layout.hanjie511_choose_file_layout,null,false);
        addFile_hj511=view.findViewById(R.id.addFile_hj511);
        listView_chooseFile_hj511=view.findViewById(R.id.listView_chooseFile_hj511);
        adapter=new MyListAdapter(context,R.layout.item_listview_choose_file_hj511,pathList);
        listView_chooseFile_hj511.setAdapter(adapter);
        addFile_hj511.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
                    ActivityCompat.requestPermissions(activity,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
                }else {
                    goChooseFile();
                }
            }
        });
        listView_chooseFile_hj511.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent=new Intent(context,BrowserActivity.class);
//                intent.putExtra("path",pathList.get(position));
//                context.startActivity(intent);
                openFileReader(context,pathList.get(position));
            }
        });
        QbSdk.forceSysWebView();
        QbSdk.initX5Environment(context, new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {

            }

            @Override
            public void onViewInitFinished(boolean b) {

            }
        });
        addView(view);
    }
    private void openFileReader(Context context, String pathName)
    {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("local", "true");
        JSONObject Object = new JSONObject();
        try
        {
            Object.put("pkgName",context.getApplicationContext().getPackageName());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        params.put("menuData",Object.toString());
        QbSdk.getMiniQBVersion(context);
        int ret = QbSdk.openFileReader(context, pathName, params, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String s) {

            }
        });

    }
    public void handleRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        switch(requestCode){
            case REQUEST_PERMISSION_CODE:
                if((permissions.length==grantResults.length)&&(grantResults[0]== PackageManager.PERMISSION_GRANTED)) {
                    goChooseFile();
                }else {
                    Toast.makeText(context, "未授权", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void handleActivityResult(int requestCode, int resultCode, final Intent data) {
        // TODO Auto-generated method stub
        switch(requestCode) {
            case 101:
                if(data!=null) {
                    System.out.println("Uri:------"+data.getData());
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            getChooseFilePath(data.getData());
                        }
                    }).start();
                }
                break;
        }
    }
    /**
     * 拿到文件的Uri后，将目标文件复制到该App对应目录下的方法
     * @param uri
     */
    private void getChooseFilePath(Uri uri){
        System.out.println("uri:"+uri);
        String suffix="";
        InputStream fis = null;
        OutputStream fos = null;
        try {
            String uriStr=uri.toString();
            int lastIndex=uri.toString().lastIndexOf(".");
            suffix=(String) uriStr.subSequence(lastIndex, uriStr.length());
            fis = context.getContentResolver().openInputStream(uri);
            Cursor cursor=context.getContentResolver().query(uri,new String[]{MediaStore.Files.FileColumns.DISPLAY_NAME,MediaStore.Files.FileColumns.TITLE},null,null,null);
            String displayName="";
            if(cursor!=null){
                while(cursor.moveToNext()){
                    displayName=cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME));
                }
            }
            System.out.println("displayName:"+displayName);
            fos = new FileOutputStream(createImageFile(suffix,displayName));
            byte[] buf = new byte[4096];
            int i;
            while ((i = fis.read(buf)) != -1) {
                fos.write(buf, 0, i);
            }
            pathList.add(currentFilePath);
            uriList.add(uri);
            if(fileChangedListener!=null){
                fileChangedListener.getPathList(pathList);
            }
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
                fos.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }
    private File createImageFile(String suffix,String fileDisplayName) throws IOException {
        int lastIndex=fileDisplayName.lastIndexOf(".");
        String fileName=fileDisplayName.substring(0,lastIndex);
        String path=context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)+"/"+fileName+suffix;
        File file=new File(path);
        // Save a file: path for use with ACTION_VIEW intents
        System.out.println("fileName:"+fileName);
        currentFilePath = file.getAbsolutePath();
        String name=file.getName();
        System.out.println("currentFilePath:"+currentFilePath);
        System.out.println("name:"+name);
        return file;
    }
    private void goChooseFile() {
        Intent intent = new Intent( Intent.ACTION_OPEN_DOCUMENT );
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/*");
        activity.startActivityForResult(intent, 101);
    }
    public void openFileByUri(Context context,Uri uri){
        String suffix="";
        InputStream fis = null;
        OutputStream fos = null;
        try {
            String uriStr=uri.toString();
            int lastIndex=uri.toString().lastIndexOf(".");
            suffix=(String) uriStr.subSequence(lastIndex, uriStr.length());
            fis = context.getContentResolver().openInputStream(uri);
            Cursor cursor=context.getContentResolver().query(uri,new String[]{MediaStore.Files.FileColumns.DISPLAY_NAME,MediaStore.Files.FileColumns.TITLE},null,null,null);
            String displayName="";
            if(cursor!=null){
                while(cursor.moveToNext()){
                    displayName=cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME));
                }
            }
            System.out.println("displayName:"+displayName);
            fos = new FileOutputStream(createImageFile(suffix,displayName));
            byte[] buf = new byte[4096];
            int i;
            while ((i = fis.read(buf)) != -1) {
                fos.write(buf, 0, i);
            }
            openFileByAbsolutePath(context,currentFilePath);
        }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
                fos.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    public void openFileByAbsolutePath(Context context,String path){
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("local", "true");
        JSONObject Object = new JSONObject();
        try
        {
            Object.put("pkgName",context.getApplicationContext().getPackageName());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        params.put("menuData",Object.toString());
        QbSdk.getMiniQBVersion(context);
        int ret = QbSdk.openFileReader(context, path, params, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String s) {

            }
        });
    }
    public void openFileByUrl(String netUrl){
        MyDownloadThread myDownloadThread=new MyDownloadThread(context,netUrl);
        myDownloadThread.execute();
    }
    public interface FileChangedListener{
        void getPathList(List<String> pathList);
    }
}
