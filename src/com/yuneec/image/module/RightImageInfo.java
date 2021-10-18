package com.yuneec.image.module;

public class RightImageInfo {

    private int len = 9;
    public static String[] exifName = {"Make","Model","Image Description","Image Width","Image Height","Date/Time Original","GPS Longitude","GPS Latitude"};
    //{"制造","型号","文件名","宽度","高度","日期","经度","纬度"};
    public static String[] make = {"Make","Make","制造商",""};
    public static String[] model = {"Model","Model","型号",""};
    public static String[] imageDescription = {"Image Description","Image Name","文件名",""};
    public static String[] sharpness = {"Image Width","Image Height","Sharpness ","分辨率",""};
    public static String[] time = {"Date/Time Original","Time ","时间",""};
    public static String[] longitude = {"GPS Longitude","Longitude ","经度",""};
    public static String[] latitude = {"GPS Latitude","Latitude ","纬度",""};
    public static String[] fileSize = {"File Size","FileSize ","文件大小",""};
    public static String[] colorPalette = {"ColorPalette ","调色板",""};


}
