package jp.co.infonic.common.db.ormap;

public class AbstractModel implements Model {
	
	protected int count = 0;
	
	public boolean save() {
		return false;
	}
	
	public boolean delete() {
		return false;
	}
}
