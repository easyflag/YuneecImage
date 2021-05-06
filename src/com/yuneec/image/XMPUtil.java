package com.yuneec.image;


import com.adobe.internal.xmp.XMPMeta;
import com.adobe.internal.xmp.XMPMetaFactory;
import com.adobe.internal.xmp.impl.XMPIteratorImpl;
import com.adobe.internal.xmp.options.PropertyOptions;
import com.adobe.internal.xmp.properties.XMPPropertyInfo;

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

    public void getXMP() throws Exception {
        File orgFile = new File(Global.currentOpenImagePath);
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
//            System.out.println("XMP---> " + obj.getNamespace() + "--" + obj.getPath() + "--" + obj.getValue());
            if (obj.getNamespace().equals("http://pix4d.com/Camera/1.0/")){
                setXMPvalue(obj.getPath(),obj.getValue());
            }
        }
        System.out.println("XMP-->" + "AtmosphereTemperature:" + TemperatureAlgorithm.AtmosphereTemperature
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

    public void getXmp() {
        try {
            getXMP();
        } catch (Exception e) {
            e.printStackTrace();
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


    public float getTempera(int x, int y) {
        return TemperatureAlgorithm.getInstance().getTemperature(x,y);
    }


}
