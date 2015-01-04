package org.mo.pmas.activity.application;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import cn.bmob.v3.Bmob;
import org.mo.common.util.FileUtil;
import org.mo.common.util.ToastUtil;
import org.mo.pmas.comm.Constant;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by moziqi on 2014/12/26 0026.
 */
public class PmasAppliaction extends Application {
    private List<Activity> activityList = new LinkedList<Activity>();

    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this, Constant.BMOBNAME);
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

}
