package quantum;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class QunFuns {
	static public int[][] compressUQ(File source,File des,int Q) {
		//reading the Image
		int[][]pixels=ImageRW.readImage(source.getPath());
		int width=ImageRW.width;
		int height=ImageRW.height;
		ArrayList<Level> table=getTable(Q);
        getBitsStream(des,pixels, height, width, table, Q);
        return pixels;
	}
	static public ArrayList<Level>getTable(int Q) {
		ArrayList<Level> levels=new ArrayList<Level>();
		int step=(int)Math.floor(255/Math.pow(2, Q));
		int comu=0,i=0;
		while(comu<255) {
			Level level=new Level();
			String code=Integer.toBinaryString(i);
			if(code.length()<Q) {
				String temp="";
				for (int j = code.length(); j < Q ;j++) {
					temp+="0";
				}
				code=temp+code;
			}
			level.setCode(code);
			level.setMin(comu);
			comu+=step;
			level.setMax(comu);
			int RQ=(level.getMax()+level.getMin())/2;
			level.setRQ(RQ);
			levels.add(level);
			i++;
		}
		return levels;
	}
    static public void getBitsStream(File file,int[][]pixels,int height,int width,ArrayList<Level>table,int Q) {
    	String bitsStream="";
    	FileOutputStream fos;
		ObjectOutputStream oos;
		try {
			fos = new FileOutputStream(file);
		    oos = new ObjectOutputStream(fos);
		    oos.writeInt(Q);
		    oos.writeInt(height);
		    oos.writeInt(width);
		    int[][]pixelscompressed=new int[height][width];
		    int comu=0;
    	    for (int i = 0; i < height; i++) {
    	    	for (int j = 0; j < width; j++) {
    	    		int numLevel=getNumLevel(pixels[i][j], Q);
    	    		pixelscompressed[i][j]=table.get(numLevel).getRQ();
				    bitsStream+=table.get(numLevel).getCode();
				    if(bitsStream.length()>=992) {
				    	comu+=bitsStream.length();
				    	bitsStream=FileRW.write(oos, bitsStream);
				    	comu-=bitsStream.length();
				    }
				}
    	    }
    	    if(bitsStream.length()!=0) {
    	    	comu+=bitsStream.length();
    	    	 //System.out.println("Colores remained1 "+bitsStream.length());
    	    	bitsStream=FileRW.write(oos, bitsStream);
	    	}
    	    if(bitsStream!="") {
    	    	int strSize=bitsStream.length();
    	   	    //System.out.println("Colores remained2 "+bitsStream.length());
			    String dd="";
			    for (int j = strSize; j < 31; j++) {
			    	dd+="0";
			    }
			    dd+=bitsStream;
			    int rr=Integer.parseInt(dd,2);
				oos.writeInt(rr);
    	    }
    	    System.out.println("Colores were stored "+comu/3);
    	    //ImageRW.writeImage(pixelscompressed, "C:\\Users\\Anoos\\Desktop\\lena2.jpg");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    static private int getNumLevel(int num,int Q) {
    	int step=(int)Math.floor(255/Math.pow(2, Q));
    	return (int)Math.ceil(num/step);
    }
    static public int[][] deCompress(File source,String des) {
    	FileInputStream reader;
    	ObjectInputStream buffer;
    	try {
    		int Q=0;
    		int height=0;
    		int width=0;
    		long fileSize=source.length()-500;
    		System.out.println(fileSize);
    		Size currentSize=new Size();
    		reader = new FileInputStream(source);
			buffer=new ObjectInputStream(reader);
			Q=buffer.readInt();
			currentSize.size+=Integer.BYTES;
			ArrayList<Level>table=getTable(Q);
			HashMap<String, Integer>hashtable=getHash(table);
			height=buffer.readInt();
			currentSize.size+=Integer.BYTES;
			width=buffer.readInt();
			currentSize.size+=Integer.BYTES;
			int[][]pixels=new int[height][width];
			int i=0,j=0;
			int comu=0;
			while(currentSize.size<fileSize) {
				String bitsStream=FileRW.read(buffer,height,width,comu,fileSize,currentSize);
				//System.out.println("size "+currentSize.size);
				comu+=bitsStream.length();
				String str="";
				int current=0;
				while(current<bitsStream.length()) {
					if(current+Q<=bitsStream.length())
					   str=bitsStream.substring(current, current+Q);
					else {
						int s=current+Q;
						System.out.println("Hey "+s+"   "+bitsStream.length());
					}
					current+=Q;
					int RQ=0;
		    		RQ=hashtable.get(str);
		    		if(i<height)
		    		   pixels[i][j]=RQ;
		    		j++;
		    		if(j==width) {
		    			j=0;
		    			i++;
		    			if(i==height){
		    				break;
		    			}
		    		}
				}
			}
			System.out.println("Colores were readed "+comu/3);
			/*for (int k = 0; k < height; k++) {
				for (int k2 = 0; k2 < width; k2++) {
					System.out.print(pixels[k][k2]+"  ");
				}
				System.out.println("------");
			}
			System.out.println("hi "+height+" wid "+width);*/
			ImageRW.writeImage(pixels, des);
			return pixels;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
    }
    static private HashMap<String,Integer>getHash(ArrayList<Level> table) {
    	HashMap<String, Integer>hashtable=new HashMap<String, Integer>();
    	for (Level level : table) {
    		hashtable.put(level.getCode(), level.getRQ());
		}
    	return hashtable;
    }
    static public int MSE(int[][]pixels,int[][]pixelsComp) {
    	int comu=0;
    	for (int i = 0; i < 512; i++) {
			for (int j = 0; j < 512; j++) {
				comu+=Math.pow(pixelsComp[i][j]-pixels[i][j], 2);
			}
		}
    	int MSE=comu/(512*512);
    	System.out.println("The MSE is "+MSE);
    	return MSE;
    }
}
