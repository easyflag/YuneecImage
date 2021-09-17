package com.yuneec.image.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.yuneec.image.Global;
import com.yuneec.image.module.Language;

public class ImageUtil {

	public static byte[] readJpgToByte(String bFile) throws IOException {
		return read(new File(bFile).getAbsoluteFile());
	}

	private static byte[] read(File bFile) throws IOException {
		BufferedInputStream bf = new BufferedInputStream(new FileInputStream(bFile));
		try {
			byte[] data = new byte[bf.available()];
			bf.read(data);
			return data;
		} finally {
			bf.close();
		}
	}

	public static byte[] imageBytes;
	public static void readImageExif(String name){
		try {
//			imageBytes = readJpgToByte(name);
			readPic(name);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void readPic(String path) {
		File file = new File(path);
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(file);
            print(metadata, "Using ImageMetadataReader");
        } catch (ImageProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
        	e.printStackTrace();
        }
        
//        try {
//            Metadata metadata = JpegMetadataReader.readMetadata(file);
//            print(metadata, "Using JpegMetadataReader");
//        } catch (JpegProcessingException e) {
//        	e.printStackTrace();
//        } catch (IOException e) {
//        	e.printStackTrace();
//        }
//        
//        try {
//            Iterable<JpegSegmentMetadataReader> readers = Arrays.asList(new ExifReader(), new IptcReader());
//            Metadata metadata = JpegMetadataReader.readMetadata(file, readers);
//            print(metadata, "Using JpegMetadataReader for Exif and IPTC only");
//        } catch (JpegProcessingException e) {
//        	e.printStackTrace();
//        } catch (IOException e) {
//        	e.printStackTrace();
//        }

	}
	
	private static void print(Metadata metadata, String method){
//        YLog.I("---------------- "+method+" ----------------");
        imageInfoList.clear();
		imageInfoSortList.clear();
        // A Metadata object contains multiple Directory objects
        Metadata metadataImage = metadata;
        for (Directory directory : metadata.getDirectories()) {
            // Each Directory stores values in Tag objects
//        	YLog.I("************ >"+directory.toString());
            for (Tag tag : directory.getTags()) {
//                YLog.I("***>"+tag);
            	storeImageInfo(tag);
            }
            // Each Directory may also contain error messages
            for (String error : directory.getErrors()) {
                System.err.println("ERROR: " + error);
            }
        }
        sortImageInfoList();
    }

	private static void sortImageInfoList() {
		String[] list = Language.isEnglish()?whiteList:whiteList_ch;
		for (int i=0;i<list.length;i++){
			String name = list[i];
			int index = getIndexForImageInfoList(name);
			imageInfoSortList.add(imageInfoList.get(index));
		}
		imageInfoSortList.add(imageInfoList.get(imageInfoList.size()-1));
	}

	private static int getIndexForImageInfoList(String name) {
		int index = 0;
		for (int i=0;i<imageInfoList.size();i++){
			ArrayList<String> list = imageInfoList.get(i);
			String tagName = list.get(0);
			if (name.equals(tagName)){
				index = i;
				break;
			}
		}
		return index;
	}

	public static String[] whiteList = {"Make","Model","Date/Time","F-Number","ISO Speed","Brightness Value","Image Width","Image Height"};
	public static String[] whiteList_ch = {"制造","型号","时间","光圈","感光度","亮度","宽度","高度"};
	private static ArrayList<ArrayList<String>> imageInfoList = new  ArrayList<ArrayList<String>>();
	public static ArrayList<ArrayList<String>> imageInfoSortList = new  ArrayList<ArrayList<String>>();
	private static void storeImageInfo(Tag tag) {
//		String s = tag.getDirectoryName() + " ; " + tag.getTagName()+ " ; " + tag.getDescription();
//    	YLog.I("EXIF ---> "+s);
		String directoryName = tag.getDirectoryName();
    	String tagName = tag.getTagName();
    	String description = tag.getDescription();
    	if(directoryName.startsWith("Exif")){
			if(Arrays.asList(whiteList).contains(tagName)){
				ArrayList<String> arrayList = new ArrayList<String>();
				int index = Arrays.asList(whiteList).indexOf(tagName);
				arrayList.add(Language.getString(tagName,whiteList_ch[index]));
				arrayList.add(description);
				imageInfoList.add(arrayList);
			}
		}
    	if(tagName.equals("File Size")){
			ArrayList<String> arrayList = new ArrayList<String>();
			arrayList.add(Language.getString(tagName,Language.File_Size_ch));
			Float size = Float.parseFloat(description.replace("bytes","").trim()) / 1024 / 1024f;
			arrayList.add(String.format("%.3f",size) + " M");
			imageInfoList.add(arrayList);
		}
		if(tagName.equals("Model")){
//			YLog.I(" Model ---> "+ description);
			Global.cameraMode = description;
		}
	}

}
