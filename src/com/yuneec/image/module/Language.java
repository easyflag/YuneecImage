package com.yuneec.image.module;

import com.yuneec.image.CenterPane;
import com.yuneec.image.Global;
import com.yuneec.image.utils.ImageUtil;
import com.yuneec.image.RightPane;
import com.yuneec.image.TopMenuBar;
import javafx.scene.control.Label;

import java.util.Arrays;

public class Language {

    public static Languages LanguageSelect = Languages.English;

    public enum Languages {
        English,
        Chinese
    }

    private static Language instance;

    public static Language getInstance() {
        if (instance == null) {
            instance = new Language();
        }
        return instance;
    }

    public static String Yuneec_Image_en = " Yuneec Image Tool ";
    public static String Yuneec_Image_ch = " Yuneec 图片工具 ";
    public static String Exit_en = "Exit";
    public static String Exit_ch = "退 出";
    public static String Add_File_en = "Add File";
    public static String Add_File_ch = "添加文件";
    public static String Settings_en = "Settings";
    public static String Settings_ch = "设 置";
    public static String Open_File_en = "Open File\t\t";
    public static String Open_File_ch = "打开文件\t\t";
    public static String Open_Folder_en = "Open Folder\t\t";
    public static String Open_Folder_ch = "打开文件夹\t\t";
    public static String Create_Report_en = "Create Report";
    public static String Create_Report_ch = "生成报告";
    public static String Temperature_Unit_en = "Temperature Unit";
    public static String Temperature_Unit_ch = "温度单位";
    public static String Temperature_en = "Temperature";
    public static String Temperature_ch = "温度";
    public static String Celsius_en = "Celsius(℃)";
    public static String Celsius_ch = "摄氏度(℃)";
    public static String Fahrenheit_en = "Fahrenheit(℉)";
    public static String Fahrenheit_ch = "华氏度(℉)";
    public static String Kelvin_en = "Kelvin(K)";
    public static String Kelvin_ch = "开尔文(K)";
    public static String Language_en = "Language";
    public static String Language_ch = "语言";
    public static String English_en = "English";
    public static String English_ch = "英语";
    public static String Chinese_en = "Chinese";
    public static String Chinese_ch = "中文";
    public static String About_en = "About";
    public static String About_ch = "关于";
    public static String File_Size_en = "File Size";
    public static String File_Size_ch = "文件大小";
    public static String Image_Info_en = "Image Info";
    public static String Image_Info_ch = "图片信息";
    public static String SinglePointTemperature_en = "Click the picture to get the temperature information at this location.";
    public static String SinglePointTemperature_ch = "点击图片可获取此位置的温度信息.";
    public static String BoxTemperature_en = "Select a rectangle on the picture box to get the maximum and minimum temperature information for this area.";
    public static String BoxTemperature_ch = "在图片上框选一个矩形可获取此区域的最大最小温度信息.";
    public static String ColorPaletteTip_en = "Color Palette.";
    public static String ColorPaletteTip_ch = "调色板.";
    public static String ClearTip_en = "One key to clear all temperature information.";
    public static String ClearTip_ch = "一键清除所有温度信息.";

    public static boolean isEnglish() {
        return LanguageSelect == Languages.English;
    }

    public static String getString(String en, String ch) {
        return isEnglish() ? en : ch;
    }

