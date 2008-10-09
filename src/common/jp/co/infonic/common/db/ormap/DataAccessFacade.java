package jp.co.infonic.common.db.ormap;


public class DataAccessFacade<T extends Model> implements DAOSupport<Model>{
	
	private Class<? extends T> modelClass;
	
	private String[] orders;
	
	private Integer limit;
	
	private Integer offset;
	
	private String joins;
	
	DataAccessFacade(Class<? extends T> c) {
		this.modelClass = c;
	}

	public T find() {
		T model = new ModelCreator<T>(modelClass).create();
		return model;
	}

	public T[] findAll() {
		return null;
	}
	
	public T findById(String id) {
		return null;
	}
	
	public int count() {
		return 0;
	}
	
	public String max() {
		return "";
	}
	
	public DataAccessFacade setJoins(String join) {
		return this;
	}
	
	public DataAccessFacade setOffset(int i) {
		offset = i;
		return this;
		
	}
	
	public DataAccessFacade setLimit(int i) {
		limit = i;
		return this;
		
	}
	
	public DataAccessFacade setOrder(String s) {
		this.orders = new String[]{s};
		return this;
	}
	
	public DataAccessFacade setOrder(String ... orders)  {
		this.orders = orders;
		return this;
	}
	
	public void clear() {
		limit = null;
		offset = null;
		joins = null;
		orders = null;
	}
}
