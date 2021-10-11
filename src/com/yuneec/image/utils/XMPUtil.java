package com.yuneec.image.utils;


import com.adobe.internal.xmp.XMPMeta;
import com.adobe.internal.xmp.XMPMetaFactory;
import com.adobe.internal.xmp.impl.XMPIteratorImpl;
import com.adobe.internal.xmp.options.PropertyOptions;
import com.adobe.internal.xmp.properties.XMPPropertyInfo;
import com.yuneec.image.Global;
import com.yuneec.image.guide.GuiDeUtil;
import com.yuneec.image.guide.GuideTemperatureAlgorithm;
import com.yuneec.image.module.colorpalette.PaletteParam;

import java.io.*;
import java.util.Arrays;
import java.util.Map;

public class XMPUtil {

//    String imagePath = "F:\\intellijSpace\\YuneecImage\\src\\image\\Yuneec01.jpg";

    private static XMPUtil instance;

    public static XMPUtil getInstance() {
        if (instance == null) {
            instance = new XMPUtil();
        }
        return instance;
    }

    private final byte[] OPEN_ARR = "<x:xmpmeta".getBytes();
    private final byte[] CLOSE_ARR = "</x:xmpmeta>".getBytes();
    private final String namespace = "http://www.xxxx.com.cn/xxxx/1.0/";
    private final String xmpTag = "xxxx:";
    private Map<String, String> xmpMap;

