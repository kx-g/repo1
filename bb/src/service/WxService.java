package service;

import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;
import com.thoughtworks.xstream.XStream;

import entity.Article;
import entity.BaseMessage;
import entity.ImageMessage;
import entity.MusicMessage;
import entity.NewsMessage;
import entity.TextMessage;
import entity.VideoMessage;
import entity.VoiceMessage;
import net.sf.json.JSONObject;
import util.Util;

public class WxService {
	private static final String  TOKEN = "zht";
//	private static final String TOKEN = "llzs";
	private static final String APPKEY="1fec136dbd19f44743803f89bd55ca62";
	/**
		 * ��֤ǩ�� 
		 * @param signature
		 * @param timestamp
		 * @param nonce
		 * @return
		 */
	public static boolean check(String signature, String timestamp, String nonce) {
		
		 //  1����token��timestamp��nonce�������������ֵ�������
			String[] strs=new String[] {TOKEN,timestamp,nonce};
			Arrays.sort(strs);
		 //  2�������������ַ���ƴ�ӳ�һ���ַ�������sha1���� 
		String a=strs[0]+strs[1]+strs[2];
		System.out.println("a="+a);
		String mysig=sha1(a);
		System.out.println(mysig+"----");
		System.out.println("��������");
		System.out.println(signature+"----");
		//  3�������߻�ü��ܺ���ַ�������signature�Աȣ� 
		  
		//equals��֤���ع����ĺʹ�������signature�Ƿ��Ӧ
		return mysig.equalsIgnoreCase(signature);
	}
	/**
	 * ����sha1����
	 * @param a
	 * @return
	 */
	private static String sha1(String src) {
		try {
			//��ȡһ�����ܶ���
			MessageDigest md = MessageDigest.getInstance("sha1");
			//����
			byte[] digest = md.digest(src.getBytes());
			char[] chars= {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
			StringBuilder sb = new StringBuilder();
			//������ܽ��
			for(byte b:digest) {
				sb.append(chars[(b>>4)&15]);
				sb.append(chars[b&15]);
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

//		private static String sha1(String src) {
//			try {
//				//��ȡ���ܶ���
//				MessageDigest md=MessageDigest.getInstance("sha1");
//				//sha1����
//				System.out.println(md);
//				byte[] digest = md.digest(src.getBytes());
//				char[] chars= {'0','1','2','3','4','5','6','7','8','a','b','c','d','e','f'};
//				StringBuilder sb=new StringBuilder();
//				System.out.println("sb");
//				//������ܽ��
//				 
//				for(byte b:digest) {
//					sb.append(chars[(b>>4)&15]);
//					sb.append(chars[b&15]); 
//				} 
//				return sb.toString();
//			} catch (NoSuchAlgorithmException e) { 
//				e.printStackTrace();
//			}
//			return null;
//		}
	
	/**
	 * ΢�źŷ���������һ��XML���ݰ�  �����ǽ���   
	 * @param is
	 * @return
	 */
	public static Map<String, String> parseRequest(InputStream is) {
			Map<String ,String > map=new HashMap<>(); 
		SAXReader reader = new SAXReader();
		try {
			//��ȡ��������ȡ�ĵ�����
			Document document = reader.read(is);
			//�����ĵ������ȡ���ڵ�
			Element root = document.getRootElement();
			//��ȡ���ڵ�Ե������ӽڵ�
			List<Element> elements = root.elements();
			for(Element e:elements) {
				map.put(e.getName(), e.getStringValue());
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//���ش���ļ�ֵ�Ե�map
		return map;

	}
	/**
	 * ���ڴ�������ʱ��Ļظ���Ϣ����Ϣ
	 * @param requestMap
	 * @return���ص���XML���ݰ�
	 * �ź���qq 291301290
	 */
	public static String getResponse(Map<String, String> requestMap) {
		BaseMessage msg=null;
		String msgType=requestMap.get("MsgType");
		System.out.println("����="+msgType);
		switch(msgType) {
		//�����ı���Ϣ
		case "text":
			msg=dealTextMessage(requestMap);
			break;
		case "image":
			
			break;
		case "voice":
			
			break;
		case "video":
			
			break;
		case "shortvideo":
			
			break;
		
		
		} 
		//����Ϣ������ΪXML���ݰ�
	if (msg!=null) {
	return 	beanToXML(msg);
	}
	return null;
}
	//����Ϣ������ΪXML���ݰ�
	private static String beanToXML(BaseMessage msg) {
		XStream stream=new XStream();
		// ������Ҫ����XStreamAlias("xml")ע�͵���
		stream.processAnnotations(TextMessage.class);
		stream.processAnnotations(ImageMessage.class);
		stream.processAnnotations(MusicMessage.class);
		stream.processAnnotations(NewsMessage.class);
		stream.processAnnotations(VideoMessage.class);
		stream.processAnnotations(VoiceMessage.class);
		String xml=stream.toXML(msg);
		return xml;
	}
	/**
	 * �����ı���Ϣ
	 * @param requestMap 
	 * @return
	 */
	private static BaseMessage dealTextMessage(Map<String, String> requestMap) {
		//�û�����������
		String msg=requestMap.get("Content");
		 if (msg.equals("��С��")) {
			 List<Article> article=new ArrayList<>();
			 article.add(new Article("��С��", "���������벻�ɸ��˵�...", "http://mmbiz.qpic.cn/mmbiz_jpg/5feuRibwof7URZiaKP2XIOzdUCmP8t95ZL2wibZdfE6yUsPAJXf1Njojo7UTCdvy7SXH6ETSqa0rjoQyBa8nxJzibA/0", "www.baidu.com"));
			 NewsMessage nm=new NewsMessage(requestMap, article);
			 return nm;
					
		} 
		
		
		
		
		//���÷����������������
		String resp=chat(msg);
		TextMessage tm=new TextMessage(requestMap,resp);
		return tm;
	}
	/**
	 * ����ͼ�����������
	 * @param msg
	 * @return
	 */
	private static String chat(String msg) {
		
		        String result =null;
		        String url ="http://op.juhe.cn/robot/index";//����ӿڵ�ַ
		        Map params = new HashMap();//�������
		            params.put("key",APPKEY);//�����뵽�ı��ӿ�ר�õ�APPKEY
		            params.put("info",msg);//Ҫ���͸������˵����ݣ���Ҫ����30���ַ�
		            params.put("dtype","");//���ص����ݵĸ�ʽ��json��xml��Ĭ��Ϊjson
		            params.put("loc","");//�ص㣬�籱���йش�
		            params.put("lon","");//���ȣ�����116.234632��С�������6λ������ҪдΪ116234632
		            params.put("lat","");//γ�ȣ���γ40.234632��С�������6λ������ҪдΪ40234632
		            params.put("userid","");//1~32λ����userid������Լ���ÿһ���û������������ĵĹ���
		 
		        try {
		            result =Util.net(url, params, "GET");
		           //����Json
		            JSONObject  jsonObject= JSONObject.fromObject(result);
		            //ȡ��error_code
		            int code = jsonObject.getInt("error_code");
		            if (code!=0) {
						return null;
					}
		            //ȥ��Ҫ���ص���Ϣ
		            String resp = jsonObject.getJSONObject("result").getString("text");
		            return resp;
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		
		        return null;
	}
	
	
	
	
	
	
	
	

}
