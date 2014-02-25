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
/*URL���е�������
 * ��Ҫ���ܣ��Է����URL��ʽ������֤
 * ���׳��Ĵ�����м�¼��д���Ӧ�Ĵ�����־
 * �Դ����URL������֤*/
public class ExceptionAspect extends SystemAspects{
	
/*	�����*/
	@Override
	@Pointcut("execution(public * spider.*.*(..))")
	public void exceptionPointCut(){}
	
	/*������־��������
	 * ���������־��Ϣ�����д��resourceĿ¼�µ�Exception.ex*/
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
	
	/*����׳��쳣
	 * ���쳣��¼����־��*/
	@AfterThrowing(pointcut="exceptionPointCut()",throwing="ex")
	public void insertURLDeal(Exception ex){
		writeException(ex.getMessage());
	}
}
