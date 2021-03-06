package org.mo.pmas.activity.application;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.TextView;
import cn.bmob.v3.Bmob;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import org.mo.common.util.FileUtil;
import org.mo.common.util.ToastUtil;
import org.mo.pmas.bmob.entity.MyUser;
import org.mo.pmas.comm.Constant;
import org.mo.pmas.util.SharePreferenceUtil;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by moziqi on 2014/12/26 0026.
 */
public class PmasAppliaction extends Application {
    // 单例模式，才能及时返回数据
    private SharePreferenceUtil mSpUtil;
    public static final String PREFERENCE_NAME = "_sharedinfo";
    private List<Activity> activityList = new LinkedList<Activity>();
    private static PmasAppliaction pmasAppliaction;

    public LocationClient mLocationClient;
    public MyLocationListener mMyLocationListener;
    public TextView mLocationResult;

    public static PmasAppliaction getInstance() {
        return pmasAppliaction;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        pmasAppliaction = this;
        Bmob.initialize(this, Constant.BMOBNAME);
        mLocationClient = new LocationClient(this.getApplicationContext());
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
    }

    // 添加Activity到容器中
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    // 遍历所有Activity并finish
    public void exit() {
        for (Activity activity : activityList) {
            activity.finish();
        }
    }

    public synchronized SharePreferenceUtil getSpUtil() {
        if (mSpUtil == null) {
            String sharedName = PREFERENCE_NAME;
            mSpUtil = new SharePreferenceUtil(this, sharedName);
        }
        return mSpUtil;
    }

    public boolean isLogOut() {
        MyUser.logOut(getApplicationContext());//清除缓存用户对象
        MyUser currentUser = MyUser.getCurrentUser(getApplicationContext(), MyUser.class);
        if (currentUser == null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 没有登录就返回 请登录,有用户登录就显示当前用户
     *
     * @return
     */
    public String getCurrentUserName() {
        MyUser currentUser = MyUser.getCurrentUser(getApplicationContext(), MyUser.class);
        if (currentUser == null) {
            return "请登录";
        } else {
            return currentUser.getUsername();
        }
    }

    /**
     * 实现实位回调监听
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation){
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\ndirection : ");
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append(location.getDirection());
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
            }
            mLocationResult.setText(sb.toString());
            Log.i("BaiduLocationApiDem", sb.toString());
        }


    }
}