    public void getXmp() {
        try {
            File orgFile = new File(Global.currentLeftSelectImagePath);
            FileInputStream in = new FileInputStream(orgFile);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            copy(in, out);
            byte[] fileData = out.toByteArray();
            int openIdx = indexOf(fileData, OPEN_ARR, 0);
            if (openIdx > 0) {
                int closeIdx = indexOf(fileData, CLOSE_ARR, openIdx + 1) + CLOSE_ARR.length;
                byte[] xmpArr = Arrays.copyOfRange(fileData, openIdx, closeIdx);
                XMPMeta xmpMeta = XMPMetaFactory.parseFromBuffer(xmpArr);
                printXMPMeta(xmpMeta);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void copy(InputStream in, OutputStream out) throws Exception {
        int len = -1;
        byte[] buf = new byte[1024];
        while ((len = in.read(buf)) >= 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    private int indexOf(byte[] arr, byte[] sub, int start) {
        int subIdx = 0;
        for (int x = start; x < arr.length; x++) {
            if (arr[x] == sub[subIdx]) {
                if (subIdx == sub.length - 1) {
                    return x - subIdx;
                }
                subIdx++;
            } else {
                subIdx = 0;
            }
        }
        return -1;
    }


    private void printXMPMeta(XMPMeta xmpMeta) throws Exception {
        XMPIteratorImpl xmpIterator = (XMPIteratorImpl) xmpMeta.iterator();
        while (xmpIterator.hasNext()) {
            XMPPropertyInfo obj = (XMPPropertyInfo) xmpIterator.next();
            PropertyOptions propertyOptions = obj.getOptions();
//            YLog.I("XMP---> " + obj.getNamespace() + " , " + obj.getPath() + " , " + obj.getValue());
            if (obj.getNamespace().equals("http://pix4d.com/Camera/1.0/")){
                if (GuiDeUtil.getInstance().isE20T()){
                    setGuideXMPvalue(obj.getPath(),obj.getValue());
                }else {
                    setXMPvalue(obj.getPath(),obj.getValue());
                }
            }
        }
        if (GuiDeUtil.getInstance().isE20T()){
            YLog.I("XMP Guide -->" + "emiss:" + GuideTemperatureAlgorithm.pParamExt.emiss
                    + " ,relHum:" + GuideTemperatureAlgorithm.pParamExt.relHum
                    + " ,distance:" + GuideTemperatureAlgorithm.pParamExt.distance
                    + " ,reflectedTemper:" + GuideTemperatureAlgorithm.pParamExt.reflectedTemper
                    + " ,atmosphericTemper:" + GuideTemperatureAlgorithm.pParamExt.atmosphericTemper
                    + " ,modifyK:" + GuideTemperatureAlgorithm.pParamExt.modifyK
                    + " ,modifyB:" + GuideTemperatureAlgorithm.pParamExt.modifyB
            );
        }else {
            YLog.I("XMP-->" + "AtmosphereTemperature:" + TemperatureAlgorithm.AtmosphereTemperature
                    + " ,SceneEmissivity:" + TemperatureAlgorithm.SceneEmissivity
                    + " ,TransmissionCoefficient:" + TemperatureAlgorithm.TransmissionCoefficient
                    + " ,R1:" + TemperatureAlgorithm.R1
                    + " ,B1:" + TemperatureAlgorithm.B1
                    + " ,O1:" + TemperatureAlgorithm.O1
                    + " ,PixelToFpaTempB:" + TemperatureAlgorithm.PixelToFpaTempB
                    + " ,PixelToFpaTempM:" + TemperatureAlgorithm.PixelToFpaTempM
                    + " ,GainMode:" + TemperatureAlgorithm.GainMode
                    + " ,AmbientTemperature:" + TemperatureAlgorithm.AmbientTemperature
            );
        }
    }

    private void setXMPvalue(String path, String value) {
        if (path == null){
            return;
        }
        if(path.endsWith("AtmosphereTemperature")){
            TemperatureAlgorithm.AtmosphereTemperature = Float.parseFloat(value);
        }else if(path.endsWith("SceneEmissivity")){
            TemperatureAlgorithm.SceneEmissivity = Float.parseFloat(value);
        }else if(path.endsWith("TransmissionCoefficient")){
            TemperatureAlgorithm.TransmissionCoefficient = Float.parseFloat(value);
        }else if(path.endsWith("R1")){
            TemperatureAlgorithm.R1 = Integer.parseInt(value);
        }else if(path.endsWith("B1")){
            TemperatureAlgorithm.B1 = Integer.parseInt(value);
        }else if(path.endsWith("F1")){
            TemperatureAlgorithm.F1 = Integer.parseInt(value);
        }else if(path.endsWith("O1")){
            TemperatureAlgorithm.O1 = Integer.parseInt(value);
        }else if(path.endsWith("PixelToFpaTempB")){
            TemperatureAlgorithm.PixelToFpaTempB = Float.parseFloat(value);
        }else if(path.endsWith("PixelToFpaTempM")){
            TemperatureAlgorithm.PixelToFpaTempM = Float.parseFloat(value);
        }else if(path.endsWith("GainMode")){
            TemperatureAlgorithm.GainMode = Integer.parseInt(value);
        }else if(path.endsWith("AmbientTemperature")){
            TemperatureAlgorithm.AmbientTemperature = Float.parseFloat(value);
        }
    }

    private void setGuideXMPvalue(String path, String value) {
        if (path == null){
            return;
        }
        if(path.endsWith("emiss")){
            GuideTemperatureAlgorithm.pParamExt.emiss = Integer.parseInt(value);
        }else if(path.endsWith("relHum")){
            GuideTemperatureAlgorithm.pParamExt.relHum = Integer.parseInt(value);
        }else if(path.endsWith("distance")){
            GuideTemperatureAlgorithm.pParamExt.distance = Integer.parseInt(value);
        }else if(path.endsWith("reflectedTemper")){
            GuideTemperatureAlgorithm.pParamExt.reflectedTemper = Short.parseShort(value);
        }else if(path.endsWith("atmosphericTemper")){
            GuideTemperatureAlgorithm.pParamExt.atmosphericTemper = Short.parseShort(value);
        }else if(path.endsWith("modifyK")){
            GuideTemperatureAlgorithm.pParamExt.modifyK = Integer.parseInt(value);
        }else if(path.endsWith("modifyB")){
            GuideTemperatureAlgorithm.pParamExt.modifyB = Short.parseShort(value);
        }else if(path.endsWith("paletteType")){
            Global.imagePaletteType = Integer.parseInt(value);
            PaletteParam.currentPalette = Global.imagePaletteType;
//            YLog.I("PaletteParam.currentPalette ：" + PaletteParam.currentPalette);
        }else if(path.endsWith("dzoom")){
            Global.dzoom = Integer.parseInt(value) / 8f;
//            YLog.I("Global.dzoom ：" + Global.dzoom);
        }
    }

}
