package com.yuneec.image.guide;

import com.yuneec.image.utils.ByteUtils;
import com.yuneec.image.utils.YLog;

public class guide_measure_internal_param_t {

    int relHum;
    int distance;
    int emiss;
    short reflectedTemper;
    short KF;
    short K1;
    short K2;
    short K3;
    short K4;
    short B1;
    int A0;
    int A1;
    short KJ;
    short KB;
    short gear;
    short sdeltaY16;
    short centerY16;
    short maxY16;
    int A2;
    short shutter_realtimeTemp;
    short lens_lastCalibrateTemp;
    short shutter_lastCalibrateTemp;
    short shutter_startTemp;
    short shutter_flag;
    short y16_average;
    short K5;
    short K6;
    int K7;
    short shutter_coldStartTemp;
    short lens_tempRaiseValue;
    short showMode;
    short lens_startTemp;
    short shutter_flashColdStartTemp;
    short K8;
    short deltaY16z;
    short shutterDrift_coldStartTemp;
    short empty1;
    short empty2;
    short hot_xPosition;
    short hot_yPosition;
    short hot_temp;
    short cold_xPosition;
    short cold_yPosition;
    short cold_temp;
    short cursor_xPosition;
    short cursor_yPosition;
    short cursor_temp;
    short region_avgTemp;
    short u16modify_k;
    short s16modify_b;
    short kbr_x100;

    public String toString(){
        return "guide_measure_internal_param_t ---> "
                + "hot_xPosition:" + hot_xPosition + " ,hot_yPosition:" + hot_yPosition + " ,hot_temp:" + hot_temp
                + " ,cold_xPosition:" + cold_xPosition + " ,cold_yPosition:" + cold_yPosition + " ,cold_temp:" + cold_temp
                + " ,region_avgTemp:" + region_avgTemp;
    }

