package jp.co.infonic.inforails.framework.presentation.control;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;

import jp.co.infonic.common.module.log.APErrorLogger;
import jp.co.infonic.common.module.log.DebugLogger;
import jp.co.infonic.common.util.StringUtil;
import jp.co.infonic.inforails.base.exception.RoutingException;
import jp.co.infonic.inforails.base.property.SystemProperty;
import jp.co.infonic.inforails.framework.presentation.control.process.ApplicationController;
import jp.co.infonic.inforails.framework.presentation.control.process.ActionHandler;
import jp.co.infonic.inforails.framework.presentation.control.process.ProcessHolder;
import jp.co.infonic.inforails.framework.presentation.routing.UrlStructure;
import jp.co.infonic.inforails.framework.presentation.view.RenderHandler;
import jp.co.infonic.inforails.framework.presentation.view.RenderHandlerImpl;

import org.apache.jasper.JasperException;
import org.apache.log4j.Logger;

public class ViewDispatcher {
	
	public static final String CONTROLLER = ":controller";
	public static final String ACTION = ":action";
	public static final String LAYOUT = ":layout";
	public static final String FILE = ":file";
	public static final String CONTENT_TYPE = ":content_type";
	public static final String REDIRECT = ":redirect";
	public static final String URL = ":url";
	public static final String TEXT = ":text";
	
	private Logger logger = DebugLogger.getLogger(this.getClass());
	
	private Map renderOption = new HashMap();
	
	private ServiceInfo info;
	
	private RenderHandler handler;
	
	ViewDispatcher(ServiceInfo info) {
		this.info = info;
		this.handler = new RenderHandlerImpl();
	}
	
	public RenderHandler getRenderHander() {
		return handler;
	}

	public void render(ProcessHolder holder) {
		renderOption = handler.option();
		try {
			if (renderOption.containsKey(REDIRECT)) {
				redirect(holder);
				
			} else
			if (renderOption.containsKey(FILE)) {
				responseFile((File)renderOption.get(FILE));
				
			} else
			if (renderOption.containsKey(TEXT)) {
				responseText();
				
			} else {

				responseJSP(holder);
			}
			
		} catch (ServletException se) {
			se.printStackTrace();
		} catch (IOException e) {
			System.err.println(e);
		}
	}
	
	private void redirect(ProcessHolder holder) throws IOException {

		if (renderOption.containsKey(URL)) {
			logger.debug("[REDIRECT] " + renderOption.get(URL));
			info.response.sendRedirect(renderOption.get(URL).toString());
		} else {
			String url = UrlStructure.struct(prefixPath(), 
					targetController(holder), (String)renderOption.get(ACTION), "");
			logger.debug("[REDIRECT] " + url);
			info.response.sendRedirect(url);
		}
	}
	
	private void responseJSP(ProcessHolder holder) throws ServletException, IOException {

		try {
			String next = new Boolean(false).equals(renderOption.get(LAYOUT))
					? actionJSP(holder.controller(), holder.action())
					: controllerJSP(holder.controller(), holder.action());
			next = "/" + next;
			logger.debug("[RENDER (Jsp)] " + next);
			setRequestScope(holder);
			
			RequestDispatcher dis = info.request.getRequestDispatcher(next);
			dis.forward(info.request, info.response);
			
		}  catch (JasperException jse) {
			APErrorLogger aplogger = APErrorLogger.getLogger();
			aplogger.outputErrorLog(this.getClass().getName(), "responseJSP", "JSPの出力中に例外が発生しました。",
					true, "JSP Error", jse);
			jse.printStackTrace();
			throw jse;
		}
	}
	
	private static int BUFF_LENGTH = 1024;
	
	private void responseFile(File file) throws IOException {
		logger.debug("[RENDER (File)] " + file.getPath());
		setHttpHeader(file);
		
		responseForStream(new FileInputStream(file));
	}
	
