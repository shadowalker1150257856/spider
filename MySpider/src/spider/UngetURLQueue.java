package spider;

import java.util.*;

import org.springframework.stereotype.Component;

@Component
public class UngetURLQueue{
	private Vector<String> queue = new Vector<String>();
	
	public Vector<String> getQueue(){
		return queue;
	}
	
	/*������в���URL*/
	public synchronized void insertURL(String url) throws Exception{
		boolean result = queue.add(url);
		if(!result)
			throw new Exception("[fail to add URL: "+url+"]:"+new Date());
	}
	
	/*����URL*/
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
	
	/*�ж϶����Ƿ�Ϊ��*/
	public synchronized boolean isEmpty(){
		if(queue.isEmpty()) return true;
		else return false;
	}
}
