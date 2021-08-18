package com.yuneec.image.demo;

import com.yuneec.image.utils.ByteUtils;
import com.yuneec.image.utils.ImageUtil;
import com.yuneec.image.utils.YLog;

import java.io.*;

public class JavaTest {

    public static void main(String[] args) {
//        test1();
        test2();


    }

    private static void test2() {

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
}
