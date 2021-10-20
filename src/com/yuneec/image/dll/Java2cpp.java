package com.yuneec.image.dll;

import com.yuneec.image.utils.ByteUtils;
import com.yuneec.image.utils.ParseTemperatureBytes;
import com.yuneec.image.utils.YLog;
import sun.misc.BASE64Decoder;

import java.io.*;

public class Java2cpp {

    static {
        System.loadLibrary("lib/guide");
        System.loadLibrary("lib/GuideImageAnalysis");
    }

    private static Java2cpp instance;
    public static Java2cpp I() {
        if (instance == null) {
            instance = new Java2cpp();
        }
        return instance;
    }

    public static void main(String[] args) {
        int sum = 0;
        Java2cpp test = new Java2cpp();
        sum = test.DLL_ADD(2, 4);
        System.out.println("Java call cpp dll result:" + sum);

//        byte[] y16Data = ParseTemperatureBytes.getInstance().TemperatureBytes;
//        byte[] bytes = Java2cpp.I().y16torgb24(y16Data,1);
    }

    public native int DLL_ADD(int a, int b);
    public native int DLL_SUB(int a, int b);

    public native byte[] guideToRGB(byte[] TemperatureBytes, int size, int paletteIndex);


    public byte[] y16torgb24(byte[] y16Data, int paletteIndex){
        int size = 640*512;
        byte[]  rgb24Data = Java2cpp.I().guideToRGB(y16Data,size,paletteIndex);
//        setImage(y16Data);
//        YLog.I("y16torgb24 rgb24: " + rgb24Data.length);
//        YLog.I("y16torgb24 rgb24: " + ByteUtils.byteArrayToHexString(rgb24Data,0,100));
        return rgb24Data;
    }

    private void setImage(byte[] bytes){
        FileOutputStream fop = null;
        File file;
        try {
            file = new File("c:/y16.txt");
            fop = new FileOutputStream(file);
            if (!file.exists()) {
                file.createNewFile();
            }
            fop.write(bytes);
            fop.flush();
            fop.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fop != null) {
                    fop.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}

/*
    int guide_image_y16torgb24
            (short* y16Data,unsigned char* rgb24Data,int size,int paletteIndex)

            y16Data  ---> 640*512*2
            rgb24Data ---> 640*512*3 (RGB)
            size ---> 640*512*2

*/


/*
   javac Java2cpp.java
*  javah -classpath F:\intellijSpace\YuneecImage\src com.yuneec.image.dll.Java2cpp
*


JNIEXPORT jbyteArray JNICALL Java_com_yuneec_image_dll_Java2cpp_guideToRGB
	(JNIEnv *env, jobject, jbyteArray temperatureBytes, jint size, jint paletteIndex)
	{
		int rgb24size = 640*512*3;
		jbyteArray rgb24DataToJave = env->NewByteArray(rgb24size);

		//guide_image_y16torgb24(short* y16Data, unsigned char* rgb24Data, int size, int paletteIndex);
		jbyte* y16Data = env->GetByteArrayElements(temperatureBytes, NULL);
		unsigned char* rgb24Data = (unsigned char*)malloc(rgb24size);
		guide_image_y16torgb24((short*)y16Data, rgb24Data,size,paletteIndex);

		env->SetByteArrayRegion(rgb24DataToJave,0, rgb24size,(jbyte *)rgb24Data);

		return rgb24DataToJave;
	}

* */


