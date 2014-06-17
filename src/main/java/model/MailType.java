package model;

public enum MailType
{
	DEFAULT(Mail.class),
	USER(MailUser.class),
	TRADE(MailTrade.class);
	
	final Class<?> type;
	
	MailType(Class<?> type)
	{
		this.type = type;
	}
	
	Class<?> getType()
	{
		return type;
	}
}
