package com.unity3d.player.model;

public class ConfigBean {

    private String video_show_number;
    private String put_download_url;
    private String put_download_notice;
    private String video_share;
    private String watermark;
    private String token;
    private String udid = "";
    private String appVersion;
    private String deviceType;
    private String sysInfo = "";
    private String video_view_domian;
    private String AppPackageVer;
    private String AppPackageUrl;
    //1.强制更新   0.非强制
    private String AppPackageIsMust;

    public String getVideo_show_number() {
        return video_show_number;
    }

    public void setVideo_show_number(String video_show_number) {
        this.video_show_number = video_show_number;
    }

    public String getPut_download_url() {
        return put_download_url;
    }

    public void setPut_download_url(String put_download_url) {
        this.put_download_url = put_download_url;
    }

    public String getPut_download_notice() {
        return put_download_notice;
    }

    public void setPut_download_notice(String put_download_notice) {
        this.put_download_notice = put_download_notice;
    }

    public String getVideo_share() {
        return video_share;
    }

    public void setVideo_share(String video_share) {
        this.video_share = video_share;
    }

    public String getWatermark() {
        return watermark;
    }

    public void setWatermark(String watermark) {
        this.watermark = watermark;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getSysInfo() {
        return sysInfo;
    }

    public void setSysInfo(String sysInfo) {
        this.sysInfo = sysInfo;
    }

    public String getVideo_view_domian() {
        return video_view_domian;
    }

    public void setVideo_view_domian(String video_view_domian) {
        this.video_view_domian = video_view_domian;
    }

    public String getAppPackageVer() {
        return AppPackageVer;
    }

    public void setAppPackageVer(String appPackageVer) {
        AppPackageVer = appPackageVer;
    }

    public String getAppPackageUrl() {
        return AppPackageUrl;
    }

    public void setAppPackageUrl(String appPackageUrl) {
        AppPackageUrl = appPackageUrl;
    }

    public String getAppPackageIsMust() {
        return AppPackageIsMust;
    }

    public void setAppPackageIsMust(String appPackageIsMust) {
        AppPackageIsMust = appPackageIsMust;
    }
}
