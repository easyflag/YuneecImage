package com.yuneec.image;

public class TemperatureAlgorithm {

    private static TemperatureAlgorithm instance;
    public static TemperatureAlgorithm getInstance() {
        if (instance == null) {
            instance = new TemperatureAlgorithm();
        }
        return instance;
    }

    static float CELSIUS_DEGREE_TO_KELVIN_DEGREE = 273.15f;
    int COMPENSATE_TEMP_HIGH_TEMP = 273;
    int COMPENSATE_TEMP_LOW_TEMP = 264;
    int BOSON_HIGH_GAIN_MAX_TEMP = 120;
    int BOSON_LOW_GAIN_MAX_TEMP = 300;
    /* min temperature we can measure */
    int BOSON_MIN_TEMP = -20;

    static  float   AtmosphereTemperature;
    static float   SceneEmissivity;
    static float   TransmissionCoefficient;
    static int R1;
    static int B1;
    static int F1;
    static int O1;
    static float PixelToFpaTempB;
    static float PixelToFpaTempM;
    static int GainMode;
    static float AmbientTemperature;


    public float getTemperature(int x, int y) {
        float pointTempera = 0;
        float pixel_value = getTemperaByteForXY(x,y);

        float fpa_compensated = (pixel_value + PixelToFpaTempB) / (1 - PixelToFpaTempM);
        double temp = ((B1 / Math.log(R1 * 1.0f / (fpa_compensated - O1) + F1)) - CELSIUS_DEGREE_TO_KELVIN_DEGREE);
        pointTempera = compensate_temp_with_env_temp((float) temp, AmbientTemperature);

        return pointTempera;
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
        Tc = (float) (Tc - CELSIUS_DEGREE_TO_KELVIN_DEGREE);
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


    int tifWidth = 320;
    int tifHeight = 256;
    private float getTemperaByteForXY(int x, int y) {
        float pixel_value = 0;
        byte[] TemperatureBytes = ParseTemperatureBytes.getInstance().TemperatureBytes;
        int index = (int) ((x / (Global.currentOpenImageWidth / tifWidth)) *
                (y / (Global.currentOpenImageHeight / tifHeight)));
        byte[] spotTempByte = new byte[4];
//        System.arraycopy(TemperatureBytes,index,spotTempByte,2,2);
        spotTempByte[2] = TemperatureBytes[index];
        spotTempByte[3] = TemperatureBytes[index+1];
        pixel_value = ByteUtils.byteArrayToInt(spotTempByte,true);
        System.out.print(" spotTempByte:" + ByteUtils.byteArrayToHexString(spotTempByte, 0,spotTempByte.length));
        System.out.println("x:" + x + " ,y:" + y + " ,pixel_value:" + pixel_value + " ,index:" + index);
        System.out.println("********************************************");
        return pixel_value;
    }
}
