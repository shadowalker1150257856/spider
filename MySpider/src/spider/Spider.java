package spider;

import java.io.*;
import java.net.*;
import java.util.regex.*;
import java.util.*;

import org.springframework.stereotype.Component;

@Component
public class Spider{
	
	/*获取stream流*/
	public InputStream getStream(String url) throws Exception{
		InputStream stream = null;
		try{
			URL connection = new URL(url);
			return stream = connection.openStream();
		}catch(MalformedURLException e){
			throw new Exception("[malformedURLException,缺少协议头,可能是URL链接格式不对]: "+url+" "+new Date());
		}catch(IOException e){
			throw new Exception("[IOException,无法建立链接]: "+url+" "+new Date());
		}
	}
	
	/*存储网页*/
	public void storePage(InputStream stream,String url,int fileNumbers) throws Exception{
		try{
			InputStream temp_stream = stream;
			url = url.replace(".","p");
			url = url.replace("http://","p");
			String line;
			String sep = System.getProperty("file.separator");
			String path =  "pages"+sep+fileNumbers+".html";
			File newFile = new File(path);
			if(!newFile.exists()){
				newFile.createNewFile();
				FileWriter fileWriter = new FileWriter(newFile);
			
				BufferedReader filedata = new BufferedReader(new InputStreamReader(temp_stream));
				while((line = filedata.readLine())!=null){
					fileWriter.write(line);
				}
				filedata.close();
				fileWriter.close();			
			}
		}catch(NullPointerException e){
			throw new Exception("[spider.Spider.storePage()方法，NullPointerException,可能是找不到文件，确认pages目录是否正确]:"+new Date());
		}catch(IOException e){
			e.printStackTrace();
			throw new Exception("[spider.Spider.storePage()方法，IOException,无法打开读写文件流，可能传入的stream有问题]:"+new Date());
		}catch(SecurityException e){
			throw new Exception("[spider.Spider.storePage()方法，SecurityException,错误原因不详]:"+new Date());
		}catch(Exception e){
			throw new Exception(e.getMessage()+"[spider.Spider.storePage()方法，Exception , 有不知名错误被抛出]:"+new Date());
		}
	} 
	
 	/*解析网页，取出其中的URL链接，供后期再抓取*/
	public Vector<String> getURL(InputStream stream) throws Exception{		
		try{
			InputStream temp_stream = stream;
			Vector<String> urlQueue = new Vector<String>();
			String data;
			BufferedReader filedata = new BufferedReader(new InputStreamReader(temp_stream));
			while((data = filedata.readLine())!=null){
				data += data;
				Pattern pattern = Pattern.compile("<a.*href=('//.*'|\"//.*\")(\\s|>)");
				Matcher matcher = pattern.matcher(data);
				boolean result = matcher.find();
				while(result){
					String newUrl = matcher.group();
					newUrl = newUrl.replaceFirst(".+href=\""," ");
					int start_index = newUrl.indexOf("\"");
					newUrl = newUrl.substring(1,start_index);
					newUrl = "http:"+newUrl;
					urlQueue.add(newUrl);
					result = matcher.find();
				}
			}			
			filedata.close();
			return urlQueue; 
		}catch(IOException e){
			throw new Exception("[spider.Spider.getURL()方法，IOException,队列无法添加，可能是传入的stream有问题]:"+new Date());
		}
	}
}
