package com.yuneec.image.module.point;

public class PointManager {

    public static PointManager instance;

    public static PointManager getInstance() {
        if (instance == null) {
            instance = new PointManager();
        }
        return instance;
    }


}
