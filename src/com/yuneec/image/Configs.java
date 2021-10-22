package com.yuneec.image;

public class Configs {

	public static String version = "1.0.8 Beta";
	public static String version_en = "   Version : " + version;
	public static String version_ch = "   版本 : " + version;
	public static String copyright = "   Copyright (C) 2021-2022 Yuneec Inc.";
	public static String copyright_ch = "   版权所有 (C) 2021-2022 Yuneec 公司.";

	public final static int DefaultSceneWidth = 1320;
	public final static int DefaultSceneHeight = 730;
	public static int SceneWidth = DefaultSceneWidth;
	public static int SceneHeight = DefaultSceneHeight;

	public final static int SystemBarHeight = 20;
	public final static int MenuHeight = 30;
	public final static int LineHeight = 40;
	
	public final static int LeftPanelWidth = 200;

	public final static int DefaultCenterPanelWidth = 800;
	public final static int DefaultCenterPanelHeight = Configs.DefaultSceneHeight - SystemBarHeight - Configs.MenuHeight - Configs.LineHeight;
	public static double CenterPanelWidth = DefaultCenterPanelWidth;
	public static double CenterPanelHeight = DefaultCenterPanelHeight;

	public final static int RightPanelWidth = 320;

	public final static int DefaultImageWidth = 640;
	public final static int DefaultImageHeight = 512;
	
//	public static int Spacing = 1;
	
	public static String backgroundColor = "#252526";
	public static String blue_color = "#234F91";
	public static String lightGray_color = "#333333";
	public static String red_color = "#FF0000";
	public static String grey_color = "#d9d6c3";
	public static String white_color = "#FFFFFF";
	public static String snow_white_color = "#FFFAFA";
	public static String blue2_color = "#0000FF";
	public static String green_color = "#008000";
	public static String light_gray = "#d0d0d0";
	public static String light_gray2 = "#E0E0E0";
	public static String black = "#000000";
	public static String light_black = "#4F4F4F";



	public static String temperatureColor = white_color;
	
	static class RightPaneImageInfo{
		static int row = 11;
		static int marginTop = 50;
		static int marginLeft = 20;
		static int marginRight = 25;
		static int lineHeight = 30;
		
		static int startX = marginLeft;
		static int startY = marginTop;
		static int endX = RightPanelWidth - marginRight;
		static int offsetX = (endX -marginLeft)/ 2;
		static int offsetY = lineHeight;
		static int endY = marginTop + offsetY * (row-1);
	}

}
