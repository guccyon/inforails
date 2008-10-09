package jp.co.infonic.common.module.ex;

public class ExObject implements IObject {

	public boolean isBlank() {
		return !isExist();
	}
	
	public boolean isExist() {
		return true;
	}
}
