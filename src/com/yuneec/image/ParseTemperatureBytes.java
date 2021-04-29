package com.yuneec.image;

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
			System.out.println("image bytes length: " + length);
//			String s = ByteUtils.byteArrayToHexString(bytes);
//			System.out.println(s);
//			String s1 = ByteUtils.byteArrayToHexString(bytes, 0, 50);
//			System.out.println(s1);
//			String s2 = ByteUtils.byteArrayToHexString(bytes, length-50, length, length);
//			System.out.println(s2);

        ArrayList findIndexList = simpleFind(bytes, 0, length, HEADER);
        bufLens = new int[findIndexList.size() + 1];
        bufLens[0] = 0;  // [0,65534,65534,33000]
        int TemperatureBytesLen = 0;  // 164068
        for (int i=0;i<findIndexList.size();i++){
//            System.out.println("findIndexList ---> " + findIndexList.get(i));
            int bufLen = getBufLen(bytes, (Integer) findIndexList.get(i));
            bufLens[i+1] = bufLen;
            TemperatureBytesLen += bufLen;
            System.out.println("findIndexList  bufLen---> " + bufLen + ",TemperatureBytesLen:" + TemperatureBytesLen);
        }

        TemperatureBytes = new byte[TemperatureBytesLen];
        for (int i=0;i<findIndexList.size();i++){
            System.arraycopy(bytes,(Integer) findIndexList.get(i)+2+2,TemperatureBytes,bufLens[i],bufLens[i+1]-2);
        }

        System.out.println("TemperatureBytes---> " + TemperatureBytes.length);

        System.out.println(ByteUtils.byteArrayToHexString(TemperatureBytes, 0,50));
        for (int i=0;i<50;i++){
            byte[] spotTempByte = new byte[4];
            System.arraycopy(TemperatureBytes,i,spotTempByte,3,1);
//            System.out.println(ByteUtils.byteArrayToHexString(spotTempByte, 0,spotTempByte.length) + " ,spotTemp:" + ByteUtils.byteArrayToInt(spotTempByte,true));
        }
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
