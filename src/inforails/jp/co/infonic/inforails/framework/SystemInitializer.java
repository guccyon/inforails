package jp.co.infonic.inforails.framework;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import jp.co.infonic.common.db.jdbc.DataSourceManager;
import jp.co.infonic.common.db.jdbc.properties.GenericProperties;
import jp.co.infonic.inforails.base.loader.WebClassSearcherFactory;
import jp.co.infonic.inforails.base.property.FactoryManager;
import jp.co.infonic.inforails.base.property.SystemProperty;
import jp.co.infonic.inforails.framework.presentation.control.InvokerFactory;
import jp.co.infonic.inforails.framework.presentation.routing.ContextContentsResolver;
import jp.co.infonic.inforails.framework.presentation.routing.ProcessMapper;
import jp.co.infonic.inforails.framework.presentation.routing.RouteParam;
import jp.co.infonic.inforails.framework.presentation.routing.UrlParser;

public class SystemInitializer implements ServletContextListener {

	public void contextInitialized(ServletContextEvent context) {
		
		System.out.println("InfoRails init start");
		
		try {
			System.out.println("InfoRails Factory initialize");
			FactoryManager.i.addFactory(new WebClassSearcherFactory());
			FactoryManager.i.addFactory(new InvokerFactory());
			
			System.out.println("InfoRails load property");
			SystemProperty.readProperty();

			System.out.println("InfoRails database init");
			databaseInit();
			
			System.out.println("InfoRails ContextContents init");
			ContextContentsResolver.initialize(context.getServletContext());
			
			System.out.println("InfoRails Controller èâä˙âª");
			ProcessMapper.i.initAll();
			
			System.out.println("InfoRails URL append Route");
			UrlParser.appendTable(new RouteParam("#{controller}/#{action}/#{id}"));
			UrlParser.appendTable(new RouteParam("#{controller}/#{action}"));
			UrlParser.appendTable(new RouteParam("#{controller}"));
			
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e);
			throw new RuntimeException(e);
		} catch (Throwable th) {
			th.printStackTrace();
			System.err.println(th);
			throw new RuntimeException(th);
		}

		System.out.println("InfoRails init end");
	}
	
	private void databaseInit() {
		DataSourceManager.setProperties(new GenericProperties(
				"org.h2.Driver",
				"jdbc:h2:file:" + SystemProperty.dataStore(),
				SystemProperty.getSystemProperty("DATABASE.USER"),
				SystemProperty.getSystemProperty("DATABASE.PASSWORD"))
		);
	}

	public void contextDestroyed(ServletContextEvent arg0) {
		SystemProperty.reloadCancel();
	}

}
