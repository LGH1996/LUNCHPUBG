package com.example.lunchpubg;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {
    String str_0, str_1, str_2, str_3, str, path;
    Handler handler;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        str_0 = "+CVars=0B57292C3B3E3D1C0F101A1C3F292A35160E444F";
        str_1 = "+CVars=0B57292C3B3E3D1C0F101A1C3F292A34101D444F";
        str_2 = "+CVars=0B57292C3B3E3D1C0F101A1C3F292A31101E11444F";
        str_3 = "+CVars=0B57292C3B3E3D1C0F101A1C3F292A313D2B444F";
        str = "\n" + str_0 + "\n" + str_1 + "\n" + str_2 + "\n" + str_3 + "\n";
        path= getExternalCacheDir().getAbsolutePath()+ "/../../com.tencent.tmgp.pubgmhd/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/Config/Android/UserCustom.ini";
        timer = new Timer();
        Intent intent = getPackageManager().getLaunchIntentForPackage("com.tencent.tmgp.pubgmhd");
        if (intent != null ) {
            handler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    switch (msg.what) {
                        case 0x00:
                            Toast.makeText(MainActivity.this, "配置文件写入成功,已开启极限帧率", Toast.LENGTH_SHORT).show();
                            break;
                        case 0x01:
                            Toast.makeText(MainActivity.this, "配置文件写入失败,请关闭游戏后重新操作", Toast.LENGTH_LONG).show();
                            break;
                    }
                    return true;
                }
            });
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                Toast.makeText(this, "请授予应用<文件读取权限>", Toast.LENGTH_SHORT).show();
            } else {
                if (check()){
                    startActivity(intent);
                    handler.sendEmptyMessage(0x00);
                }else {
                    try {
                        FileWriter writer = new FileWriter(path,true);
                        writer.append(str);
                        writer.close();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                if (check()){
                                    handler.sendEmptyMessage(0x00);
                                }else {
                                    handler.sendEmptyMessage(0x01);
                                }
                            }
                        }, 5000);
                    } catch (Throwable e) {
                        Toast.makeText(this, "读写配置文件出现错误\n"+e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } else {
            Toast.makeText(this, "请确保<和平精英>已安装", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        finish();
    }
    private boolean check(){
        try {
            Scanner scanner = new Scanner(new FileInputStream(path));
            StringBuilder stringBuilder = new StringBuilder();
            while (scanner.hasNextLine())
                stringBuilder.append(scanner.nextLine());
            scanner.close();
            String s = stringBuilder.toString();
            return  (s.contains(str_0) && s.contains(str_1) && s.contains(str_2) && s.contains(str_3));
        } catch (IOException e) {return false; }
    }

}
