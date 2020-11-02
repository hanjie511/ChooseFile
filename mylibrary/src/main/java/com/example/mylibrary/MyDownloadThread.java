package com.example.mylibrary;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.ValueCallback;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class MyDownloadThread extends AsyncTask<Integer,Integer,String> {
    private String path;
    private Context context;
    private ProgressBar progressBar;
    private TextView progressText;
    private AlertDialog alertDialog;
    public MyDownloadThread(Context context, String url){
        this.path=url;
        this.context=context;
        this.progressBar=progressBar;
        this.progressText=progressText;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        View view= LayoutInflater.from(context).inflate(R.layout.peogress_dialog_hj511,null,false);
        progressBar=view.findViewById(R.id.progressBar_hj511);
        progressText=view.findViewById(R.id.progressText_hj511);
        alertDialog=new AlertDialog.Builder(context).setTitle("正在下载该文件:").setView(view).create();
        alertDialog.show();
    }

    @Override
    protected String doInBackground(Integer... integers) {
        URL url = null;
        File tempFile=null;
        try {
            url = new URL(path);
            InputStream is = url.openStream();
            int totalLength=url.openConnection().getContentLength();
            System.out.println("totalLength:"+totalLength);
            //截取最后的文件名
            String end = path.substring(path.lastIndexOf("."));
            //打开手机对应的输出流,输出到文件中
            tempFile=createTempFile(end);
            OutputStream os = new FileOutputStream(tempFile);
            byte[] buffer = new byte[1024];
            int len = 0;
            int currentLen=0;
            //从输入六中读取数据,读到缓冲区中
            while((len = is.read(buffer)) > 0)
            {
                os.write(buffer,0,len);
                currentLen=currentLen+len;
                publishProgress(currentLen,totalLength);
                Thread.sleep((long) 0.9);
            }
            //关闭输入输出流
            is.close();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tempFile.getAbsolutePath();
    }
    private File createTempFile(String suffix) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "temp_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File file = File.createTempFile(
                imageFileName,  /* prefix */
                suffix,         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        return file;
    }
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        alertDialog.dismiss();
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
        int ret = QbSdk.openFileReader(context, s, params, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String s) {

            }
        });
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        progressBar.setMax(values[1]);
        progressBar.setProgress(values[0]);
        float percent=((float)values[0]/(float)values[1])*100;
        progressText.setText(""+(int)percent+"%");
    }
}
