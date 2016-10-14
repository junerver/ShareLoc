package com.junerver.shareloc;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.baidu.mapapi.model.LatLng;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class LocationService extends Service {

    private static final String SERVER_IP = "192.168.31.186";
    private Socket mSocket;
    private BufferedReader mBufferedReader;
    private BufferedWriter mBufferedWriter;

    public LocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //注册总线
        EventBus.getDefault().register(this);
    }

    @Override
    public int onStartCommand(Intent intent, final int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //连接服务器
                    mSocket = new Socket(SERVER_IP, 8000);
                    Logger.d("创建套接字成功");
                    mBufferedReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
                    mBufferedWriter = new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //从bufferedreader中不断读取服务器传来的数据
                while (true) {
                    try {
                        String locationStr;
                        while ((locationStr = mBufferedReader.readLine()) != null) {
                            String[] lat_lang = locationStr.trim().split(",");
                            LatLng latlng = new LatLng(Double.parseDouble(lat_lang[0]), Double.parseDouble(lat_lang[1]));
                            //通过总线像activity传递位置数据
                            EventBus.getDefault().post(new LocationEvent(latlng,false));
                            Logger.d("已经从服务器接收到数据" + latlng);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Subscribe
    public void onLocationEvent(LocationEvent event) {
        if (event.isPublish()) {
            //向服务器提交位置
            LatLng latLng = event.getLatLng();
            String latlngStr = latLng.latitude + "," + latLng.longitude + "\n";
            try {
                mBufferedWriter.write(latlngStr);
                mBufferedWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
