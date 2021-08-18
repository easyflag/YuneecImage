package com.yuneec.image.guide;

import com.yuneec.image.Global;
import com.yuneec.image.utils.ByteUtils;
import com.yuneec.image.utils.ParseTemperatureBytes;
import com.yuneec.image.utils.YLog;

import java.util.ArrayList;

public class GuiDeUtil {

    private static GuiDeUtil instance;
    public static GuiDeUtil getInstance() {
        if (instance == null) {
            instance = new GuiDeUtil();
        }
        return instance;
    }

    public boolean isE20T(){
        if (Global.cameraMode.startsWith(Global.cameraE20TMode)){
            return true;
        }else {
            return false;
        }
    }

    public byte[] pParamLine = new byte[640];
    public guide_measure_internal_param_t paramInt;

    private byte[] HEADER = new byte[]{(byte) 0xFF, (byte) 0xE4};

    //解析 paramline 参数，通过FF E4 解析，拷贝02 82之后 640个字节即可。
    public void init(byte[] jpgBytes){
        if (!isE20T()){
            return;
        }
        int length = jpgBytes.length;
        ArrayList findIndexList = ParseTemperatureBytes.simpleFind(jpgBytes, 0, length, HEADER);

        System.arraycopy(jpgBytes, (int) findIndexList.get(0) + 2 + 2,pParamLine,0,640);

        YLog.I("paramline: " + ByteUtils.byteArrayToHexString(pParamLine, 0,100));

        paramInt = guide_measure_internal_param_t.getParamInt(pParamLine);

    }

    private byte[] HEADERRR = new byte[]{(byte) 0x00, (byte) 0x00};
    public void getErrByte(byte[] bytes) {
        ArrayList findIndexList = ParseTemperatureBytes.simpleFind(bytes, 0, bytes.length, HEADERRR);
        for (int i=0;i<findIndexList.size();i++){
            YLog.I("findIndexList getErrByte ---> " + findIndexList.get(i));
        }
    }

}
