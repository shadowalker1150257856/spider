package aspects;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
/*URL队列的切面类
 * 主要功能：对放入的URL格式进行认证
 * 对抛出的错误进行记录，写入对应的错误日志
 * 对传入的URL进行验证*/
public class ExceptionAspect extends SystemAspects{
	
/*	切入点*/
	@Override
	@Pointcut("execution(public * spider.*.*(..))")
	public void exceptionPointCut(){}
	
	/*错误日志操作方法
	 * 传入错误日志信息，随后写入resource目录下的Exception.ex*/
	private synchronized void writeException(String message){
		String path = "Exception.ex";
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
	
	/*如果抛出异常
	 * 则将异常记录进日志中*/
	@AfterThrowing(pointcut="exceptionPointCut()",throwing="ex")
	public void insertURLDeal(Exception ex){
		writeException(ex.getMessage());
	}
}
