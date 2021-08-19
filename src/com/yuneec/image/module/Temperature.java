package com.yuneec.image.module;

public class Temperature {

    public enum TYPE {
        BOX,
        CURVE,
        NONE
    }

    public TYPE type;

    public void setType(TYPE type) {
        this.type = type;
    }

}
