package spider;

import java.util.*;

import org.springframework.stereotype.Component;

@Component
public class FinishedURLQueue{
	private Vector<String> finishedURLQueue = new Vector<String>();
	
	/*����URL*/
	public synchronized String removeURL(){
		boolean result = finishedURLQueue.isEmpty();
		if(!result){
			try{
				String url = finishedURLQueue.remove(0);
				return url;
			}catch(ArrayIndexOutOfBoundsException e){
				return null;
			}
		}
		return null;
	}	
	
	/*����URL*/
	public synchronized void insertURL(String url) throws Exception{
		boolean result = finishedURLQueue.add(url);
		if(!result)
			throw new Exception("[fail to insert URL:"+url+"]:"+new Date());
	}
	
	/*�ж��Ƿ�Ϊ��*/
	public synchronized boolean isEmpty() throws Exception{
		throw new Exception("[finished url queue is empty]:"+new Date());
	}
	
	/*�ж�ָ��URL�Ƿ����*/
	public synchronized boolean isExist(String url){
		int index = finishedURLQueue.lastIndexOf(url);
		if(index == -1) return false;
		else return true;
	}
}
