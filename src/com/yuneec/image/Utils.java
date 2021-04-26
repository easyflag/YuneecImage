package com.yuneec.image;

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

}
