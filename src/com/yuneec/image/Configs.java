package com.yuneec.image;

public class Configs {
	
	public static int SceneWidth = 1300;
	public static int SceneHeight = 700;
	
	public static int MenuHeight = 30;
	
	public static int LineHeight = 40;
	
	public static int LeftPanelWidth = 200;
	public static int CenterPanelWidth = 800;
	public static int RightPanelWidth = 300;
	
	public static int Spacing = 1;
	
	
	public static String backgroundColor = "#252526";
	public static String color1 = "#234F91"; //LeftPanel
	public static String color2 = "#333333"; 
	public static String red_color = "#FF0000";
	public static String grey_color = "#d9d6c3";
	
	
	static class RightPaneImageInfo{
		static int row = 12;
		static int marginTop = 50;
		static int marginLeft = 20;
		static int marginRight = 20;
		static int lineHeight = 30;
		
		static int startX = marginLeft;
		static int startY = marginTop;
		static int endX = Configs.RightPanelWidth - marginRight;
		static int offsetX = (endX -marginLeft)/ 2;
		static int offsetY = lineHeight;
		static int endY = marginTop + offsetY * (row-1);
	}

}
