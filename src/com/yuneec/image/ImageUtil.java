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
//			int length = bytes.length;
//			System.out.println("image bytes length: " + length);
////			String s = ByteUtils.byteArrayToHexString(bytes);
////			System.out.println(s);
//			String s1 = ByteUtils.byteArrayToHexString(bytes, 0, 50);
//			System.out.println(s1);
//			String s2 = ByteUtils.byteArrayToHexString(bytes, length-50, length, length);
//			System.out.println(s2);
			
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
    }

	public static String[] whiteList = {"Make","Model","Date/Time","F-Number","Aperture Value","Focal Length","Color Space"
		,"Exif Image Width","Exif Image Height","Lens Specification"};
	public static ArrayList<ArrayList<String>> imageInfoList = new  ArrayList<ArrayList<String>>();
	private static void storeImageInfo(Tag tag) {
//		String s = tag.getDirectoryName() + " ; " + tag.getTagName()+ " ; " + tag.getDescription();
//    	System.out.println("---> "+s);
    	String tagName = tag.getTagName();
    	String description = tag.getDescription();
		if(Arrays.asList(whiteList).contains(tagName)){
			ArrayList<String> arrayList = new ArrayList<String>();
			arrayList.add(tagName);
			arrayList.add(description);
			imageInfoList.add(arrayList);
		}
	}
	
	

}
