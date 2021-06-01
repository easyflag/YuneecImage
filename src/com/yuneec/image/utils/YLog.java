package com.yuneec.image.utils;

public class YLog {

    private static boolean Debug = true;

    public static void I(String info) {
        if (Debug) {
            System.out.println(info);
        }
    }

	public static void I(String... info) {
		if (Debug) {
			String log = null;
			for (String i:info){
				log += i;
			}
			System.out.println(log);
		}
	}

}
