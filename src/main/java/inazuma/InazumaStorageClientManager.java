package inazuma;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import jmx.JMXAgent;
import request.RequestController;
import stats.StatisticManager;

import java.util.concurrent.CountDownLatch;

public class InazumaStorageClientManager
{
	private static final CountDownLatch latch = new CountDownLatch(1);

	public static CountDownLatch start()
	{
		// Get Hazelcast instance
		final HazelcastInstance hz = HazelcastClientManager.getInstance();

		// Start JMX agent
		new JMXAgent("de.donnerbart", "InazumaStorageClient");

		// Startup request controller
		new RequestController(hz, null);

		// Create shutdown event
		Runtime.getRuntime().addShutdownHook(new Thread(new HazelcastShutdownHook()));

		return latch;
	}

	private static class HazelcastShutdownHook implements Runnable
	{
		@Override
		public void run()
		{
			System.out.println("Received shutdown signal!");

			// Shutdown request storage
			System.out.println("Shutting down RequestController...");
			RequestController.getInstance().shutdown();
			System.out.println("Done!\n");

			// Shutdown of Hazelcast instance
			System.out.println("Shutting down Hazelcast instance...");
			Hazelcast.shutdownAll();
			System.out.println("Done!\n");

			// Shutdown of StatisticManager
			System.out.println("Shutting down StatisticManager...");
			StatisticManager.getInstance().shutdown();
			System.out.println("Done!\n");

			// Release main thread
			latch.countDown();
		}
	}
}
