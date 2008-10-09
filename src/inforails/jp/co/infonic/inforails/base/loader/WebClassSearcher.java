package jp.co.infonic.inforails.base.loader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedList;
import java.util.List;

public class WebClassSearcher extends ClassSearcher {
	
	@Override
	protected  List<ClassPath> getClassPathObjects() throws IOException {
		//List<ClassPath> result = super.getClassPathObjects();
		List<ClassPath> result = new LinkedList<ClassPath>();
		logger.debug("WEBClassSearcher classPath search start");
		
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		if (loader instanceof URLClassLoader) {
			URL[] urls = ((URLClassLoader)loader).getURLs();
			for (URL url: urls) {
				logger.debug("WEBClassSeracher " + url);
				result.add(new ClassPath(url.getPath()));
			}
		} else {
			//URL thisClass = loader.getResource("jp.co.infonic.inforails.base.loader.WebClassSearcher" );
			URL thisClass = loader.getResource("application.properties" );
			if( thisClass != null ) {
				// WEB-INF/classes
				String classes = thisClass.getPath().replaceFirst("/$", "");
				URL url = new URL(thisClass.getProtocol(), thisClass.getHost(), classes);
				logger.debug("WEBClassSeracher " + url);
				result.add(new ClassPath(new File(url.getPath()).getParent()));
			}
		}
		
		return result;
	}
	
}
