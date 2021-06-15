package com.yuneec.image.utils;

import java.util.ArrayList;

public class ParseTemperatureBytes {

    private static ParseTemperatureBytes instance;
    public static ParseTemperatureBytes getInstance() {
        if (instance == null) {
            instance = new ParseTemperatureBytes();
        }
        return instance;
    }

    public byte[] TemperatureBytes;
    private byte[] HEADER = new byte[]{(byte) 0xFF, (byte) 0xE3};
    private int[] bufLens;

    public void init(byte[] bytes) {
            int length = bytes.length;
        YLog.I("image bytes length: " + length);
//			String s = ByteUtils.byteArrayToHexString(bytes);
//			YLog.I(s);
//			String s1 = ByteUtils.byteArrayToHexString(bytes, 0, 50);
//			YLog.I(s1);
//			String s2 = ByteUtils.byteArrayToHexString(bytes, length-50, length, length);
//			YLog.I(s2);

        ArrayList findIndexList = simpleFind(bytes, 0, length, HEADER); //{5911,71447,136983}
        bufLens = new int[findIndexList.size() + 1];
        bufLens[0] = 0;  // [0,65534,65534,32778]
        int TemperatureBytesLen = 0;  // 163846
        for (int i=0;i<findIndexList.size();i++){
//            YLog.I("findIndexList ---> " + findIndexList.get(i));
            int bufLen = getBufLen(bytes, (Integer) findIndexList.get(i));
            bufLens[i+1] = bufLen;
            TemperatureBytesLen += bufLen;
//            YLog.I("findIndexList  bufLen---> " + bufLen + ",TemperatureBytesLen:" + TemperatureBytesLen);
        }

        TemperatureBytes = new byte[TemperatureBytesLen];
        int desPos = 0;
        for (int i=0;i<findIndexList.size();i++){
            desPos += bufLens[i];
            System.arraycopy(bytes,(int)findIndexList.get(i)+2+2,TemperatureBytes,desPos,bufLens[i+1]-2);
        }
        YLog.I("TemperatureBytes---> " + TemperatureBytes.length +  " , " + TemperatureBytesLen);
//        YLog.I(ByteUtils.byteArrayToHexString(TemperatureBytes, 0,100));
//        YLog.I(ByteUtils.byteArrayToHexString(bytes, TemperatureBytesLen-100, TemperatureBytesLen, TemperatureBytesLen));
    }

    private int getBufLen(byte[] bytes, int offset) {
        byte[] buf = new byte[4];
        System.arraycopy(bytes,offset+2,buf,2,2);
//        System.out.print(ByteUtils.byteArrayToHexString(buf, 0,buf.length));
        int len = ByteUtils.byteArrayToInt(buf,true);
        return len;
    }

    public static ArrayList simpleFind(byte[] src, int srcOff, int srcLen, byte[] dst) {
        return simpleFind(src, srcOff, srcLen, dst, 0, dst.length);
    }

    public static ArrayList simpleFind(byte[] src, int srcOff, int srcLen, byte[] dst, int dstOff, int dstLen) {
        ArrayList findIndexList = new ArrayList();
        findIndexList.clear();
        for (int i = 0; i < srcLen; i++) {
            if ((srcLen - i) < dstLen) {
                return findIndexList;
            }
            int j = 0;
            for (; j < dstLen; j++) {
                if (src[srcOff + i + j] != dst[dstOff + j]) {
                    break;
                }
            }
            if (j == dstLen) {
                findIndexList.add(i);
            }
        }
        return findIndexList;
    }

}
