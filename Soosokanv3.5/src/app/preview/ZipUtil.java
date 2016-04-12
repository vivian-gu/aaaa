package app.preview;



import java.io.BufferedReader;
import java.io.ByteArrayInputStream;   
import java.io.ByteArrayOutputStream;   
import java.io.IOException;   
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;   
import java.util.zip.GZIPOutputStream;   
  
public class ZipUtil {   
    // 压缩   

	/**
	 * To compress the string by Gzip
	 * 
	 * @author Tian
	 * */
	public static String compress(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return str;
        }
        System.out.println("String length : " + str.length());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(str.getBytes());
        gzip.close();
        String outStr = out.toString("ISO-8859-1");
        System.out.println("Output String lenght : " + outStr.length());
        return outStr;
     }
	
 
	/**
	 * To uncompress the String which already compressed by Gzip.
	 * 
	 * @author Tian
	 * */
 public static String unCompress(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return str;
        }
        System.out.println("Input String length : " + str.length());
        GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(str.getBytes("ISO-8859-1")));
        BufferedReader bf = new BufferedReader(new InputStreamReader(gis, "ISO-8859-1"));
        String outStr = "";
        String line;
        while ((line=bf.readLine())!=null) {
          outStr += line;
        }
        System.out.println("Output String lenght : " + outStr.length());
        return outStr;
     }
  
      // 测试方法   
//      public static void main(String[] args) throws IOException {   
//        String temp = "l;jsafljsdoeiuoksjdfpwrp3oiruewoifrjewflk我的得到喀喀喀 看看看safljsdoeiuoksjdfpwrp3oiruewoifrjewflk我的得到喀喀喀 看看看看safljsdoeiuoksjdfpwrp3oiruewoifrjewflk我的得到喀喀喀 看看看看safljsdoeiuoksjdfpwrp3oiruewoifrjewflk我的得到喀喀喀 看看看看safljsdoeiuoksjdfpwrp3oiruewoifrjewflk我的得到喀喀喀 看看看看safljsdoeiuoksjdfpwrp3oiruewoifrjewflk我的得到喀喀喀 看看看看safljsdoeiuoksjdfpwrp3oiruewoifrjewflk我的得到喀喀喀 看看看看safljsdoeiuoksjdfpwrp3oiruewoifrjewflk我的得到喀喀喀 看看看看safljsdoeiuoksjdfpwrp3oiruewoifrjewflk我的得到喀喀喀 看看看看safljsdoeiuoksjdfpwrp3oiruewoifrjewflk我的得到喀喀喀 看看看看safljsdoeiuoksjdfpwrp3oiruewoifrjewflk我的得到喀喀喀 看看看看safljsdoeiuoksjdfpwrp3oiruewoifrjewflk我的得到喀喀喀 看看看看safljsdoeiuoksjdfpwrp3oiruewoifrjewflk我的得到喀喀喀 看看看看看看看看  ";   
//        System.out.println("原字符串="+temp);   
//        System.out.println("原长="+temp.length());   
//        String temp1 = ZipUtil.compress(temp);   
//        System.out.println("压缩后的字符串="+temp1);   
//        System.out.println("压缩后的长="+temp1.length());   
//        System.out.println("解压后的字符串="+ZipUtil.uncompress(temp1));   
//      }   
  
}  


