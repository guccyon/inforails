package jp.co.infonic.inforails.base.property;

import java.util.HashMap;
import java.util.Map;

import jp.co.infonic.common.util.Assertion;

public class FactoryManager {

	public static FactoryManager i = new FactoryManager();
	
	private FactoryManager(){
		this.factories = new HashMap<String, IFactory>();
	}
	
	private Map<String, IFactory> factories;
	
	public IFactory getFactory(String id) {
		Assertion.isNotNull(id, "factory id is null");
		return factories.get(id);
	}
	
	public void addFactory(IFactory fact) {
		factories.put(fact.getId(), fact);
	}
}
