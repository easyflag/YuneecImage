package com.yuneec.image.guide;

import com.yuneec.image.Global;
import com.yuneec.image.dll.YuneecGuide;
import com.yuneec.image.utils.ByteUtils;
import com.yuneec.image.utils.ParseTemperatureBytes;

import static java.lang.Math.abs;
import static java.lang.Math.pow;

public class GuideTemperatureAlgorithm {

    private static GuideTemperatureAlgorithm instance;
    public static GuideTemperatureAlgorithm getInstance() {
        if (instance == null) {
            instance = new GuideTemperatureAlgorithm();
        }
        return instance;
    }

    public float getTemperature(int x, int y) {
        int pGray = getTemperaByteForXY(x,y);
//        float pointTemperature = guide_measure_convertgray2temper(pGray,1);

        //change to call guide dll
        float pointTemperature = YuneecGuide.I().guideGrayTemper((short) pGray, GuiDeUtil.getInstance().pParamLine, 1,
                pParamExt.emiss, pParamExt.relHum, pParamExt.distance, pParamExt.reflectedTemper, pParamExt.atmosphericTemper, pParamExt.modifyK, pParamExt.modifyB);

//        YLog.I(" --E20T-- > pointTemperature :" + pointTemperature);
//        YLog.I(" -----------------------------------------------------------------------");
        return pointTemperature;
    }


    private int getTemperaByteForXY(int x, int y) {
        int index = getTemperaByteIndex(x,y);
        if (index >= ParseTemperatureBytes.getInstance().TemperatureBytes.length){
            index = ParseTemperatureBytes.getInstance().TemperatureBytes.length-2;
        }
        byte[] spotTempByte = new byte[4];
        spotTempByte[0] = ParseTemperatureBytes.getInstance().TemperatureBytes[index];
        spotTempByte[1] = ParseTemperatureBytes.getInstance().TemperatureBytes[index + 1];
        int pixel_value = ByteUtils.getInt(spotTempByte);
        return pixel_value;
    }

    public static boolean SupportScale = false;
    private int getTemperaByteIndex(int x, int y){
//        if (SupportScale){
//            if (ParseTemperatureBytes.getInstance().TemperatureBytes.length == TemperatureLen){
//                return getTemperaByteIndexSupportScaleForXY(x,y);
//            }else {
//                return getTemperaByteIndexSupportScaleForXY2(x,y);
//            }
//        }else {
//            return getTemperaByteIndexForXY(x,y);
//        }

        return getTemperaByteIndexSupportScaleForXY2(x,y);
    }

    private int getTemperaByteIndexSupportScaleForXY(int x, int y) {
        int index = 0;
        int lineTemperaBytes = 0;
        lineTemperaBytes = tifWidth * 2;
        double yScale = Global.currentOpenImageHeight / tifHeight;
        double xScale = Global.currentOpenImageWidth / (tifWidth * 2);
        int s_y = (int) (y / yScale);
        if (y <= 1) {
            index = (int) (x / xScale);
        } else if (x == 0) {
            index = ((s_y * lineTemperaBytes));
        } else {
            index = (int) ((s_y * lineTemperaBytes) + (x/xScale));
        }
        if (index % 2 != 0) {
            index++;
        }
        if (index > TemperatureLen){
            index = TemperatureLen-1;
        }
        return index;
    }

    private int getTemperaByteIndexSupportScaleForXY2(int x, int y) {
        int index = 0;
        int lineTemperaBytes = 0;
        tifWidth = 640;
        tifHeight = 512;
        lineTemperaBytes = tifWidth * 2;
        double yScale = Global.currentOpenImageHeight / tifHeight;
        double xScale = Global.currentOpenImageWidth / tifWidth;

        int s_y = (int) (y / yScale);
        int s_x = (int) (x / xScale);
        if(y==0){
            index = s_x * 2;
        }else if(x==0){
            index = s_y * lineTemperaBytes;
        }else {
            index = ((s_y * lineTemperaBytes) + s_x * 2);
        }

/*
//        if (Global.dzoom != 1.0){
        int topLeftX = ZoomManager.zoomRect[0];
        int topLeftY = ZoomManager.zoomRect[1];
        int width = ZoomManager.zoomRect[2];
        double zoom = Global.currentOpenImageWidth / width;
//            index = (int) ((topLeftY + y/Global.dzoom) * lineTemperaBytes + (topLeftX + x/Global.dzoom)*2);
        index = (int) ((topLeftY + y/zoom) * lineTemperaBytes + (topLeftX + x/zoom)*2);
        YLog.I(" zoomRect : " + ZoomManager.zoomRect[0] + "," + ZoomManager.zoomRect[1] +","+ ZoomManager.zoomRect[2] + "," + ZoomManager.zoomRect[3]
                + " ,zoom:" +zoom + " ,index:" + index);
//        }
*/
        return index;
    }

