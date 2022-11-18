package com.yuneec.image.dll;

import com.yuneec.image.module.Language;
import com.yuneec.image.utils.ToastUtil;

public class YuneecGuide {
    static {
        try {
            String path = System.getProperty("user.dir") + "\\lib\\";
            System.load(path + "YuneecGuide.dll");
        } catch (Exception e) {
            ToastUtil.toast("loadLibrary fail:", new int[]{50,0});
        }
//        System.loadLibrary("lib/GuideImageAnalysis");
    }

    private static YuneecGuide instance;
    public static YuneecGuide I() {
        if (instance == null) {
            instance = new YuneecGuide();
        }
        return instance;
    }

    public static void main(String[] args) {
        YuneecGuide y = new YuneecGuide();
        int sum = y.DLL_ADD(2, 4);
        System.out.println("Java call cpp dll result:" + sum);
    }

    public native int DLL_ADD(int a, int b);
    public native int DLL_SUB(int a, int b);

    public native byte[] guideToRGB(byte[] TemperatureBytes, int size, int paletteIndex);

    public native float guideGrayTemper(short TemperatureBytes, byte[] pParamline, int len,
                                        int emiss, int relHum, int distance, short reflectedTemper, short atmosphericTemper, int modifyK, short modifyB);


/*com.yuneec.image.demo.ProgressDemo.CallBack
   javac YuneecGuide.java
   javah -classpath F:\git\github\YuneecImage\src -jni com.yuneec.image.dll.YuneecGuide
   javah -classpath . -jni com.yuneec.image.dll.YuneecGuide
   dumpbin -imports F:\git\YuneecGuide\x64\Debug\YuneecGuide.dll
   dumpbin -imports F:\git\YuneecGuide\x64\Release\YuneecGuide.dll
*/

}
