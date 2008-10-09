package jp.co.infonic.inforails.framework.presentation.control;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.infonic.common.util.StringUtil;
import jp.co.infonic.inforails.base.property.FactoryManager;
import jp.co.infonic.inforails.framework.presentation.control.process.ApplicationController;
import jp.co.infonic.inforails.framework.presentation.routing.RouteParam;
import jp.co.infonic.inforails.framework.presentation.routing.UrlParser;

public class ServiceProvider {

	public static void service(ServletContext context, HttpServletRequest request, HttpServletResponse response) {
		ServiceInfo info = new ServiceInfo();
		info.request = request;
		info.response = response;
		
		new ServiceProvider(info).service(context);
	}
	
	private ServiceInfo info;
	
	ServiceProvider(ServiceInfo info) {
		this.info = info;
		init();
	}
	
	private void init() {
		String path = info.request.getPathInfo() == null ? "" : info.request.getPathInfo();
		RouteParam r = UrlParser.lookup(path);
		setServiceInfo( info, r, path);
	}
	
	private void service(ServletContext context) {
		addOptionalInfo(info, context);
		
		ViewDispatcher dispatcher = new ViewDispatcher(info);
		
		InvokerFactory fact = (InvokerFactory)FactoryManager.i.getFactory(InvokerFactory.FACTORY_ID);
		MainInvoker invoker = fact.getInvoker(info);
		invoker.invoke(info, dispatcher);
	}
	
	// set service information
	private void setServiceInfo(ServiceInfo info, RouteParam r, String requestPath) {
		Map<String, String> param = new UrlParser(r, requestPath).parameter();
		
		if (r.controller() != null) {
			info.setController(ApplicationController.getId(r.controller()));
		} else if (param.containsKey(ServiceInfo.CONTROLLER_ID)) {
			info.setController(
				capitalize(StringUtil.camelize(param.get(ServiceInfo.CONTROLLER_ID)))
			);
			param.remove(ServiceInfo.CONTROLLER_ID);
		}

		if (r.action() != null) {
			info.setAction(r.action());
		} else if (param.containsKey(ServiceInfo.ACTION_ID)) {
			info.setAction(
				capitalize(StringUtil.camelize(param.get(ServiceInfo.ACTION_ID)))
			);
			param.remove(ServiceInfo.ACTION_ID);
		} else {
			info.setAction("Index");
		}
		
		info.margeParam(param);
	}
	
	private String capitalize(String str) {
		return (str.charAt(0) + "").toUpperCase() + str.substring(1);
	}
	
	private void addOptionalInfo(ServiceInfo info, ServletContext context) {
		info.setContextRoot(context.getRealPath(""));
		info.context = context;
	}
}