    int tifWidth = 320;
    int tifHeight = 256;
    static int TemperatureLen = 320*256*2;
    private int getTemperaByteIndexForXY(int x, int y) {
        int index = 0;
        int lineTemperaBytes = 0;

        if (ParseTemperatureBytes.getInstance().TemperatureBytes.length == TemperatureLen){
            tifWidth = 320;
            tifHeight = 256;
            lineTemperaBytes = tifWidth * 2;
            int ratio = (int) (Global.currentOpenImageWidth / tifWidth);
            if (y <= 1) {
                index = x;
            } else if (x == 0) {
                index = (y / ratio * lineTemperaBytes);
            } else {
                index = (((y / ratio) * lineTemperaBytes) + x);
            }
            if (index % 2 != 0) {
                index++;
            }
        }else {
            tifWidth = 640;
            tifHeight = 512;
            lineTemperaBytes = tifWidth * 2;
            if(y==0){
                index = x * 2;
            }else if(x==0){
                index = y*lineTemperaBytes;
            }else {
                index = ((y * lineTemperaBytes) + x*2);
            }
//            YLog.I(" --E20T-- >  index: " + index);
        }
        return index;
    }

    /*
    ....................................................................................................................
    *  Translate code from GuiDe C to Java
    * */


    public static class pParamExt{
        public static int emiss;
        public static int relHum;
        public static int distance;
        public static short reflectedTemper;
        public static short atmosphericTemper;
        public static int modifyK;
        public static short modifyB;
    }

    int COEF = 8192;
    int CURVE_LENGTH = 16384;

    float guide_measure_convertgray2temper(int pGray, int len) {
        float pTemper = 0;
        byte[] pParamLine = GuiDeUtil.getInstance().pParamLine;
        guide_measure_internal_param_t paramInt;
        if ((pParamLine[0] & 0xff)==0xAA && ( pParamLine[1] & 0xff) ==0x55) {
//            memcpy( & paramInt, (pParamLine + 4), sizeof(guide_measure_internal_param_t));
            paramInt = GuiDeUtil.getInstance().paramInt;
        } else {
            return -1;
        }

        int emiss = 98;
        if (pParamExt.emiss < 1 || pParamExt.emiss > 100) {
            return -2;
        } else {
            emiss = pParamExt.emiss;
        }

        int relHum = 60;
        if (pParamExt.relHum > 100) {
            return -3;
        } else {
            relHum = pParamExt.relHum;
        }

        int distance = 50;
        if (pParamExt.distance < 5 || pParamExt.distance > 5000) {
            return -4;
        } else {
            distance = pParamExt.distance / 10;
        }

        short reflectedTemper = 230;
        if (pParamExt.reflectedTemper < -400 || pParamExt.reflectedTemper > 5500) {
            return -5;
        } else {
            reflectedTemper = pParamExt.reflectedTemper;
        }

        short atmosphericTemper = 230;
        if (pParamExt.atmosphericTemper < -400 || pParamExt.atmosphericTemper > 1000) {
            return -6;
        } else {
            atmosphericTemper = pParamExt.atmosphericTemper;
        }

        int modifyK = 100;
        if (pParamExt.modifyK < 1 || pParamExt.modifyK > 200) {
            return -7;
        } else {
            modifyK = pParamExt.modifyK;
        }

        short modifyB = 0;
        if (pParamExt.modifyB < -100 || pParamExt.modifyB > 100) {
            return -8;
        } else {
            modifyB = pParamExt.modifyB;
        }

        if ((paramInt.shutter_flag & 0x0001) == 0x0001) {
            return -9;
        }

        for (int i = 0; i < len; i++) {
            //y16修正
            int y16_correct = pGray * paramInt.u16modify_k / COEF + paramInt.s16modify_b;

            //湿度修正
            double coEfficient_relhum = 1 - (relHum - 60) / 5000.0;
            y16_correct *= coEfficient_relhum;

            //距离修正
            double coEfficient_distance = (10000 + paramInt.A0 * (distance - 5) * (distance - 5) - paramInt.A1 * (distance - 5)) / 10000.0;
            y16_correct /= coEfficient_distance;

            //温度曲线反查温度
            if (y16_correct < 0) {
                y16_correct = 0;
            } else if (y16_correct >= CURVE_LENGTH) {
                y16_correct = CURVE_LENGTH - 1;
            }
            short tmp = 0;
            if (paramInt.gear == 0) {
                tmp = GuiDe.lowTempCurve[y16_correct];
            } else {
                tmp = GuiDe.highTempCurve[y16_correct];
            }

            //温度修正
            tmp += paramInt.B1;

            //发射率修正
            double tmp1 = 0;
            if (emiss < 100) {
                double dTc = (double) ((tmp + 2735) / 10.0);
                double dTu;

                if (abs(reflectedTemper - atmosphericTemper) > 20) {
                    dTu = (double) ((reflectedTemper + 2735) / 10.0);
                } else {
                    dTu = (double) ((atmosphericTemper + 2735) / 10.0);
                }

                double dTc_square = pow((double) dTc, (double) 4.0);
                double dTu_square = pow((double) dTu, (double) 4.0);

                double dta = dTu_square + (dTc_square - dTu_square) * 100 / (emiss + 1);

                tmp1 = 10.0 * pow((double) dta, (double) 0.25);
            } else {
                tmp1 = tmp / 1.0 + 2735;
            }

            short tmp2 = (short) (tmp1 - 2735);

            int tmp3 = (tmp2 * modifyK) / 100 + modifyB;

            pTemper = (float) (tmp3 / 10.0);
        }
        return pTemper;
    }


}
