package aspects;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
/*
 * ϵͳ��ASPECT����
 * �����࣬�����������ASPECT�̳�
 * ��Ҫ��Ϊ���ࣺ�ֱ�Ϊ��־����ʼ��������
 * */
public abstract class SystemAspects {
	
	/*��־Pointcut Expression
	 * ��spider���µ�������ΪĿ��*/
	public static final String logPC = "within(spider.*)";
	
	/*����Pointcut Expression
	 *��spider���µ�������ΪĿ�� */
	public static final String exceptionPC = "execution(public * spider.*.*(..))";
	
	/*��־�е�*/
	@Pointcut(logPC)
	public void logPointCut(){}
	
	/*�����е�*/
	@Pointcut(exceptionPC)
	public void exceptionPointCut(){}
}
