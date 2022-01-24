package com.unity3d.player.model;

public class DataMgr {

    private static class SingletonClassInstance {
        private static final DataMgr instance = new DataMgr();
    }

    private DataMgr() {
        this.initData();
    }

    public static DataMgr getInstance() {
        return SingletonClassInstance.instance;
    }

    public ConfigBean user;

    public void setUser(ConfigBean user) {
        this.user = user;
    }

    // 用户数据
    public ConfigBean getUser() {
        return user;
    }

    private void initData() {
        this.setUser(new ConfigBean());
    }
}
