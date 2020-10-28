package com.example.mylibrary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class MyListAdapter extends ArrayAdapter<String> {
    private List<String> pathList;
    public MyListAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        pathList=objects;
    }

    @Override
    public int getCount() {
        return pathList.size();
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return pathList.get(position);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder=null;
        String path=pathList.get(position);
        if(convertView==null){
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listview_choose_file_hj511,null,true);
            holder=new ViewHolder();
            holder.typeIcon_imageView_hj511=convertView.findViewById(R.id.typeIcon_imageView_hj511);
            holder.cancelIcon_imageView_hj511=convertView.findViewById(R.id.cancelIcon_imageView_hj511);
            holder.textView_hj511=convertView.findViewById(R.id.textView_hj511);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
        }
        int lastSeprator=path.lastIndexOf("/");
        int lastSuffix=path.lastIndexOf(".");
        String fileName=path.substring(lastSeprator+1,path.length());
        String suffix=path.substring(lastSuffix,path.length());
        if(suffix.contains("doc")){
            holder.typeIcon_imageView_hj511.setImageResource(R.drawable.ic_word);
        }else if(suffix.contains("xls")){
            holder.typeIcon_imageView_hj511.setImageResource(R.drawable.ic_excel);
        }else if(suffix.contains("pdf")){
            holder.typeIcon_imageView_hj511.setImageResource(R.drawable.ic_pdf);
        }
        holder.textView_hj511.setText(fileName);
        holder.cancelIcon_imageView_hj511.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pathList.remove(position);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }
    class ViewHolder{
        ImageView typeIcon_imageView_hj511;
        ImageView cancelIcon_imageView_hj511;
        TextView textView_hj511;
    }
}
