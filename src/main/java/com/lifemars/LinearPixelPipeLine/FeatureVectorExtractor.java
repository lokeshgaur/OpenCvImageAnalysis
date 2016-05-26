package com.lifemars.LinearPixelPipeLine;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.FeatureDetector;
import org.opencv.highgui.Highgui;

public class FeatureVectorExtractor {
	
	private static FeatureDetector detector ;
    public static Mat getImageinMatFormat(String filename){
        Mat mat = null;
        System.out.println("filename to read: "+filename);
        mat = Highgui.imread(filename);
        return mat;
    }
    public static String  getFeatureVectorOFImage(String fileName){
    	Mat mat_image = getImageinMatFormat(fileName);
        detector = FeatureDetector.create(FeatureDetector.ORB);
        DescriptorExtractor descriptor = DescriptorExtractor.create(DescriptorExtractor.ORB);
        Mat descriptors1 = new Mat();
        MatOfKeyPoint keypoints1 = new MatOfKeyPoint();
        detector.detect(mat_image, keypoints1);
        descriptor.compute(mat_image, keypoints1, descriptors1);
        
        double[] val = new double[descriptors1.height() * descriptors1.width() *  descriptors1.channels()];
       
        long size = descriptors1.height() * descriptors1.width() *  descriptors1.channels();
        StringBuilder s = new StringBuilder();
        int ch = descriptors1.channels();
        int index=0;
       /* System.out.println("des_Height: "+descriptors1.height());
        System.out.println("des_width: "+ descriptors1.width());
        System.out.println("size: "+size);*/
        for(int i =0; i < descriptors1.height();i++){
        	for(int j =0;j < descriptors1.width();j++){
        		double[] data = descriptors1.get(i, j);
        	    for (int k = 0; k < ch; k++) //Runs for the available number of channels
        	      {
        	    	    String str  = Double.toString(data[k]);
        	    	    double d =0;
        	    	    if(str.length()>=4){
        	    	    	str=str.substring(0,1);
        	    	    	d=Double.parseDouble(str);
        	    	    }else{
        	               d = data[k]; //Pixel modification done here
        	    	    }
        	            s.append(d+" ");
        	    	  
        	      }
        	}
        } 
        
        return s.toString().replace(".0", "").replace("[", "").replace("]","");
    }
    // array of supported extensions (use a List if you prefer)
    static final String[] EXTENSIONS = new String[]{
        "gif", "png", "bmp","jpeg", "jpg" // and other formats you need
    };
    // filter to identify images based on their extensions
    static final FilenameFilter IMAGE_FILTER = new FilenameFilter() {

        public boolean accept(final File dir, final String name) {
            for (final String ext : EXTENSIONS) {
                if (name.endsWith("." + ext)) {
                    return (true);
                }
            }
            return (false);
        }
    };
	
	public static void main(String[] args){
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
        BufferedWriter output_withlabel = null;
        BufferedWriter output_withoutlabel = null;
        try {
            File file_withlabel = new File("/Users/gaurl/Code/LifeMars/ClouderaShare/Data_withLabel.txt");
            File file_withoutlabel = new File("/Users/gaurl/Code/LifeMars/ClouderaShare/Data_withoutLabel.txt");
            output_withlabel = new BufferedWriter(new FileWriter(file_withlabel));
            output_withoutlabel = new BufferedWriter(new FileWriter(file_withoutlabel));
            File dir_train = new File("/Users/gaurl/Code/LifeMars/ClouderaShare/train");
            if(dir_train.isDirectory()){
            	for(File f: dir_train.listFiles(IMAGE_FILTER)){
            		//System.out.println("FileName: "+f.getName());
            		StringBuilder text_withlabel = new StringBuilder("1,"+f.getName()+",");
            		String s = getFeatureVectorOFImage(Utils.TRAIN_DATA_DIR+f.getName());
            		
            		text_withlabel.append(s);
            		output_withlabel.write(text_withlabel.toString());
            		output_withlabel.newLine();
            		//output_withoutlabel.write(v.toString().substring(1, v.toString().length()-1).replace(",", " "));
            		output_withoutlabel.write(s);
            		output_withoutlabel.newLine();;
            	}
            }
        } catch ( IOException e ) {
            e.printStackTrace();
        } finally {
          if ( output_withlabel != null ) {
            try {
            	output_withlabel.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
          }
          if ( output_withoutlabel != null ) {
              try {
              	output_withoutlabel.close();
  			} catch (IOException e) {
  				// TODO Auto-generated catch block
  				e.printStackTrace();
  			}
            }
          
        }
		
		
	}

}
