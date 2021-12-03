package com.yuneec.image.demo;

import com.yuneec.image.module.RightImageInfo;
import com.yuneec.image.utils.ByteUtils;
import com.yuneec.image.utils.ImageUtil;
import com.yuneec.image.utils.YLog;

import javax.swing.filechooser.FileSystemView;
import java.io.*;

public class JavaTest {

    public static void main(String[] args) {
//        test1();
//        test2();

        gmap();

    }

    private static void test2() {

        FileSystemView fsv = FileSystemView.getFileSystemView();
        System.out.println("桌面路径："+fsv.getHomeDirectory());
        System.out.println("默认路径：" + fsv.getDefaultDirectory());

    }

    private static void test1() {
        int xy = 0x9651;
//        YLog.I("JavaTest--->" + xy + " ---> " + TemperatureAlgorithm.getInstance().getTemperatureTest(xy));
        YLog.I("" + 3/2);

        String fileName = "F:\\intellijSpace\\YuneecImage\\src\\image\\wendu_06.txt";
        try {
            byte[] bytes = ImageUtil.readJpgToByte(fileName);
//            YLog.I(ByteUtils.byteArrayToHexString(bytes, 0,100));
            int len = bytes.length;
            for (int i=0;i<len/2;i++){
                byte[] spotTempByte = new byte[4];
                spotTempByte[3] = bytes[i*2];
                spotTempByte[2] = bytes[i*2 + 1];
//                YLog.I(ByteUtils.byteArrayToHexString(spotTempByte, 0,8));
                double temp = ByteUtils.byte2Int(spotTempByte);
                System.out.print("" + temp + " ; ");
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void gmap() {
        String lonlat = "120.935157,31.187180";  //31.187180,120.935157
        String bingMapUrl = "https://dev.virtualearth.net/REST/v1/Imagery/Map/Road/31.187180,120.935157/12?mapSize=500,500&pp=31.187180,120.935157;66&mapLayer=Basemap,Buildings&key=AgkhWkLY5kDXRCQOBduHxzCOoPlLC0GsRdHdeug8VzqZ3cwe_PSoPcY70sCnWabc";
        String url = "https://maps.googleapis.com/maps/api/staticmap?center=Brooklyn+Bridge,New+York,NY&zoom=13&size=600x300&maptype=roadmap" +
                "&markers=color:blue%7Clabel:S%7C40.702147,-74.015794&markers=color:green%7Clabel:G%7C40.711614,-74.012318" +
                "&markers=color:red%7Clabel:C%7C40.718217,-73.998284" +
                "&key=AIzaSyAfqQT3drrj2zId_Cbt1Q1mSFMBxGuwRKU";
        String amapUrl = "https://restapi.amap.com/v3/staticmap?location=120.935157,31.187180&zoom=12&size=640*512&markers=mid,,A:120.935157,31.187180&key=b58d00f5158d46e04f68b2fe471d1db5";
        HttpUtils.I().download(amapUrl, new HttpUtils.DownloadCallBack() {
            @Override
            public void success() {
                System.out.println("**** 下载完成：" + HttpUtils.tempMapImagePath);
            }
        });
    }

}
