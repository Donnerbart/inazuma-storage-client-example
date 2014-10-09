import de.donnerbart.inazuma.storage.base.util.NamedThreadFactory;
import de.donnerbart.inazuma.storage.client.InazumaStorageClient;
import util.Config;
import worker.CreateRandomMails;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main
{
	public static void main(final String[] args)
	{
		System.out.println("Running for " + TimeUnit.MILLISECONDS.toSeconds(Config.RUNTIME) + " seconds...\n");

		// Start Inazuma-Storage client
		final CountDownLatch latch = InazumaStorageClient.start();

		// Configure thread pool
		final ScheduledExecutorService threadPoolCreation = Executors.newScheduledThreadPool(10, new NamedThreadFactory("MailCreation"));
		for (int i = 0; i < Config.CREATION_JOBS; ++i)
		{
			threadPoolCreation.submit(new CreateRandomMails(threadPoolCreation));
		}

		// Wait until configured runtime is over
		try
		{
			TimeUnit.MILLISECONDS.sleep(Config.RUNTIME);
		}
		catch (InterruptedException ignored)
		{
		}

		// Shutdown of thread pool
		System.out.println("Shutting down thread pool...");
		threadPoolCreation.shutdown();
		try
		{
			threadPoolCreation.awaitTermination(60, TimeUnit.SECONDS);
		}
		catch (InterruptedException ignored)
		{
		}
		System.out.println("Done!\n");

		// Shutdown of Inazuma-Storage client
		System.out.println("Shutting down Inazuma-Storage client...\n");
		InazumaStorageClient.shutdown();
		try
		{
			latch.await();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		System.out.println("Inazuma-Storage client is shut down!\n");

		System.exit(0);
	}
}
