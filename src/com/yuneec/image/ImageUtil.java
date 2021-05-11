package com.yuneec.image;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.imaging.jpeg.JpegSegmentMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifReader;
import com.drew.metadata.iptc.IptcReader;
import com.yuneec.image.module.Language;

public class ImageUtil {

	public static byte[] read(File bFile) throws IOException {
		BufferedInputStream bf = new BufferedInputStream(new FileInputStream(bFile));
		try {
			byte[] data = new byte[bf.available()];
			bf.read(data);
			return data;
		} finally {
			bf.close();
		}
	}

	public static byte[] read(String bFile) throws IOException {
		return read(new File(bFile).getAbsoluteFile());
	}
	
	public static void readImage(String name){
		try {
			byte[] bytes = read(name);

			ParseTemperatureBytes.getInstance().init(bytes);

			readPic(name);
		} catch (IOException e) {
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
//        System.out.println("---------------- "+method+" ----------------");
        imageInfoList.clear();
		imageInfoSortList.clear();
        // A Metadata object contains multiple Directory objects
        Metadata metadataImage = metadata;
        for (Directory directory : metadata.getDirectories()) {
            // Each Directory stores values in Tag objects
//        	System.out.println("************ >"+directory.toString());
            for (Tag tag : directory.getTags()) {
//                System.out.println("***>"+tag);
            	storeImageInfo(tag);
            }
            // Each Directory may also contain error messages
            for (String error : directory.getErrors()) {
                System.err.println("ERROR: " + error);
            }
        }

        sortImageInfoList();

		try {
			XMPUtil.getInstance().getXmp();
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	public static String[] whiteList = {"Make","Model","Date/Time","F-Number","ISO Speed","Brightness Value","Color Space","Image Width","Image Height"};
	public static String[] whiteList_ch = {"制造","型号","时间","光圈","感光度","亮度","色彩度","宽度","高度"};
	private static ArrayList<ArrayList<String>> imageInfoList = new  ArrayList<ArrayList<String>>();
	public static ArrayList<ArrayList<String>> imageInfoSortList = new  ArrayList<ArrayList<String>>();
	private static void storeImageInfo(Tag tag) {
//		String s = tag.getDirectoryName() + " ; " + tag.getTagName()+ " ; " + tag.getDescription();
//    	System.out.println("---> "+s);
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
			arrayList.add(description);
			imageInfoList.add(arrayList);
		}
	}

}