    public void setEnglish() {
        Global.primaryStage.setTitle(Yuneec_Image_en);
        TopMenuBar.getInstance().exitMenuItem.setText(Exit_en);
        TopMenuBar.getInstance().fileMenu.setText(Add_File_en);
        TopMenuBar.getInstance().settingsMenu.setText(Settings_en);
        TopMenuBar.getInstance().openFileMenuItem.setText(Open_File_en);
        TopMenuBar.getInstance().openFolderMenuItem.setText(Open_Folder_en);
        TopMenuBar.getInstance().reportMenuItem.setText(Create_Report_en);
        TopMenuBar.getInstance().TemperatureManeu.setText(Temperature_Unit_en);
        TopMenuBar.getInstance().CelsiusMenuItem.setText(Celsius_en);
        TopMenuBar.getInstance().FahrenheitMenuItem.setText(Fahrenheit_en);
        TopMenuBar.getInstance().KelvinMenuItem.setText(Kelvin_en);
        TopMenuBar.getInstance().LanguageManeu.setText(Language_en);
        TopMenuBar.getInstance().EnglishMenuItem.setText(English_en);
        TopMenuBar.getInstance().ChineseMenuItem.setText(Chinese_en);
        TopMenuBar.getInstance().aboutMenuItem.setText(About_en);
        if (RightPane.getInstance().imageInfoTagNameLabelList.size() >0){
            for (int i=0;i<RightPane.getInstance().imageInfoTagNameLabelList.size()-1;i++){
                Label tagNamelabel = RightPane.getInstance().imageInfoTagNameLabelList.get(i);
                tagNamelabel.setText(ImageUtil.whiteList[Arrays.asList(ImageUtil.whiteList_ch).indexOf(tagNamelabel.getText())]);
            }
            RightPane.getInstance().imageInfoTagNameLabelList.get(
                    RightPane.getInstance().imageInfoTagNameLabelList.size()-1).setText(File_Size_en);
        }
        RightPane.getInstance().titlelabel.setText(Image_Info_en);
        String XYlabelText = RightPane.getInstance().showXYlabel.getText();
        RightPane.getInstance().showXYlabel.setText(XYlabelText.replace(Language.Temperature_ch,Language.Temperature_en));
        ColorPalette.getInstance().changeColorPaletteNameLanguage();
        CenterPane.getInstance().setTooltip();
    }

    public void setChinese() {
        Global.primaryStage.setTitle(Yuneec_Image_ch);
        TopMenuBar.getInstance().exitMenuItem.setText(Exit_ch);
        TopMenuBar.getInstance().fileMenu.setText(Add_File_ch);
        TopMenuBar.getInstance().settingsMenu.setText(Settings_ch);
        TopMenuBar.getInstance().openFileMenuItem.setText(Open_File_ch);
        TopMenuBar.getInstance().openFolderMenuItem.setText(Open_Folder_ch);
        TopMenuBar.getInstance().reportMenuItem.setText(Create_Report_ch);
        TopMenuBar.getInstance().TemperatureManeu.setText(Temperature_Unit_ch);
        TopMenuBar.getInstance().CelsiusMenuItem.setText(Celsius_ch);
        TopMenuBar.getInstance().FahrenheitMenuItem.setText(Fahrenheit_ch);
        TopMenuBar.getInstance().KelvinMenuItem.setText(Kelvin_ch);
        TopMenuBar.getInstance().LanguageManeu.setText(Language_ch);
        TopMenuBar.getInstance().EnglishMenuItem.setText(English_ch);
        TopMenuBar.getInstance().ChineseMenuItem.setText(Chinese_ch);
        TopMenuBar.getInstance().aboutMenuItem.setText(About_ch);
        if (RightPane.getInstance().imageInfoTagNameLabelList.size() >0){
            for (int i=0;i<RightPane.getInstance().imageInfoTagNameLabelList.size()-1;i++){
                Label tagNamelabel = RightPane.getInstance().imageInfoTagNameLabelList.get(i);
                tagNamelabel.setText(ImageUtil.whiteList_ch[Arrays.asList(ImageUtil.whiteList).indexOf(tagNamelabel.getText())]);
            }
            RightPane.getInstance().imageInfoTagNameLabelList.get(
                    RightPane.getInstance().imageInfoTagNameLabelList.size()-1).setText(File_Size_ch);
        }
        RightPane.getInstance().titlelabel.setText(Image_Info_ch);
        String XYlabelText = RightPane.getInstance().showXYlabel.getText();
        RightPane.getInstance().showXYlabel.setText(XYlabelText.replace(Language.Temperature_en,Language.Temperature_ch));
        ColorPalette.getInstance().changeColorPaletteNameLanguage();
        CenterPane.getInstance().setTooltip();
    }


}
