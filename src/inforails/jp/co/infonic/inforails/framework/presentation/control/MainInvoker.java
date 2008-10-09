package jp.co.infonic.inforails.framework.presentation.control;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;

import jp.co.infonic.common.module.log.DebugLogger;
import jp.co.infonic.inforails.base.property.SystemProperty;
import jp.co.infonic.inforails.framework.presentation.control.process.ProcessHolder;
import jp.co.infonic.inforails.framework.presentation.fileupload.MultipartRequestParser;
import jp.co.infonic.inforails.framework.presentation.routing.ProcessMapper;
import jp.co.infonic.inforails.framework.session.InfoRailsSession;
import jp.co.infonic.inforails.framework.session.InfoRailsSessionFactory;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

public abstract class MainInvoker {
	
	private Logger logger = DebugLogger.getLogger(this.getClass());

	protected MainInvoker() {
	}

	public void invoke(ServiceInfo info, ViewDispatcher dispatcher) {

		try {
			ProcessHolder holder = new ProcessHolder();
			
			holder.setController(ProcessMapper.i.getController(info.controller()).newInstance());
			
			holder.setAction(ProcessMapper.i.getAction(info.controller(), info.action()).newInstance());

			info.request.setCharacterEncoding(holder.action().requestEncode());
			
			parseParamters(info, holder);
			
			startSession(info, holder);

			logger.debug("èàóùäJénÅFcontroller: " + info.controller() + "   action: " + info.action());

			invoke(holder, dispatcher);

		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private void startSession(ServiceInfo info, ProcessHolder holder) {
		InfoRailsSession session = InfoRailsSessionFactory.create(
				info.request.getSession(), holder.controller().getClass());
		
		holder.setSession(session);
	}

	private Parameter parseParamters(ServiceInfo info,	ProcessHolder holder)
			throws Exception {

		Parameter params = new Parameter();
		if (ServletFileUpload.isMultipartContent(info.request)) {
			MultipartRequestParser parser = new MultipartRequestParser();
			params.setAll( parser.parse(info.request, SystemProperty.defaultCharset()) );
		} else {
			Enumeration enume = info.request.getParameterNames();
			while (enume.hasMoreElements()) {
				String key = enume.nextElement().toString();
				params.set(key, info.request.getParameter(key));
			}
		}

		params.setAll(info.defaultParams());
		
		holder.setParameter(params);
		
		params.set("#HTTP.SERVLET.REQUEST#", info.request);

		info.margeParam(params.getAll());
		return params;
	}

	abstract protected void invoke(ProcessHolder holder,	ViewDispatcher dispatcher);
}
