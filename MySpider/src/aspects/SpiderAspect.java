package aspects;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SpiderAspect extends SystemAspects{
	
	private synchronized void writeLog(String message){
		String path = "Log.lg";
		try {
			FileOutputStream outputStream = new FileOutputStream(path,true);
			OutputStreamWriter outputWriter = new OutputStreamWriter(outputStream,"utf-8");
			Writer output = new BufferedWriter(outputWriter);
			output.write(message+"\n");
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	@Pointcut("execution(public * spider.Spider.*(..))")
	public void logPointCut(){}
	
	@After("logPointCut() && args(stream,url,fileName)")
	public void addLog(InputStream stream,String url,int fileName){
		writeLog("[success to link:] "+url+" "+new Date());
		writeLog("[a page named "+fileName+" has stored] "+new Date());
	}
}
