package com.gs.coderepository.utils;

import android.content.Intent;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.gs.coderepository.R;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 13203 on 2018/12/28.
 */

public class FileSelector extends AppCompatActivity {

    Toolbar toolbar;
    LinearLayout exit;
    ListView listView;
    TextView textView,back;

    //记录当前父文件夹
    File currentParent;
    //记录当前路经下的所有文件
    File[] currentFiles;

    int flag=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_selectot);
        initView();
        setToolbar();
        initFiles();
    }

    private void initFiles() {
        String[] names=getMountPathList();
        File[]files=new File[names.length];
        for (int i=0;i<names.length;i++){
            files[i]=new File(names[i]);
        }
        currentParent=new File("/storage");
        currentFiles=files;
        inflateListView(currentFiles);
    }

    private void initView() {
        listView=findViewById(R.id.file_list);
        textView=findViewById(R.id.file_path);
        toolbar=findViewById(R.id.toolbar);
        exit=toolbar.findViewById(R.id.exit);
        back=findViewById(R.id.back_folder);
        //为ListView的列表项单击事件绑定监听器
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                flag++;
                //用户单击了文件，直接返回
                if (currentFiles[position].isFile()) {
                    if (currentFiles[position].getName().contains(".lic")){
                        Intent intent=new Intent();
                        intent.putExtra("filename",currentFiles[position].getAbsolutePath());
                        setResult(RESULT_OK,intent);
                        finish();
                    }else {
                        Toast.makeText(FileSelector.this,"当前文件不是license文件，请选择license文件！",Toast.LENGTH_SHORT).show();
                        flag--;
                    }
                    return;
                }
                //获取单击文件夹下的所有文件
                File[] tmp = currentFiles[position].listFiles();
                if (tmp == null || tmp.length == 0) {
                    Toast.makeText(FileSelector.this, "当前文件夹下没有文件！" ,Toast.LENGTH_SHORT).show();
                    flag--;
                } else {
                    //获取用户单击的列表项对应的文件夹，设为当前父文件夹
                    currentParent = currentFiles[position];
                    //保存当前父文件夹内的所有文件和文件夹
                    currentFiles = tmp;
                    //再次更新ListView
                    inflateListView(currentFiles);
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag--;
                if (flag==0){
                    FileSelector.this.finish();
                    return;
                }
                //获取上一目录
                currentParent = currentParent.getParentFile();
                //列出当前目录下的所有文件
                currentFiles = currentParent.listFiles();
                //再次更新ListView
                inflateListView(currentFiles);
            }
        });
    }

    private void setToolbar() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileSelector.this.finish();
            }
        });
    }

    private void inflateListView(File[] files) {
        //创建List集合，元素是Map
        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < files.length; i++ ) {
            Map<String, Object> listItem = new HashMap<String, Object>();
            //如果当前File是文件夹，使用文件夹图标，其它使用文件图标
            if (files[i].isDirectory()) {
                listItem.put("icon", R.drawable.foder);
            } else {
                listItem.put("icon", R.drawable.file);
            }
            listItem.put("file", files[i].getName());
            //添加List项
            listItems.add(listItem);
        }
        //创建
        SimpleAdapter simpleAdapter = new SimpleAdapter(this
                ,listItems ,R.layout._item_file
                ,new String[]{"icon", "file"}
                ,new int[] {R.id.file_icon, R.id.file_name});
        //为ListView设置Adapter
        listView.setAdapter(simpleAdapter);
        try {
            textView.setText(currentParent.getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public  String[] getMountPathList() {
        StorageManager sm= (StorageManager) getSystemService(STORAGE_SERVICE);
        try {
            Method get=StorageManager.class.getMethod("getVolumePaths",(Class<?>[])null);
            String[] paths= (String[]) get.invoke(sm,(Object[]) null);
            return paths;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
