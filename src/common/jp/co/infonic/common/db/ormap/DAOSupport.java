package jp.co.infonic.common.db.ormap;

public interface DAOSupport<T extends Model> {

	public T find();
	
	public T[] findAll();
	
	public int count();
	
	public String max();
}
