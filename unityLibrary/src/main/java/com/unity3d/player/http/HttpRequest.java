package com.unity3d.player.http;

import androidx.lifecycle.LifecycleOwner;

import com.unity3d.player.VideoApplication;
import com.unity3d.player.model.ConfigBean;
import com.unity3d.player.model.DataMgr;
import com.unity3d.player.model.FusionBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;

public class HttpRequest {

    static ApiRequest newApi;

    static {
        newApi = RetrofitFactory.getInstance().initNewRetrofit().create(ApiRequest.class);
    }

    /**
     * 获取配置
     *
     * @param activity
     * @param callBack
     */
    public static void getConfigs(LifecycleOwner activity, String utm_source, String utm_medium, String install_time, String version,
                                  final HttpCallBack<ConfigBean> callBack) {
        newApi.getConfigs(DataMgr.getInstance().getUser() != null ? DataMgr.getInstance().getUser().getUdid() : "",
                VideoApplication.getInstance().getVerName(), "1",
                DataMgr.getInstance().getUser() != null ? DataMgr.getInstance().getUser().getSysInfo() : "",
                VideoApplication.getInstance().getPackageName(), utm_source, utm_medium, install_time, version)
                .compose(RxSchedulers.io_main())
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(activity)))
                .subscribe(new ApiObserver<ConfigBean>() {

                    @Override
                    public void onSuccess(ConfigBean demo, String msg) {
                        callBack.onSuccess(demo, msg);
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg) {
                        callBack.onFail(errorCode, errorMsg);
                    }
                });
    }

    /**
     * 获取融合APP配置
     *
     * @param activity
     * @param callBack
     */
    public static void getFusion(LifecycleOwner activity, final HttpCallBack<FusionBean> callBack) {
        newApi.getFusion(DataMgr.getInstance().getUser() != null ? DataMgr.getInstance().getUser().getUdid() : "",
                VideoApplication.getInstance().getVerName(), "1",
                DataMgr.getInstance().getUser() != null ? DataMgr.getInstance().getUser().getSysInfo() : "",
                VideoApplication.getInstance().getPackageName())
                .compose(RxSchedulers.io_main())
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(activity)))
                .subscribe(new ApiObserver<FusionBean>() {

                    @Override
                    public void onSuccess(FusionBean demo, String msg) {
                        callBack.onSuccess(demo, msg);
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg) {
                        callBack.onFail(errorCode, errorMsg);
                    }
                });
    }

    /**
     * 记录切换APP
     *
     * @param activity
     * @param event
     * @param callBack
     */
    public static void stateChange(LifecycleOwner activity, int event, final HttpCallBack<List<String>> callBack) {
        Map<String, Object> map = new HashMap<>();
        map.put("event", event);
        newApi.stateChange(DataMgr.getInstance().getUser() != null ? DataMgr.getInstance().getUser().getUdid() : "",
                VideoApplication.getInstance().getVerName(), "1",
                DataMgr.getInstance().getUser() != null ? DataMgr.getInstance().getUser().getSysInfo() : "",
                VideoApplication.getInstance().getPackageName(), DataMgr.getInstance().getUser().getToken(), event)
                .compose(RxSchedulers.io_main())
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(activity)))
                .subscribe(new ApiObserver<List<String>>() {

                    @Override
                    public void onSuccess(List<String> demo, String msg) {
                        callBack.onSuccess(demo, msg);
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg) {
                        callBack.onFail(errorCode, errorMsg);
                    }
                });
    }
}
