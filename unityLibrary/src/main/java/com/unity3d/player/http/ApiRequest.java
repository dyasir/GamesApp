package com.unity3d.player.http;

import com.unity3d.player.model.ConfigBean;
import com.unity3d.player.model.FusionBean;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiRequest {

    //获取配置
    String CONFIG_URL = "api/get_config";
    //获取融合APP配置
    String FUSION_URL = "api/change_config";
    //记录切换APP
    String STATE_CHANGE_URL = "api/stat_change";

    /**
     * 获取配置
     *
     * @param udid
     * @param app_version
     * @param device_type
     * @param sys_info
     * @param package_id
     * @return
     */
    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded")
    @POST(CONFIG_URL)
    Observable<ApiResponse<ConfigBean>> getConfigs(@Header("udid") String udid, @Header("app-version") String app_version,
                                                   @Header("device-type") String device_type, @Header("sys-info") String sys_info,
                                                   @Header("package-id") String package_id, @Field("utm_source") String utm_source,
                                                   @Field("utm_medium") String utm_medium, @Field("install_time") String install_time,
                                                   @Field("version") String version);

    /**
     * 获取融合APP配置
     *
     * @param udid
     * @param app_version
     * @param device_type
     * @param sys_info
     * @param package_id
     * @return
     */
    @Headers("Content-Type:application/x-www-form-urlencoded")
    @POST(FUSION_URL)
    Observable<ApiResponse<FusionBean>> getFusion(@Header("udid") String udid, @Header("app-version") String app_version,
                                                  @Header("device-type") String device_type, @Header("sys-info") String sys_info,
                                                  @Header("package-id") String package_id);

    /**
     * 记录切换APP
     *
     * @param udid
     * @param app_version
     * @param device_type
     * @param sys_info
     * @param package_id
     * @param event       1：视频   2：广告   3: 时长   4：启动   5：立即
     * @return
     */
    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded")
    @POST(STATE_CHANGE_URL)
    Observable<ApiResponse<List<String>>> stateChange(@Header("udid") String udid, @Header("app-version") String app_version,
                                                      @Header("device-type") String device_type, @Header("sys-info") String sys_info,
                                                      @Header("package-id") String package_id, @Header("Authorization") String Authorization,
                                                      @Field("event") int event);
}
