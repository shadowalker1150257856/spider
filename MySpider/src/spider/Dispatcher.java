package spider;

import java.io.*;
import java.util.*;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.w3c.dom.NodeList;

import aspects.Ini;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan({"spider","aspects"})
public class Dispatcher{
	
	/*��ʼ�ļ����
	 * ���������߳���
	 * δ���ץȡ����
	 * �����ץȡ����
	 * ��ǰ����������*/
	private static int fileNumbers;
	private static int threadNumbers;
	private static UngetURLQueue unGetUrlQueue;
	private static FinishedURLQueue finishedUrlQueue;
	private static ApplicationContext ctx;
	
	/*��ʼ���������*/
	public static void ini(String[] args) throws Exception{
		File pages = new File("pages");
		if(!pages.isDirectory()){
			pages.mkdir();
		}
		fileNumbers = Ini.getFileStart();
		threadNumbers = Ini.getThreadNumbers();
		ctx = SpringApplication.run(Dispatcher.class, args);
		
		/*��ʼURL��������*/
		unGetUrlQueue = (UngetURLQueue) ctx.getBean("ungetURLQueue");
		NodeList start_urls = Ini.getStartUrls();
		int length = start_urls.getLength();
		int index;
		for(index=0;index<length;++index){
			unGetUrlQueue.insertURL(Ini.getUrl(start_urls, index));
		}
		NodeList unget_urls = Ini.getUngetUrls();
		length = unget_urls.getLength();
		for(index=0;index<length;index++){
			unGetUrlQueue.insertURL(Ini.getUrl(unget_urls, index));
		}
		
		/*��ʼ�����ץȡURL��������*/
		finishedUrlQueue = (FinishedURLQueue) ctx.getBean("finishedURLQueue");
		NodeList fin_urls = Ini.getFinUrls();
		length = fin_urls.getLength();
		for(index=0;index<length;index++){
			finishedUrlQueue.insertURL(Ini.getUrl(fin_urls, index));
		}
		
		Ini.clear();
	}

	/*�������*/
	public static void main(String[] args) throws Exception{
		
		/*��ӹ����̣߳���������ֹʱ��������������*/
		Runtime.getRuntime().addShutdownHook(new Thread(){
			public void run(){
				try{
					Ini.setFileStart(String.valueOf(Dispatcher.fileNumbers));
					
					String finUrl;
					while((finUrl = Dispatcher.finishedUrlQueue.removeURL())!=null){
						Ini.addFinUrl(finUrl);
					}
					
					String url;
					while((url = Dispatcher.unGetUrlQueue.removeURL())!=null){
						Ini.addUngetUrl(url);
					}
				}catch(Exception e){}
			}
		});
		ini(args);
		System.out.println("the spider is working");
		/*���̴߳������������С֩�룬�ڶ��̴߳������±��ֽϺã����̴߳������£�Ч��Զ�����޿��
		 * �ĵ��߳�֩��*/
		int start_thread;
		for(start_thread=0;start_thread<threadNumbers;start_thread++){
			(new littleSpider(unGetUrlQueue,finishedUrlQueue,(Spider)ctx.getBean("spider"))).start();
		}
	}
	
	/*�ڲ�С֩�룬������Ҫ��ץȡ����*/
	 @Component
     static class littleSpider extends Thread{
			private UngetURLQueue unGetUrlQueue;
			private FinishedURLQueue finishedUrlQueue;
			private Spider spider;
			
			public littleSpider(){}
			
			public littleSpider(UngetURLQueue unGetUrlQueue,FinishedURLQueue finishedUrlQueue,Spider spider){
				this.unGetUrlQueue = unGetUrlQueue;
				this.finishedUrlQueue = finishedUrlQueue;
				this.spider = spider;
			}
			
			public void run(){
				System.out.println("thread started");
				try{
					while(true){
						System.out.println("thread "+this.getId()+" is working...");
						String url = unGetUrlQueue.removeURL();
						InputStream store_stream = spider.getStream(url);
						spider.storePage(store_stream,url,Dispatcher.fileNumbers);
						Dispatcher.fileNumbers++;
						
						InputStream get_stream = spider.getStream(url);
						Vector<String> url_storage = new Vector<String>();
						url_storage = spider.getURL(get_stream);
						finishedUrlQueue.insertURL(url);
						Iterator<String> it = url_storage.iterator();
						while(it.hasNext()){
							String temp_url = (String)it.next();
							if(!finishedUrlQueue.isExist(temp_url))
								unGetUrlQueue.insertURL(temp_url);
							else finishedUrlQueue.insertURL(temp_url);
						}
					}
				}catch(Exception e){}
			}
		}

}