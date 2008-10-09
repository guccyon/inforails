package jp.co.infonic.common.mail;

public class MailFactory {

	public static Mail createNewMail(String fromAddress, String subject) {
		Mail mail = new Mail(fromAddress, subject);
		return mail;
	}
	
	public static MailDestination createDestTo(String toAddress, String name) {
		MailDestination dest = new MailDestination(toAddress, name, MailDestination.ADD_TYPE_TO);
		return dest;
	}
	
	public static MailDestination createDestTo(String toAddress) {
		MailDestination dest = new MailDestination(toAddress, null, MailDestination.ADD_TYPE_TO);
		return dest;
	}
	
	public static MailDestination createDestCC(String ccAddress, String name) {
		MailDestination dest = new MailDestination(ccAddress, name, MailDestination.ADD_TYPE_CC);
		return dest;
	}
	
	public static MailDestination createDestCC(String ccAddress) {
		MailDestination dest = new MailDestination(ccAddress, null, MailDestination.ADD_TYPE_CC);
		return dest;
	}
	
	public static MailDestination createDestBCC(String ccAddress, String name) {
		MailDestination dest = new MailDestination(ccAddress, name, MailDestination.ADD_TYPE_BCC);
		return dest;
	}
	
	public static MailDestination createDestBCC(String ccAddress) {
		MailDestination dest = new MailDestination(ccAddress, null, MailDestination.ADD_TYPE_BCC);
		return dest;
	}
}
