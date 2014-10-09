package worker;

import com.google.gson.Gson;
import de.donnerbart.inazuma.storage.base.request.RequestController;
import model.Mail;
import model.MailTrade;
import model.MailType;
import model.MailUser;
import util.Config;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CreateRandomMails implements Runnable
{
	private static final Gson gson = new Gson();
	private static final MailType[] mailTypes = MailType.values();
	private static final Random generator = new Random();

	private final RequestController requestController = RequestController.getInstance();

	private final ScheduledExecutorService threadPool;

	public CreateRandomMails(final ScheduledExecutorService threadPool)
	{
		this.threadPool = threadPool;
	}

	@Override
	public void run()
	{
		if (threadPool.isShutdown() || threadPool.isTerminated())
		{
			return;
		}

		// Create random mail document
		final int senderID = Config.MIN_USER + generator.nextInt(Config.MAX_USER);
		final int receiverID = Config.MIN_USER + generator.nextInt(Config.MAX_USER);
		final MailType mailType = mailTypes[1 + generator.nextInt(mailTypes.length - 1)];
		final Mail mail;

		switch (mailType)
		{
			case USER:
			{
				final int textIndex = 1 + generator.nextInt(Config.SUBJECTS.length - 1);
				mail = new MailUser(senderID, receiverID, Config.SUBJECTS[textIndex], Config.BODIES[textIndex]);
				break;
			}
			case TRADE:
			{
				final ArrayList<Long> items = new ArrayList<>();
				final int itemCount = 1 + generator.nextInt(5);
				for (int i = 0; i < itemCount; ++i)
				{
					items.add(Math.abs(generator.nextLong()));
				}
				mail = new MailTrade(senderID, receiverID, items);
				break;
			}
			default:
				return;
		}

		// Put mail on storage queue
		requestController.addDocument(
				String.valueOf(receiverID),
				UUID.randomUUID().toString(),
				gson.toJson(mail),
				TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())
		);

		if (!threadPool.isShutdown() && !threadPool.isTerminated())
		{
			threadPool.schedule(this, Config.CREATION_DELAY, Config.CREATION_TIMEUNIT);
		}
	}
}
