package jp.co.infonic.inforails.framework.presentation.routing;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletContext;

import jp.co.infonic.common.module.log.DebugLogger;

import org.apache.log4j.Logger;

public class ContextContentsResolver {
	
	private static URL contextRoot;
	
	private static String contextPath;
	
	private static Logger logger = DebugLogger.getLogger(ContextContentsResolver.class);

	public static void initialize(ServletContext context) {
		DebugLogger.getLogger(ContextContentsResolver.class).info("ContextContentsResolver initialize");
		if (!isInit()) {
			try {
				contextRoot = context.getResource("/");
				logger.debug("ContextRoot -> " + contextRoot);
				
				contextPath = "/" + context.getServletContextName();
				logger.debug("ContextPath -> " + contextPath);
			} catch (MalformedURLException e) {
				throw new RuntimeException("Invalid URL", e);
			}
		}
	}
	
	private static boolean isInit() {
		return contextRoot != null;
	}
	
	public static URL getContentsUrl(String resource) throws IOException {
		return new URL(contextRoot.toExternalForm() + resource);
	}
	
	public static boolean isExist(String resource) {
		try {
			getContentsUrl(resource).getContent();
			return true;
		} catch (IOException e) {
			logger.debug("can't find " + resource + " " + e.getMessage());
			return false;
		}
	}
	
	public static String getCollectPath(String contentPath) {
		if (!contentPath.startsWith("/")) {
			contentPath = "/" + contentPath;
		}
		return contextPath + contentPath;
	}
}
