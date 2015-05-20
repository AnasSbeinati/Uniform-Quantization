package quantum;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

public class FileRW {
	static public String write(ObjectOutputStream oos,String stream) {
		int i=0,size1=stream.length();
		try {
			while(i<size1) {
				if(stream.length()<31) {
					/*int strSize=stream.length();
				    String dd="";
    			    for (int j = strSize; j < 31; j++) {
    			    	dd+="0";
				    }
    			    dd+=stream;
    			    int rr=Integer.parseInt(dd,2);
					oos.writeInt(rr);*/
					return stream;
    		   }
    		   String tempStr=stream.substring(0, 31);
    		   stream=stream.substring(31, stream.length());
    		   i+=31;
    		   int rr=Integer.parseInt(tempStr,2);
    		   oos.writeInt(rr);
			}
		} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return "";
    	}
	static public String read(ObjectInputStream buffer,int height,int width,int comu,long fileSize,Size currentSize) throws IOException {
		String bitsStream="";
		while(currentSize.size<fileSize) {
			int current=buffer.readInt();
			currentSize.size+=Integer.BYTES;
			String tempStr=Integer.toBinaryString(current);
			comu+=31;
			if(tempStr.length()<31) {
				String appe="";
				for (int i =tempStr.length() ; i < 31; i++) {
					appe+="0";
				}
				tempStr=appe+tempStr;
			}
			bitsStream+=tempStr;
			if(bitsStream.length()>=(32*6)) {
				break;
			}
		}
		return bitsStream;
	}
}
