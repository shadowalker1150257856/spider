package aspects;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
/*
 * 系统级ASPECT声明
 * 抽象类，供后面各具体ASPECT继承
 * 主要分为三类：分别为日志，初始化，错误
 * */
public abstract class SystemAspects {
	
	/*日志Pointcut Expression
	 * 在spider包下的所有类为目标*/
	public static final String logPC = "within(spider.*)";
	
	/*错误Pointcut Expression
	 *在spider包下的所有类为目标 */
	public static final String exceptionPC = "execution(public * spider.*.*(..))";
	
	/*日志切点*/
	@Pointcut(logPC)
	public void logPointCut(){}
	
	/*错误切点*/
	@Pointcut(exceptionPC)
	public void exceptionPointCut(){}
}
