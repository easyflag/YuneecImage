package com.yuneec.image.guide;

import java.io.File;
import java.io.FileInputStream;
import java.util.Scanner;


public class GuiDe {

    public static short[] lowTempCurve = new short[16384];

    public static short[] highTempCurve = new short[16384];

    public static void init() {

        lowTempCurve = add(LowTempCurve1.lowTempCurve,LowTempCurve2.lowTempCurve);
        highTempCurve = add(HighTempCurve1.highTempCurve,HighTempCurve2.highTempCurve);

//        readLowTempCurvePath();
//        readHighTempCurvePath();
    }

    private static short[] add(short[] a,short[] b){
        short[] c = new short[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }


    private static void readLowTempCurvePath() {
//        String projetPath = GuiDe.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        String projetPath = GuiDe.class.getResource("").getPath();
//        String lowTempCurvePath =  projetPath + "/com/yuneec/image/guide/" + "lowTempCurve";
        String lowTempCurvePath = projetPath +  "lowTempCurve";
        System.out.println("GuiDe ---> " + lowTempCurvePath);

        StringBuilder builder = new StringBuilder();
        try {
            Scanner input= new Scanner(new FileInputStream(lowTempCurvePath));
            while (input.hasNextLine()) {
                builder.append(input.nextLine().trim());
            }
            System.out.println("GuiDe ---> " + builder.toString());
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String[] numbers = builder.toString().split(",");
        for (int i=0;i<numbers.length;i++){
            lowTempCurve[i] = Short.parseShort(numbers[i]);
        }
//        System.out.println("GuiDe ---> ");
    }

    private static void readHighTempCurvePath() {
        String projetPath = new File("").getAbsolutePath();
        String lowTempCurvePath = projetPath + File.separator + "com\\yuneec\\image\\guide\\" + "highTempCurve";
        System.out.println("GuiDe ---> " + lowTempCurvePath);
        StringBuilder builder = new StringBuilder();
        try {
            Scanner input = new Scanner(new FileInputStream(lowTempCurvePath));
            while (input.hasNextLine()) {
                builder.append(input.nextLine().trim());
            }
//            System.out.println("GuiDe ---> " + builder.toString());
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String[] numbers = builder.toString().split(",");
        for (int i=0;i<numbers.length;i++){
            highTempCurve[i] = Short.parseShort(numbers[i]);
        }
//        System.out.println("GuiDe ---> ");
    }

}
