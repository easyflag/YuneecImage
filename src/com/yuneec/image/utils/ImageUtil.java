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
import com.yuneec.image.module.RightImageInfo;

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
	public static void readImageExifAndXmp(String name){
//		YLog.I("readImageExifAndXmp: " + name);
		try {
//			imageBytes = readJpgToByte(name);
			readPic(name);
			XMPUtil.getInstance().getXmp();
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
    }

	private static void storeImageInfo(Tag tag) {
//		String s = tag.getDirectoryName() + " ; " + tag.getTagName()+ " ; " + tag.getDescription();
//    	YLog.I("EXIF ---> "+s);
		String directoryName = tag.getDirectoryName();
    	String tagName = tag.getTagName();
    	String description = tag.getDescription();
		if(tagName.equals(RightImageInfo.make[0])){
			RightImageInfo.make[3] = description;
		}
		if(tagName.equals(RightImageInfo.model[0])){
			RightImageInfo.model[3] = description;
		}
		if(tagName.equals(RightImageInfo.imageDescription[0])){
			RightImageInfo.imageDescription[3] = description;
		}
		String imageWidth = "640";
		String imageHeight = "512";
		if(tagName.equals(RightImageInfo.sharpness[0])){
			imageWidth = description;
		}
		if(tagName.equals(RightImageInfo.sharpness[1])){
			imageHeight = description;
		}
		RightImageInfo.sharpness[4] = imageWidth + "x" + imageHeight;

		if(tagName.equals(RightImageInfo.time[0])){
			RightImageInfo.time[3] = description;
		}
		if(tagName.equals(RightImageInfo.longitude[0])){
			RightImageInfo.longitude[3] = description;
		}
		if(tagName.equals(RightImageInfo.latitude[0])){
			RightImageInfo.latitude[3] = description;
		}
		 if(tagName.equals(RightImageInfo.fileSize[0])){
			Float size = Float.parseFloat(description.replace("bytes","").trim()) / 1024 / 1024f;
			RightImageInfo.fileSize[3] = (String.format("%.3f",size) + " M");
		}
		 if(tagName.equals("Model")){
//			YLog.I(" Model ---> "+ description);
			Global.cameraMode = description;
		}
	}

}
