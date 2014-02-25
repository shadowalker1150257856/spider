package spider;

import java.io.*;
import java.net.*;
import java.util.regex.*;
import java.util.*;

import org.springframework.stereotype.Component;

@Component
public class Spider{
	
	/*��ȡstream��*/
	public InputStream getStream(String url) throws Exception{
		InputStream stream = null;
		try{
			URL connection = new URL(url);
			return stream = connection.openStream();
		}catch(MalformedURLException e){
			throw new Exception("[malformedURLException,ȱ��Э��ͷ,������URL���Ӹ�ʽ����]: "+url+" "+new Date());
		}catch(IOException e){
			throw new Exception("[IOException,�޷���������]: "+url+" "+new Date());
		}
	}
	
	/*�洢��ҳ*/
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
			throw new Exception("[spider.Spider.storePage()������NullPointerException,�������Ҳ����ļ���ȷ��pagesĿ¼�Ƿ���ȷ]:"+new Date());
		}catch(IOException e){
			e.printStackTrace();
			throw new Exception("[spider.Spider.storePage()������IOException,�޷��򿪶�д�ļ��������ܴ����stream������]:"+new Date());
		}catch(SecurityException e){
			throw new Exception("[spider.Spider.storePage()������SecurityException,����ԭ����]:"+new Date());
		}catch(Exception e){
			throw new Exception(e.getMessage()+"[spider.Spider.storePage()������Exception , �в�֪�������׳�]:"+new Date());
		}
	} 
	
 	/*������ҳ��ȡ�����е�URL���ӣ���������ץȡ*/
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
			throw new Exception("[spider.Spider.getURL()������IOException,�����޷���ӣ������Ǵ����stream������]:"+new Date());
		}
	}
}