    //memcpy(&paramInt, (pParamLine+4), sizeof(guide_measure_internal_param_t));
    public static guide_measure_internal_param_t getParamInt(byte[] pParamLine) {
        guide_measure_internal_param_t paramInt = new guide_measure_internal_param_t();
        int offset = 4;

        byte[] relHumBytes = new byte[4];
        System.arraycopy(pParamLine,offset,relHumBytes,0,2);
        paramInt.relHum = ByteUtils.getInt(relHumBytes);

        offset = offset + 2;
        byte[] distanceBytes = new byte[4];
        System.arraycopy(pParamLine,offset,distanceBytes,0,2);
        paramInt.distance = ByteUtils.getInt(distanceBytes);

        offset = offset + 2;
        byte[] emissBytes = new byte[4];
        System.arraycopy(pParamLine,offset,emissBytes,0,2);
        paramInt.emiss = ByteUtils.getInt(emissBytes);

        offset = offset + 2;
        byte[] reflectedTemperBytes = new byte[2];
        System.arraycopy(pParamLine,offset,reflectedTemperBytes,0,2);
        paramInt.reflectedTemper = ByteUtils.getShort(reflectedTemperBytes);

        offset = offset + 2;
        byte[] KFBytes = new byte[2];
        System.arraycopy(pParamLine,offset,KFBytes,0,2);
        paramInt.KF = ByteUtils.getShort(KFBytes);

        offset = offset + 2;
        byte[] K1Bytes = new byte[2];
        System.arraycopy(pParamLine,offset,K1Bytes,0,2);
        paramInt.K1 = ByteUtils.getShort(K1Bytes);

        offset = offset + 2;
        byte[] K2Bytes = new byte[2];
        System.arraycopy(pParamLine,offset,K2Bytes,0,2);
        paramInt.K2 = ByteUtils.getShort(K2Bytes);

        offset = offset + 2;
        byte[] K3Bytes = new byte[2];
        System.arraycopy(pParamLine,offset,K3Bytes,0,2);
        paramInt.K3 = ByteUtils.getShort(K3Bytes);

        offset = offset + 2;
        byte[] K4Bytes = new byte[2];
        System.arraycopy(pParamLine,offset,K4Bytes,0,2);
        paramInt.K4 = ByteUtils.getShort(K4Bytes);

        offset = offset + 2;
        byte[] B1Bytes = new byte[2];
        System.arraycopy(pParamLine,offset,B1Bytes,0,2);
        paramInt.B1 = ByteUtils.getShort(B1Bytes);

        offset = offset + 2;
        byte[] A0Bytes = new byte[4];
        System.arraycopy(pParamLine,offset,A0Bytes,0,4);
        paramInt.A0 = ByteUtils.getInt(A0Bytes);

        offset = offset + 4;
        byte[] A1Bytes = new byte[4];
        System.arraycopy(pParamLine,offset,A1Bytes,0,4);
        paramInt.A1 = ByteUtils.getInt(A1Bytes);

        offset = offset + 4;
        byte[] KJBytes = new byte[2];
        System.arraycopy(pParamLine,offset,KJBytes,0,2);
        paramInt.KJ = ByteUtils.getShort(KJBytes);

        offset = offset + 2;
        byte[] KBBytes = new byte[2];
        System.arraycopy(pParamLine,offset,KBBytes,0,2);
        paramInt.KB = ByteUtils.getShort(KBBytes);

        offset = offset + 2;
        byte[] gearBytes = new byte[2];
        System.arraycopy(pParamLine,offset,gearBytes,0,2);
        paramInt.gear = ByteUtils.getShort(gearBytes);

        offset = offset + 2;
        byte[] sdeltaY16Bytes = new byte[2];
        System.arraycopy(pParamLine,offset,sdeltaY16Bytes,0,2);
        paramInt.sdeltaY16 = ByteUtils.getShort(sdeltaY16Bytes);

        offset = offset + 2;
        byte[] centerY16Bytes = new byte[2];
        System.arraycopy(pParamLine,offset,centerY16Bytes,0,2);
        paramInt.centerY16 = ByteUtils.getShort(centerY16Bytes);

        offset = offset + 2;
        byte[] maxY16Bytes = new byte[2];
        System.arraycopy(pParamLine,offset,maxY16Bytes,0,2);
        paramInt.maxY16 = ByteUtils.getShort(maxY16Bytes);

        offset = offset + 2;
        byte[] A2Bytes = new byte[4];
        System.arraycopy(pParamLine,offset,A2Bytes,0,4);
        paramInt.A2 = ByteUtils.getInt(A2Bytes);

        offset = offset + 4;
        byte[] shutter_realtimeTempBytes = new byte[2];
        System.arraycopy(pParamLine,offset,shutter_realtimeTempBytes,0,2);
        paramInt.shutter_realtimeTemp = ByteUtils.getShort(shutter_realtimeTempBytes);

        offset = offset + 2;
        byte[] lens_lastCalibrateTempBytes = new byte[2];
        System.arraycopy(pParamLine,offset,lens_lastCalibrateTempBytes,0,2);
        paramInt.lens_lastCalibrateTemp = ByteUtils.getShort(lens_lastCalibrateTempBytes);

        offset = offset + 2;
        byte[] shutter_lastCalibrateTempBytes = new byte[2];
        System.arraycopy(pParamLine,offset,shutter_lastCalibrateTempBytes,0,2);
        paramInt.shutter_lastCalibrateTemp = ByteUtils.getShort(shutter_lastCalibrateTempBytes);

        offset = offset + 2;
        byte[] shutter_startTempBytes = new byte[2];
        System.arraycopy(pParamLine,offset,shutter_startTempBytes,0,2);
        paramInt.shutter_startTemp = ByteUtils.getShort(shutter_startTempBytes);

        offset = offset + 2;
        byte[] shutter_flagBytes = new byte[2];
        System.arraycopy(pParamLine,offset,shutter_flagBytes,0,2);
        paramInt.shutter_flag = ByteUtils.getShort(shutter_flagBytes);

        offset = offset + 2;
        byte[] y16_averageBytes = new byte[2];
        System.arraycopy(pParamLine,offset,y16_averageBytes,0,2);
        paramInt.y16_average = ByteUtils.getShort(y16_averageBytes);

        short K5;
        offset = offset + 2;
        byte[] K5Bytes = new byte[2];
        System.arraycopy(pParamLine,offset,K5Bytes,0,2);
        paramInt.K5 = ByteUtils.getShort(K5Bytes);

        short K6;
        offset = offset + 2;
        byte[] K6Bytes = new byte[2];
        System.arraycopy(pParamLine,offset,K6Bytes,0,2);
        paramInt.K6 = ByteUtils.getShort(K6Bytes);

        int K7;
        offset = offset + 2;
        byte[] K7Bytes = new byte[4];
        System.arraycopy(pParamLine,offset,K7Bytes,0,4);
        paramInt.K7 = ByteUtils.getInt(K7Bytes);

        short shutter_coldStartTemp;
        offset = offset + 4;
        byte[] shutter_coldStartTempBytes = new byte[2];
        System.arraycopy(pParamLine,offset,shutter_coldStartTempBytes,0,2);
        paramInt.shutter_coldStartTemp = ByteUtils.getShort(shutter_coldStartTempBytes);

        short lens_tempRaiseValue;
        offset = offset + 2;
        byte[] lens_tempRaiseValueBytes = new byte[2];
        System.arraycopy(pParamLine,offset,lens_tempRaiseValueBytes,0,2);
        paramInt.lens_tempRaiseValue = ByteUtils.getShort(lens_tempRaiseValueBytes);

        short showMode;
        offset = offset + 2;
        byte[] showModeBytes = new byte[2];
        System.arraycopy(pParamLine,offset,showModeBytes,0,2);
        paramInt.showMode = ByteUtils.getShort(showModeBytes);

        short lens_startTemp;
        offset = offset + 2;
        byte[] lens_startTempBytes = new byte[2];
        System.arraycopy(pParamLine,offset,lens_startTempBytes,0,2);
        paramInt.lens_startTemp = ByteUtils.getShort(lens_startTempBytes);

        short shutter_flashColdStartTemp;
        offset = offset + 2;
        byte[] shutter_flashColdStartTempBytes = new byte[2];
        System.arraycopy(pParamLine,offset,shutter_flashColdStartTempBytes,0,2);
        paramInt.shutter_flashColdStartTemp = ByteUtils.getShort(shutter_flashColdStartTempBytes);

        short K8;
        offset = offset + 2;
        byte[] K8Bytes = new byte[2];
        System.arraycopy(pParamLine,offset,K8Bytes,0,2);
        paramInt.K8 = ByteUtils.getShort(K8Bytes);

        short deltaY16z;
        offset = offset + 2;
        byte[] deltaY16zBytes = new byte[2];
        System.arraycopy(pParamLine,offset,deltaY16zBytes,0,2);
        paramInt.deltaY16z = ByteUtils.getShort(deltaY16zBytes);

        short shutterDrift_coldStartTemp;
        offset = offset + 2;
        byte[] shutterDrift_coldStartTempBytes = new byte[2];
        System.arraycopy(pParamLine,offset,shutterDrift_coldStartTempBytes,0,2);
        paramInt.shutterDrift_coldStartTemp = ByteUtils.getShort(shutterDrift_coldStartTempBytes);

        short empty1;
        offset = offset + 2;
        byte[] empty1Bytes = new byte[2];
        System.arraycopy(pParamLine,offset,empty1Bytes,0,2);
        paramInt.empty1 = ByteUtils.getShort(empty1Bytes);

        short empty2;
        offset = offset + 2;
        byte[] empty2Bytes = new byte[2];
        System.arraycopy(pParamLine,offset,empty2Bytes,0,2);
        paramInt.empty2 = ByteUtils.getShort(empty2Bytes);

        short hot_xPosition;
        offset = offset + 2;
        byte[] hot_xPositionBytes = new byte[2];
        System.arraycopy(pParamLine,offset,hot_xPositionBytes,0,2);
        paramInt.hot_xPosition = ByteUtils.getShort(hot_xPositionBytes);

        short hot_yPosition;
        offset = offset + 2;
        byte[] hot_yPositionBytes = new byte[2];
        System.arraycopy(pParamLine,offset,hot_yPositionBytes,0,2);
        paramInt.hot_yPosition = ByteUtils.getShort(hot_yPositionBytes);

        short hot_temp;
        offset = offset + 2;
        byte[] hot_tempBytes = new byte[2];
        System.arraycopy(pParamLine,offset,hot_tempBytes,0,2);
        paramInt.hot_temp = ByteUtils.getShort(hot_tempBytes);

        short cold_xPosition;
        offset = offset + 2;
        byte[] cold_xPositionBytes = new byte[2];
        System.arraycopy(pParamLine,offset,cold_xPositionBytes,0,2);
        paramInt.cold_xPosition = ByteUtils.getShort(cold_xPositionBytes);

        short cold_yPosition;
        offset = offset + 2;
        byte[] cold_yPositionBytes = new byte[2];
        System.arraycopy(pParamLine,offset,cold_yPositionBytes,0,2);
        paramInt.cold_yPosition = ByteUtils.getShort(cold_yPositionBytes);

        short cold_temp;
        offset = offset + 2;
        byte[] cold_tempBytes = new byte[2];
        System.arraycopy(pParamLine,offset,cold_tempBytes,0,2);
        paramInt.cold_temp = ByteUtils.getShort(cold_tempBytes);

        short cursor_xPosition;
        offset = offset + 2;
        byte[] cursor_xPositionBytes = new byte[2];
        System.arraycopy(pParamLine,offset,cursor_xPositionBytes,0,2);
        paramInt.cursor_xPosition = ByteUtils.getShort(cursor_xPositionBytes);

        short cursor_yPosition;
        offset = offset + 2;
        byte[] cursor_yPositionBytes = new byte[2];
        System.arraycopy(pParamLine,offset,cursor_yPositionBytes,0,2);
        paramInt.cursor_yPosition = ByteUtils.getShort(cursor_yPositionBytes);

        short cursor_temp;
        offset = offset + 2;
        byte[] cursor_tempBytes = new byte[2];
        System.arraycopy(pParamLine,offset,cursor_tempBytes,0,2);
        paramInt.cursor_temp = ByteUtils.getShort(cursor_tempBytes);

        short region_avgTemp;
        offset = offset + 2;
        byte[] region_avgTempBytes = new byte[2];
        System.arraycopy(pParamLine,offset,region_avgTempBytes,0,2);
        paramInt.region_avgTemp = ByteUtils.getShort(region_avgTempBytes);

        short u16modify_k;
        offset = offset + 2;
        byte[] u16modify_kBytes = new byte[2];
        System.arraycopy(pParamLine,offset,u16modify_kBytes,0,2);
        paramInt.u16modify_k = ByteUtils.getShort(u16modify_kBytes);

        short s16modify_b;
        offset = offset + 2;
        byte[] s16modify_bBytes = new byte[2];
        System.arraycopy(pParamLine,offset,s16modify_bBytes,0,2);
        paramInt.s16modify_b = ByteUtils.getShort(s16modify_bBytes);

        short kbr_x100;
        offset = offset + 2;
        byte[] kbr_x100Bytes = new byte[2];
        System.arraycopy(pParamLine,offset,kbr_x100Bytes,0,2);
        paramInt.kbr_x100 = ByteUtils.getShort(kbr_x100Bytes);

        YLog.I(paramInt.toString());

        return paramInt;
    }

}
