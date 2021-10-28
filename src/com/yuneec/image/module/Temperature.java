package com.yuneec.image.module;

public class Temperature {

    public enum TYPE {
        POINT,
        BOX,
        CURVE,
        LINE,
        NONE
    }

    public TYPE type;

    public void setType(TYPE type) {
        this.type = type;
    }

}
