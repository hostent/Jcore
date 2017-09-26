package UnitTest;

import java.io.*;
import java.util.List;

import com.alibaba.fastjson.JSON;
 

public class JsonTest {

	public static void main(String[] args) throws IOException {
		
		 File f1 =new File("C:\\Users\\Administrator\\Desktop\\test-x1.json");
		 
		 String jsonStr="";
		 InputStreamReader  isr = new InputStreamReader(new FileInputStream(f1));
		 int temp;
         while((temp=isr.read())!=-1){
              
             
            	 jsonStr+=(char)temp;
             
         }
         
        
		 
         List<OrderLog> list =  JSON.parseArray(jsonStr, OrderLog.class);
		 
		System.out.println(list.size());
		
	}
}
