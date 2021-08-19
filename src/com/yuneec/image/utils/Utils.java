package com.yuneec.image.utils;

import com.yuneec.image.Global;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class Utils {

	public static boolean mouseLeftClick(MouseEvent e){
		return e.getButton().name().equals(MouseButton.PRIMARY.name());
	}
	
	public static boolean mouseMiddleClick(MouseEvent e){
		return e.getButton().name().equals(MouseButton.MIDDLE.name());
	}
	
	public static boolean mouseRightClick(MouseEvent e){
		return e.getButton().name().equals(MouseButton.SECONDARY.name());
	}

	//	F=(℃×9/5)+32
	//	K=℃+273.15
	private static String format = "%1.1f";
	public static String getFormatTemperature(float temperature) {
		String t = String.format(format, temperature) + "℃";
		if (Global.NowTemperatureUnit == Global.TemperatureUnit.Fachrenheit) {
			float f = (temperature * 9 / 5) + 32;
			t = String.format(format, f) + "℉";
		} else if (Global.NowTemperatureUnit == Global.TemperatureUnit.Kelvin) {
			float k = temperature + 273.15f;
			t = String.format(format, k) + "K";
		}
		return t;
	}

}
