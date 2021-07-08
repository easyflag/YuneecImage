package com.yuneec.image.module.center;

public class CenterTopSettingPane {

    private static CenterTopSettingPane instance;
    public static CenterTopSettingPane getInstance() {
        if (instance == null) {
            instance = new CenterTopSettingPane();
        }
        return instance;
    }

    public void init() {
        initCenterTopSettingPane();
    }

    private void initCenterTopSettingPane() {

    }


}
