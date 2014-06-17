import worker.CreateRandomMails;
import de.donnerbart.inazuma.storage.base.util.NamedThreadFactory;
import de.donnerbart.inazuma.storage.client.InazumaStorageClient;
import util.Config;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main
{
	public static void main(final String[] args)
	{
		final CountDownLatch latch = InazumaStorageClient.start();

		// Configure thread pool
		System.out.println("Running for " + Config.RUNTIME + " ms...");

		final ScheduledExecutorService threadPoolCreation = Executors.newScheduledThreadPool(10, new NamedThreadFactory("MailCreation"));
		for (int i = 0; i < Config.CREATION_JOBS; ++i)
		{
			threadPoolCreation.submit(new CreateRandomMails(threadPoolCreation));
		}

		// Wait until runtime is over
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

		// Shutdown of Inazuma-Storage-Client
		InazumaStorageClient.shutdown();

		// Wait for shutdown hook
		System.out.println("Inazuma-Storage-Client is running...");
		try
		{
			latch.await();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		System.out.println("Inazuma-Storage-Client is shut down!");
		System.exit(0);
	}
}
