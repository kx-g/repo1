package service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
@WebServlet("/wx")
public class WxServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
     
 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		signature	΢�ż���ǩ����signature����˿�������д��token�����������е�timestamp������nonce������
//		timestamp	ʱ���
//		nonce	�����
//		echostr	����ַ���
		System.out.println("get");
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");
		System.out.println(signature);
		System.out.println(timestamp);
		System.out.println(nonce);
		System.out.println(echostr);
//       У��ǩ��
		if (WxService.check(signature,timestamp,nonce)) {
			System.out.println("����ɹ�");
			PrintWriter out= response.getWriter();
			//ԭ������echostr����
			out.print(echostr);
			out.flush();
			out.close();
		}else {
			
			System.out.println("����ʧ��");
		}
		
		
		
	}
 
			/**
			 * ������Ϣ  ���¼�����
			 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf8");
		response.setCharacterEncoding("utf8");
		System.out.println("post");
		//������Ϣ���¼�����
	 Map<String,String> requestMap=	WxService.parseRequest(request.getInputStream());
		//��ӡ����������Map
		System.out.println(requestMap);
//		String huifu="<xml><ToUserName><![CDATA["+requestMap.get("FromUserName")+"]]></ToUserName><FromUserName><![CDATA[fromUser]]></FromUserName><CreateTime>"+System.currentTimeMillis()/1000+"</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[����ѽ]]></Content></xml>";
//		String huifu="<xml>\r\n" + 
//				"<ToUserName><![CDATA["+requestMap.get("FromUserName")+"]]></ToUserName>\r\n" + 
//				"<FromUserName><![CDATA["+requestMap.get("ToUserName")+"]]></FromUserName>\r\n" + 
//				"<CreateTime>"+System.currentTimeMillis()/1000+"</CreateTime>\r\n" + 
//				"<MsgType><![CDATA[text]]></MsgType>\r\n" + 
//				"<Content><![CDATA[�ǰ��ǰ�]]></Content>\r\n" + 
//				"</xml>";
		//�����һ���ظ�����Ϊ  δ��һ������  
		//�ڶ����ظ��ɹ���  ������ô��̫�鷳
		//�����Ǽ򵥵�
		String huifu= WxService.getResponse(requestMap);
		System.out.println(huifu);
		PrintWriter out = response.getWriter();
		out.print(huifu);
		out.flush();
		out.close();
		
		
		
		
		
		
		
	}

}
