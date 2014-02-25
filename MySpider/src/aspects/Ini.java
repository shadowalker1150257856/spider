package aspects;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@Component
public final class Ini {
	
	/*����ģʽ*/
	private Ini(){}
	
	/*�����ļ�Ŀ¼*/
	private static String iniFile = "ini.xml";
	
	/**/
	private static Ini ini = new Ini();
	
	/**/
	public static Ini getInstance(){
		return ini;
	}
	
	/*��ȡDOM-xmldoc*/
	private static Document getXmlDoc(){
		Document xmldoc = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringElementContentWhitespace(true);
		try{
			DocumentBuilder db = factory.newDocumentBuilder();		
			xmldoc = db.parse(new File(iniFile));
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return xmldoc;
	}
	
	/*��ȡ���ڵ�-root*/
	private static Element getRoot(Document xmldoc){
		return xmldoc.getDocumentElement();
	}
	
	/*��ȡ��ʼ�ĵ����
	 * file_start�ڵ�ֵ*/
	public static int getFileStart(){
		Document xmldoc = getXmlDoc();
		Element root = getRoot(xmldoc);
		Node file_start = getSingleNode("/ini/file_start",root);
		return Integer.parseInt(file_start.getTextContent());
	}
	
	/*������ʼ�ļ���*/
	public synchronized static void setFileStart(String file_start){
		Document xmldoc = getXmlDoc();
		Element root = getRoot(xmldoc);
		Node fileStart = getSingleNode("/ini/file_start",root);
		fileStart.setTextContent(file_start);
		saveXML("ini.xml",xmldoc);
	}
	
	/*��ȡ�������߳���
	 * thread_number�ڵ�ֵ*/
	public static int getThreadNumbers(){
		Document xmldoc = getXmlDoc();
		Element root = getRoot(xmldoc);
		Node thread_numbers = getSingleNode("/ini/thread_numbers",root);
		return Integer.parseInt(thread_numbers.getTextContent());
	}
	
	/*��ȡ��ʼ��URL��ַ
	 * start_urls�Ľڵ㼯*/
	public static NodeList getStartUrls(){
		Document xmldoc = getXmlDoc();
		Element root = getRoot(xmldoc);
		NodeList start_urls = getNodes("/ini/start_urls/url",root);
		return start_urls;
	}
	
	/*��ȡurl�ڵ�ֵ*/
	public static String getUrl(NodeList urls,int index){
		String url = urls.item(index).getTextContent();
		return url;
	}
	
	/*��ȡ�����ڵ�*/
	private static Node getSingleNode(String expression,Object source){
		Node result = null;
		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		try {
			result = (Node) xpath.evaluate(expression, source,XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	/*��ȡδץȡURL����
	 * unget_urls�ڵ㼯*/
	public static NodeList getUngetUrls(){
		Document xmldoc = getXmlDoc();
		Element root = getRoot(xmldoc);
		NodeList unget_urls = getNodes("/ini/unget_urls/url",root);
		return unget_urls;
	}
	
	/*��ȡ��ץȡURL����*/
	public static NodeList getFinUrls(){
		Document xmldoc = getXmlDoc();
		Element root = getRoot(xmldoc);
		NodeList fin_urls = getNodes("/ini/finished_urls/url",root);
		return fin_urls;
	}
	
	/*ɾ���ڵ�*/
	private synchronized static void removeNodes(NodeList nodes){
		int length = nodes.getLength();
		int index;
		for(index = 0;index<length;index++){
			nodes.item(index).getParentNode().removeChild(nodes.item(index));
		}
	}
	
	/*�������*/
	public synchronized static void clear(){
		Document xmldoc = getXmlDoc();
		Element root = getRoot(xmldoc);
		removeNodes(getNodes("/ini/unget_urls/url",root));
		removeNodes(getNodes("/ini/finished_urls/url",root));
		saveXML("ini.xml",xmldoc);
	}
	
	/*���δץȡURL*/
	public synchronized static void addUngetUrl(String url){
		Document xmldoc = getXmlDoc();
		Element root = getRoot(xmldoc);
		Element unget_urls = (Element)getSingleNode("/ini/unget_urls",root);
		Element urlTag = xmldoc.createElement("url");
		urlTag.setTextContent(url);
		unget_urls.appendChild(urlTag);
		saveXML("ini.xml",xmldoc);
	}
	
	/*�����ץȡURL*/
	public synchronized static void addFinUrl(String url){
		Document xmldoc = getXmlDoc();
		Element root = getRoot(xmldoc);
		Element fin_urls = (Element)getSingleNode("/ini/finished_urls",root);
		Element urlTag = xmldoc.createElement("url");
		urlTag.setTextContent(url);
		fin_urls.appendChild(urlTag);
		saveXML("ini.xml",xmldoc);
	}
	
	/*���������ļ�*/
	private synchronized static void saveXML(String fileName,Document doc){
		TransformerFactory transFactory = TransformerFactory.newInstance();
		try {
			Transformer transformer = transFactory.newTransformer();
			
			DOMSource source = new DOMSource();
			source.setNode(doc);
			StreamResult result = new StreamResult();
			result.setOutputStream(new FileOutputStream(fileName));
			transformer.transform(source, result);
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}
	
	/*��ȡ�ڵ㼯*/
	private static NodeList getNodes(String expression,Object source){
		NodeList result = null;
		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		try {
			result = (NodeList) xpath.evaluate(expression, source, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
}
