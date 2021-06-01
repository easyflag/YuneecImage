package com.yuneec.image.utils;

import java.util.ArrayList;

public class BoxTemperatureUtil {

	public static BoxTemperatureUtil instance;
	public static BoxTemperatureUtil getInstance() {
		if (instance == null) {
			instance = new BoxTemperatureUtil();
		}
		return instance;
	}

	private ArrayList<int[]> xyList = new ArrayList<int[]>();
	private ArrayList temperatureList = new ArrayList();
	private int startX = 0;
	private int startY = 0;
	private int endX = 0;
	private int endY = 0;

	public void init(int startLineX, int startLineY, int endLineX, int endLineY,MaxMinTemperature callback) {
		maxMinTemperature = callback;
		String s1 = "startLineX=" + startLineX + " startLineY=" + startLineY;
		String s2 = "endLineX=" + endLineX + " endLineY=" + endLineY;
//		YLog.I("BoxTemperatureUtil :" + s1 + " ; " + s2);
		startX = startLineX;
		startY = startLineY;
		endX = endLineX;
		endY = endLineY;
		if(startLineX > endLineX){
			startX = endLineX;
			endX = startLineX;
		}
		if (startLineY > endLineY){
			startY = endLineY;
			endY = startLineY;
		}
		xyList.clear();
		temperatureList.clear();
		getBoxAllXY();
		getBoxAllTemperature();
		getMaxMinTemperature();
	}

	private void getBoxAllXY() {
		int row = endY - startY;
		int column = endX - startX;
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
				int[] xy = new int[]{startX + j ,startY + i};
				xyList.add(xy);
			}
		}
	}

	private void getBoxAllTemperature() {
		for (int i=0;i<xyList.size();i++){
			int x = xyList.get(i)[0];
			int y = xyList.get(i)[1];
			temperatureList.add(TemperatureAlgorithm.getInstance().getTemperature(x,y));
		}
	}

	float maxTemperature = 0;
	int maxTemperatureIndex = 0;
	float minTemperature = 0;
	int minTemperatureIndex = 0;
	private void getMaxMinTemperature(){
		maxTemperature = (float)temperatureList.get(0);
		minTemperature = (float)temperatureList.get(0);
		for (int i = 0; i < temperatureList.size(); i++) {
			if ((float)temperatureList.get(i) > maxTemperature) {
				maxTemperature = (float)temperatureList.get(i);
				maxTemperatureIndex = i;
			}
			if ((float)temperatureList.get(i) < minTemperature) {
				minTemperature = (float)temperatureList.get(i);
				minTemperatureIndex = i;
			}
		}
//		YLog.I("BoxTemperatureUtil..." + " maxTemperature:" + maxTemperature  + " maxTemperatureIndex:" + maxTemperatureIndex
//				+ " minTemperature:" + minTemperature + " minTemperatureIndex:" + minTemperatureIndex);

		int[] maxTemperatureXY = xyList.get(maxTemperatureIndex);
		int[] minTemperatureXY = xyList.get(minTemperatureIndex);

		if (maxMinTemperature != null){
			maxMinTemperature.onResust(maxTemperature,maxTemperatureXY,minTemperature,minTemperatureXY);
		}
	}

	public void setMaxMinTemperature(MaxMinTemperature maxMinTemperature) {
		this.maxMinTemperature = maxMinTemperature;
	}

	public MaxMinTemperature maxMinTemperature;
	public interface MaxMinTemperature{
		void onResust(float maxTemperature,int[] maxTemperatureXY,float minTemperature,int[] minTemperatureXY);
	}



}
