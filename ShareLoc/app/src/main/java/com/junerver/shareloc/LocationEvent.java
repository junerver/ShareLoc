package com.junerver.shareloc;

import com.baidu.mapapi.model.LatLng;

/**
 * Created by junerver on 2016/8/12.
 */
public class LocationEvent {
    private LatLng mLatLng;     //位置信息
    private boolean isPublish;  //是提交服务器还是从服务器接收

    public LocationEvent(LatLng latLng, boolean isPublish) {
        mLatLng = latLng;
        this.isPublish = isPublish;
    }

    public LatLng getLatLng() {
        return mLatLng;
    }

    public void setLatLng(LatLng latLng) {
        mLatLng = latLng;
    }

    public boolean isPublish() {
        return isPublish;
    }

    public void setPublish(boolean publish) {
        isPublish = publish;
    }
}
