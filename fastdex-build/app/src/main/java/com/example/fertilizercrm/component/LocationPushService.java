package com.example.fertilizercrm.component;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.fertilizercrm.common.utils.Logger;

/**
 * 业务员位置信息上送服务
 */
public class LocationPushService extends Service {
    /**
     * 每隔多少秒上传一次信息
     */
    private static final int INTERVAL = 30;

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.d("LocationPushService: onCreate");


    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