	private void responseForStream(InputStream in) throws IOException {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
	        bis =new BufferedInputStream(in);
	        bos = new BufferedOutputStream(info.response.getOutputStream());
	
	        byte[] buff = new byte[BUFF_LENGTH];
	        int sum = 0;
	        int length;
	        while ((length = bis.read(buff)) != -1) {
	            bos.write(buff, 0, length);
	            sum += length;
	        }
	
	        bos.flush();
	        
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	private void setHttpHeader(File file) throws UnsupportedEncodingException {
		if(!renderOption.containsKey(CONTENT_TYPE)) {
			String contentType = info.context.getMimeType(file.getName());
			if (contentType == null || contentType.equals("")) {
				contentType = "application/octet-stream;";
			}
			info.response.setContentType(contentType);
		}
		
		if (renderOption.containsKey("filename")) {
			String encFileName = URLEncoder.encode(file.getName(), "UTF-8");
			
			// InternetExplorerの場合
			if (info.request.getHeader("User-Agent").indexOf("MSIE") != -1) {
				info.response.setHeader("Content-Disposition", "attachment; filename=" + encFileName);
			} else {
				info.response.setHeader("Content-Disposition", "attachment; filename*=" + encFileName);
			}
		}
		
        info.response.setContentLength((int) file.length());
		
	}
	
	private void responseText() throws IOException {
		logger.debug("[RENDER (Text)]" + renderOption.get(TEXT));
		info.response.getWriter().print(renderOption.get(TEXT));
	}
	
	private String prefixPath() {
		return info.request.getContextPath() + info.request.getServletPath();
	}
	
	/**
	 * リクエストスコープのページ情報をセットする。
	 *
	 */
	private void setRequestScope(ProcessHolder holder) {
		info.request.setAttribute("params", info.defaultParams());
		info.request.setAttribute("session", holder.session().getAll());
		info.request.setAttribute("URL_PREFIX", prefixPath());
		for(Object o: handler.requestScope().keySet()) {
			info.request.setAttribute(o.toString(), handler.requestScope().get(o));
		}
	}
	
	private Class targetController(ProcessHolder holder) {
		Class controller = (Class) renderOption.get(CONTROLLER);
		return controller == null ? holder.controller().getClass() : controller;
	}
	
	/**
	 * jsp_directory/layout/コントローラー名 
	 * に対応したJSPファイルを参照する。
	 * コントローラー名に対応したファイルがない場合、
	 * デフォルトでapplication.jspを参照する。
	 * @param controller
	 * @param action
	 * @return
	 */
	private String controllerJSP(ApplicationController controller, ActionHandler action) {
		StringBuffer sb = new StringBuffer(SystemProperty.jspDirectory());
		
		sb.append("layout/");

		if(renderOption.get(CONTROLLER) == null) {
			String conViewName = controller.getViewName();
			sb.append( existLayout(conViewName) ? conViewName : "application");
		}
		sb.append(".jsp");

		info.request.setAttribute("action", "/" + actionJSP(controller, action));
		
		return sb.toString();
	}
	
	/**
	 * jsp_directory/コントローラー/イベント
	 * に対応したJSPファイルを参照する。
	 * @param controller
	 * @param action
	 * @return
	 */
	private String actionJSP(ApplicationController controller, ActionHandler action) {
		StringBuffer sb = new StringBuffer();
		sb.append(controller.getViewName()).append("/");
		
		if (renderOption.containsKey(ACTION)) {
			sb.append(StringUtil.decamelize(renderOption.get(ACTION).toString()));
		} else {
			sb.append(action.getViewName());
		}
		sb.append(".jsp");
		
		if (!existJsp(sb.toString())) {
			throw new RoutingException("Not Found " + action.getViewName());
		}
		
		sb.insert(0, SystemProperty.jspDirectory());
		return sb.toString();
	}
	
	/**
	 * jspディレクトリからの相対パスファイルの有無を返す
	 * @param file
	 * @return
	 */
	private boolean existJsp(String file) {
		try {
			URL url = info.context.getResource("/" + SystemProperty.jspDirectory() + file);
			return url != null;
		} catch (MalformedURLException e) {
			logger.error(e);
		}
		return false;
	}
	
	private boolean existLayout(String name) {
		return existJsp("layout/" + name + ".jsp");
	}
}
