package spider;

import java.util.*;

import org.springframework.stereotype.Component;

@Component
public class UngetURLQueue{
	private Vector<String> queue = new Vector<String>();
	
	public Vector<String> getQueue(){
		return queue;
	}
	
	/*向队列中插入URL*/
	public synchronized void insertURL(String url) throws Exception{
		boolean result = queue.add(url);
		if(!result)
			throw new Exception("[fail to add URL: "+url+"]:"+new Date());
	}
	
	/*返回URL*/
	public synchronized String removeURL() throws Exception{
		boolean result = queue.isEmpty();
		if(!result){
			try{
				String url = queue.remove(0);
				return url;
			}catch(ArrayIndexOutOfBoundsException e){
				throw new Exception("[unget url queue is out of bounds]:"+new Date());
			}
		}
		throw new Exception("[unget url queue is empty]:"+new Date());
	}
	
	/*判断队列是否为空*/
	public synchronized boolean isEmpty(){
		if(queue.isEmpty()) return true;
		else return false;
	}
}
