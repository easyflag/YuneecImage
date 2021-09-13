package com.yuneec.image.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class YLog {

    private static boolean Debug = true;

    public static void I(String info) {
        if (Debug) {
            System.out.println(getTime() + info);
        }
    }

	public static String getTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		return df.format(new Date()) + " -> ";
	}


	public static void I(String... info) {
		if (Debug) {
			String log = null;
			for (String i:info){
				log += i;
			}
			System.out.println(getTime() + log);
		}
	}

}
