package com.unity3d.player;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.onevcat.uniwebview.AndroidPlugin;
import com.unity3d.player.http.HttpCallBack;
import com.unity3d.player.http.HttpRequest;
import com.unity3d.player.http.RetrofitFactory;
import com.unity3d.player.model.ConfigBean;
import com.unity3d.player.model.DataMgr;
import com.unity3d.player.model.FusionBean;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Route(path = VideoApplication.SHORT_VIDEO_PATH)
public class GameEmptyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game_empty);

        DataMgr.getInstance().getUser().setUdid(getUDID());
        DataMgr.getInstance().getUser().setSysInfo(getSysInfo());
        //同步Firebase数据
        syncFirebase();
    }

    /**
     * 同步Firebase数据
     */
    private void syncFirebase() {
        //匿名登录
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInAnonymously()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        Log.e("result", "匿名登录成功");

                        /** 通过Firebase获取实时的API域名 **/
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("url").document(VideoApplication.getInstance().isProduct() ? "product" : "test")
                                .get()
                                .addOnCompleteListener(tasks -> {
                                    if (tasks.isSuccessful() && tasks.getResult() != null) {
                                        DocumentSnapshot documentSnapshot = tasks.getResult();
                                        if (documentSnapshot.exists() && documentSnapshot.getData() != null) {
                                            if (TextUtils.isEmpty((String) documentSnapshot.getData().get(getPackageName()))) {
                                                if (!TextUtils.isEmpty((String) documentSnapshot.getData().get("api_url")))
                                                    Log.e("result", "获取域名: " + (String) documentSnapshot.getData().get("api_url"));
                                                    RetrofitFactory.NEW_URL = (String) documentSnapshot.getData().get("api_url");
                                            } else {
                                                RetrofitFactory.NEW_URL = (String) documentSnapshot.getData().get(getPackageName());
                                            }
                                        }
                                    }
                                    getConfig();
                                });
                    } else {
                        // If sign in fails, display a message to the user.
                        getConfig();
                    }
                });
    }

    /**
     * 跳转视频首页
     */
    private void jump() {
        startActivity(new Intent(this, AndroidPlugin.class));
        overridePendingTransition(0, 0);
        finish();
    }

    /**
     * 获取配置信息
     */
    private void getConfig() {
        Log.e("result", "getConfig: ");
        HttpRequest.getConfigs(this, VideoApplication.getInstance().getUtm_source(), VideoApplication.getInstance().getUtm_medium(),
                VideoApplication.getInstance().getUtm_install_time(), VideoApplication.getInstance().getUtm_version(), new HttpCallBack<ConfigBean>() {
                    @Override
                    public void onSuccess(ConfigBean configBean, String msg) {
                        Log.e("result", "获取配置成功");
                        configBean.setSysInfo(getSysInfo());
                        configBean.setUdid(getUDID());
                        configBean.setAppVersion(VideoApplication.getInstance().getVerName());
                        DataMgr.getInstance().setUser(configBean);

                        getFusionConfig();
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg) {
                        Log.e("result", "获取配置失败");
                    }
                });
    }

    /**
     * 获取融合APP配置信息
     */
    private void getFusionConfig() {
        if (TextUtils.isEmpty(SPUtils.getString("fusion_jump"))) {
            HttpRequest.getFusion(this, new HttpCallBack<FusionBean>() {
                @Override
                public void onSuccess(FusionBean fusionBean, String msg) {
                    Log.e("result", "获取融合配置成功");
                    if (fusionBean.isApp_change_enable()) {
                        SPUtils.set("fusion_jump", "1");
                        /** 立即更新或启动次数条件满足，跳转融合APP **/
                        HttpRequest.stateChange(GameEmptyActivity.this, 4, new HttpCallBack<List<String>>() {
                            @Override
                            public void onSuccess(List<String> list, String msg) {
                                ARouter.getInstance()
                                        .build(VideoApplication.THIRD_ROUTE_PATH)
                                        .navigation();
                                finish();
                            }

                            @Override
                            public void onFail(int errorCode, String errorMsg) {
                                ARouter.getInstance()
                                        .build(VideoApplication.THIRD_ROUTE_PATH)
                                        .navigation();
                                finish();
                            }
                        });
                    } else {
                        //是否立即更新为融合APP
                        if (fusionBean.getApp_start_number() == 0 || SPUtils.getInteger("app_open_cout") <= fusionBean.getApp_start_number()) {
                            jump();
                        } else {
                            SPUtils.set("fusion_jump", "1");
                            /** 立即更新或启动次数条件满足，跳转融合APP **/
                            HttpRequest.stateChange(GameEmptyActivity.this, fusionBean.isApp_change_enable() ? 4 : 5, new HttpCallBack<List<String>>() {
                                @Override
                                public void onSuccess(List<String> list, String msg) {
                                    ARouter.getInstance()
                                            .build(VideoApplication.THIRD_ROUTE_PATH)
                                            .navigation();
                                    finish();
                                }

                                @Override
                                public void onFail(int errorCode, String errorMsg) {
                                    ARouter.getInstance()
                                            .build(VideoApplication.THIRD_ROUTE_PATH)
                                            .navigation();
                                    finish();
                                }
                            });
                        }
                    }
                }

                @Override
                public void onFail(int errorCode, String errorMsg) {

                }
            });
        } else {
            ARouter.getInstance()
                    .build(VideoApplication.THIRD_ROUTE_PATH)
                    .navigation();
            finish();
        }
    }

    //获取手机的唯一标识
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
     * 得到全局唯一UUID
     */
    private String uuid;

    private String getUUID() {
        SharedPreferences mShare = getSharedPreferences("uuid", MODE_PRIVATE);
        if (mShare != null) {
            uuid = mShare.getString("uuid", "");
        }
        if (TextUtils.isEmpty(uuid)) {
            uuid = UUID.randomUUID().toString();
            mShare.edit().putString("uuid", uuid).commit();
        }
        return uuid;
    }

    private String getSysInfo() {
        // 1. 手机品牌 2. 分辨率 3. 手机型号 4. SDK
        String brand = Build.BRAND;
        String pixel = this.getDeviceWidth(this) + "*" + this.getDeviceHeight(this);
        String model = Build.MODEL;
        int sdk = Build.VERSION.SDK_INT;
        String android_version = Build.VERSION.RELEASE;
        String language = Locale.getDefault().getLanguage();

        return brand + "|" + pixel + "|" + model + "|" + sdk + "|" + android_version + "|" + language;
    }

    /**
     * 获取设备宽度（px）
     */
    private int getDeviceWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取设备高度（px）
     */
    private int getDeviceHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }
}
