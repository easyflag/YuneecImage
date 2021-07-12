package com.yuneec.image.utils;

import com.yuneec.image.Global;

public class TemperatureAlgorithm {

    private static TemperatureAlgorithm instance;
    public static TemperatureAlgorithm getInstance() {
        if (instance == null) {
            instance = new TemperatureAlgorithm();
        }
        return instance;
    }

    float CELSIUS_DEGREE_TO_KELVIN_DEGREE = 273.15f;
    int COMPENSATE_TEMP_HIGH_TEMP = 273;
    int COMPENSATE_TEMP_LOW_TEMP = 264;
    int BOSON_HIGH_GAIN_MAX_TEMP = 120;
    int BOSON_LOW_GAIN_MAX_TEMP = 300;
    /* min temperature we can measure */
    int BOSON_MIN_TEMP = -20;

    static float AtmosphereTemperature;
    static float SceneEmissivity;
    static float TransmissionCoefficient;
    static int R1;
    static int B1;
    static int F1;
    static int O1;
    static float PixelToFpaTempB;
    static float PixelToFpaTempM;
    static int GainMode;
    static float AmbientTemperature;

    public float getTemperatureTest(int pixel_value) {
        float fpa_compensated = (0x9851 + PixelToFpaTempB) / (1 - PixelToFpaTempM);
        double temp = ((B1 / Math.log(R1 * 1.0f / (fpa_compensated - O1) + F1)) - CELSIUS_DEGREE_TO_KELVIN_DEGREE);
        return compensate_temp_with_env_temp((float) temp, AmbientTemperature);
    }

    public float getTemperature(int x, int y) {
        float pointTemperature;
        int pixel_value = getTemperaByteForXY(x,y);  // 0x5478 5478  --  21624
        float fpa_compensated = (pixel_value + PixelToFpaTempB) / (1 - PixelToFpaTempM);
        double temp = ((B1 / Math.log(R1 * 1.0f / (fpa_compensated - O1) + F1)) - CELSIUS_DEGREE_TO_KELVIN_DEGREE);
        pointTemperature = compensate_temp_with_env_temp((float) temp, AmbientTemperature);
//        YLog.I(" ---- > pointTemperature :" + pointTemperature);
//        YLog.I(" -----------------------------------------------------------------------");
        return pointTemperature;
    }

    private int getTemperaByteForXY(int x, int y) {
        int pixel_value = 0;

        int index = getTemperaByteIndex(x,y);

        byte[] spotTempByte = new byte[4];
//        System.arraycopy(TemperatureBytes,index,spotTempByte,2,2);
//        spotTempByte[3] = TemperatureBytes[index];
//        spotTempByte[2] = TemperatureBytes[index + 1];
//        pixel_value = ByteUtils.byteArrayToInt(spotTempByte,false);

        spotTempByte[0] = ParseTemperatureBytes.getInstance().TemperatureBytes[index];
        spotTempByte[1] = ParseTemperatureBytes.getInstance().TemperatureBytes[index + 1];
        pixel_value = ByteUtils.getInt(spotTempByte);

//        YLog.I(" spotTempByte:" + ByteUtils.byteArrayToHexString(spotTempByte, 0,spotTempByte.length-2));
//        YLog.I(" x:" + x + " ,y:" + y + " ,index:" + index + " ,pixel_value:" + pixel_value);
        return pixel_value;
    }

    public static boolean SupportScale = false;
    private int getTemperaByteIndex(int x, int y){
        if (SupportScale){
            if (ParseTemperatureBytes.getInstance().TemperatureBytes.length == TemperatureLen){
                return getTemperaByteIndexSupportScaleForXY(x,y);
            }else {
                return getTemperaByteIndexSupportScaleForXY2(x,y);
            }
        }else {
            return getTemperaByteIndexForXY(x,y);
        }
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
        return index;
    }

    int tifWidth = 320;
    int tifHeight = 256;
    static int TemperatureLen = 163846;
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
        }
        return index;
    }

    float compensate_temp_with_env_temp(float temp, float envTemp)
    {
       float caliTemp = (float) 23.0; /* calibration was done in env 23 */
        float T = (float)CELSIUS_DEGREE_TO_KELVIN_DEGREE + temp;
        float Tc = 0;
        float beta  = 0;
        float alpha = 0;
        float k = 0;
        float coff = 1.0f;
        boolean smoonth_comp = false;
        //15755
        if(T > COMPENSATE_TEMP_HIGH_TEMP)
        {
            /* add extra coff when env temperature low than 23 */
            if(envTemp > caliTemp)
            {
                beta  = -5000.6f;
                alpha = (T*(T - 311.49f) + 15920f);
                k = 0;
            }
            else
            {
                beta  = -365.56f;
                alpha = (T*(T - 528) + 69912);
                k = 0;
                coff = 0.1f;
                smoonth_comp = true;
            }
        }
        else if (T < COMPENSATE_TEMP_LOW_TEMP)
        {
            beta  = -365.56f;
            alpha = (T*(-T + 536) - 72040);
            k = 3.38f;
        }
        else
        {
            /* temperature between low and high */
            beta  = -365.56f;
            alpha = (T*(T - 528) + 69912);
            k = 0;
        }
        if(Math.abs(alpha) <= 10)
        {
            return temp;
        }
        Tc = (beta/alpha - k) * (envTemp - caliTemp);

        if(smoonth_comp && Tc > 0)
        {
            Tc = (float) (Math.sqrt(Tc) + T);
        }
        else
        {
            Tc = Tc * coff +  T;
        }
        Tc = (Tc - CELSIUS_DEGREE_TO_KELVIN_DEGREE);
        if(Math.abs(Tc - temp) > 20.0)
        {
            Tc = temp;
        }
        Tc = constrain_final_temperature(Tc);
        return Tc;
    }

    float constrain_final_temperature(float inTemp)
    {
        float temp = inTemp;
        int  gainMode = GainMode;
        /* max temperature is different for high/low gain mode */
        if(gainMode == 0)
        {
            if(inTemp > BOSON_HIGH_GAIN_MAX_TEMP)
            {
                temp = BOSON_HIGH_GAIN_MAX_TEMP;
            }
        }
        else if(gainMode == 1)
        {
            if(inTemp > BOSON_LOW_GAIN_MAX_TEMP)
            {
                temp = BOSON_LOW_GAIN_MAX_TEMP;
            }
        }
        if(inTemp < BOSON_MIN_TEMP)
        {
            temp = BOSON_MIN_TEMP;
        }
        return temp;
    }


}
