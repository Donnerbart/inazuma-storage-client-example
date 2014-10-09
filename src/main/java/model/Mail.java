package model;

public abstract class Mail
{
	private final MailType mailType;
	private final int senderID;
	private final int receiverID;

	protected Mail(final MailType mailType, final int senderID, final int receiverID)
	{
		this.mailType = mailType;
		this.senderID = senderID;
		this.receiverID = receiverID;
	}

	public MailType getMailType()
	{
		return mailType;
	}

	public int getSenderID()
	{
		return senderID;
	}

	public int getReceiverID()
	{
		return receiverID;
	}
}
