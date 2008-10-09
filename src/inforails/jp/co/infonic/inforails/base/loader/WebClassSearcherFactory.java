package jp.co.infonic.inforails.base.loader;

public class WebClassSearcherFactory implements ClassSearcherFactory {
	
	public static final String FACTORY_ID = "WEB_CLASS_SEARCHER";

	public String getId() {
		return FACTORY_ID;
	}
	
	public ClassSearcher getClassSearcher() {
		return new WebClassSearcher();
	}

}
