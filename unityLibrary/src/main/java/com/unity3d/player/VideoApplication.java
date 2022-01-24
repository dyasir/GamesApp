package com.unity3d.player;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.unity3d.player.http.RetrofitFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.UUID;

public class VideoApplication extends Application {

    private static VideoApplication application;

    //Firebase
    private FirebaseAnalytics mFirebaseAnalytics;

    private String utm_source;
    private String utm_medium;
    private String utm_install_time;
    private String utm_version;

    /**
     * 自定义属性开始
     **/
    //是否是生产环境
    private boolean isProduct = false;

    //跳转视频APP的路由path
    public static final String SHORT_VIDEO_PATH = "/videolib/videosplash";
    //跳转第三方APP的路由path
    public static final String THIRD_ROUTE_PATH = "/third/mainactivity";

    public static VideoApplication getInstance() {
        return application;
    }

    public FirebaseAnalytics getmFirebaseAnalytics() {
        return mFirebaseAnalytics;
    }

    public String getUtm_source() {
        return utm_source;
    }

    public String getUtm_medium() {
        return utm_medium;
    }

    public String getUtm_install_time() {
        return utm_install_time;
    }

    public String getUtm_version() {
        return utm_version;
    }

    /**
     * 自定义属性开始
     **/
    public VideoApplication setDefaultUrl(String url) {
        RetrofitFactory.NEW_URL = url;
        return application;
    }

    public VideoApplication setProduct(boolean product) {
        isProduct = product;
        return application;
    }
    /** 自定义属性结束 **/

    /**
     * 获取自定义属性开始
     **/
    public boolean isProduct() {
        return isProduct;
    }

    /**
     * 获取自定义属性结束
     **/

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;

        //初始化路由
        ARouter.init(this);

        //初始化sp
        SPUtils.init(this);

        //连接Google pay分析来源
        connectGooglePlay();

        //初始化Google统计
        initFirebase();
    }

    /**
     * 初始化Firebase
     */
    private void initFirebase() {
        //开启Google分析
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);

        //启动次数
        SPUtils.set("app_open_cout", SPUtils.getInteger("app_open_cout") + 1);
    }

    /**
     * 谷歌自定义事件上报
     *
     * @param eventName
     * @param bundle
     */
    public void reportToGoogle(String eventName, Bundle bundle) {
        if (!TextUtils.isEmpty(eventName) && bundle != null)
            mFirebaseAnalytics.logEvent(eventName, bundle);
    }

    /**
     * 连接Google pay分析来源
     */
    private void connectGooglePlay() {
        if (TextUtils.isEmpty(SPUtils.getString("install_referrer_info"))) {
            InstallReferrerClient installReferrerClient = InstallReferrerClient.newBuilder(this).build();
            installReferrerClient.startConnection(new InstallReferrerStateListener() {
                @Override
                public void onInstallReferrerSetupFinished(int responseCode) {
                    switch (responseCode) {
                        case InstallReferrerClient.InstallReferrerResponse.OK:
                            // Connection established.

                            ReferrerDetails response;
                            try {
                                response = installReferrerClient.getInstallReferrer();
                                String referrerUrl = response.getInstallReferrer();
                                utm_install_time = response.getInstallBeginTimestampSeconds() + "";
                                utm_version = response.getInstallVersion();
                                if (!TextUtils.isEmpty(referrerUrl)) {
                                    utm_source = referrerUrl.substring(referrerUrl.indexOf("utm_source=") + 11, referrerUrl.indexOf("&"));
                                    utm_medium = referrerUrl.substring(referrerUrl.indexOf("utm_medium=") + 11);
                                    SPUtils.set("install_referrer_info", referrerUrl);
                                }
                                installReferrerClient.endConnection();
                            } catch (RemoteException e) {
                                e.printStackTrace();
                                installReferrerClient.endConnection();
                            }

                            break;
                        case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                            // API not available on the current Play Store app.
                            break;
                        case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                            // Connection couldn't be established.
                            break;
                    }
                }

                @Override
                public void onInstallReferrerServiceDisconnected() {

                }
            });
        }
    }

    /**
     * 获取版本号名称
     *
     * @return
     */
    public String getVerName() {
        String verName = "";
        try {
            verName = getPackageManager().
                    getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }

    /**
     * 判断APP是否是Debug模式
     *
     * @return
     */
    public boolean isDebugMode() {
        return getApplicationInfo() != null
                && (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取手机的唯一标识
     *
     * @return
     */
    private String getUDID() {
        StringBuilder deviceId = new StringBuilder();
        // 渠道标志
        deviceId.append("a");
        try {
            //IMEI（imei）
            TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            @SuppressLint("MissingPermission") String imei = tm.getDeviceId();
            if (!TextUtils.isEmpty(imei)) {
                deviceId.append("imei");
                deviceId.append(imei);
                return deviceId.toString();
            }
            //序列号（sn）
            @SuppressLint("MissingPermission") String sn = tm.getSimSerialNumber();
            if (!TextUtils.isEmpty(sn)) {
                deviceId.append("sn");
                deviceId.append(sn);
                return deviceId.toString();
            }
            //如果上面都没有， 则生成一个id：随机码
            String uuid = getUUID();
            if (!TextUtils.isEmpty(uuid)) {
                deviceId.append("id");
                deviceId.append(uuid);
                return deviceId.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            deviceId.append("id").append(getUUID());
        }
        return deviceId.toString();
    }

    /**
     * 优先读取本地uuid
     *
     * @return
     */
    private String getUUID() {
        SharedPreferences mShare = getSharedPreferences("uuid", MODE_PRIVATE);
        String uuid = "";
        if (mShare != null) {
            uuid = mShare.getString("uuid", "");
        }
        if (TextUtils.isEmpty(uuid)) {
            uuid = UUID.randomUUID().toString();
            mShare.edit().putString("uuid", uuid).commit();
        }
        return uuid;
    }
}
