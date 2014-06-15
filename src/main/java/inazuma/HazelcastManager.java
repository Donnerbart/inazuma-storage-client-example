package inazuma;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.SerializerConfig;
import com.hazelcast.core.HazelcastInstance;
import request.serialization.AddDocumentTaskStreamSerializer;
import request.serialization.DeleteDocumentTaskStreamSerializer;
import request.serialization.GetDocumentMetadataTaskStreamSerializer;
import request.serialization.GetDocumentTaskStreamSerializer;
import request.task.AddDocumentTask;
import request.task.DeleteDocumentTask;
import request.task.GetDocumentMetadataTask;
import request.task.GetDocumentTask;

public class HazelcastManager
{
	public static HazelcastInstance getInstance()
	{
		final SerializerConfig addDocumentConfig = new SerializerConfig();
		addDocumentConfig.setImplementation(new AddDocumentTaskStreamSerializer()).setTypeClass(AddDocumentTask.class);

		final SerializerConfig deleteDocumentConfig = new SerializerConfig();
		deleteDocumentConfig.setImplementation(new DeleteDocumentTaskStreamSerializer()).setTypeClass(DeleteDocumentTask.class);

		final SerializerConfig getDocumentConfig = new SerializerConfig();
		getDocumentConfig.setImplementation(new GetDocumentTaskStreamSerializer()).setTypeClass(GetDocumentTask.class);

		final SerializerConfig getDocumentMetadataConfig = new SerializerConfig();
		getDocumentMetadataConfig.setImplementation(new GetDocumentMetadataTaskStreamSerializer()).setTypeClass(GetDocumentMetadataTask.class);

		final ClientConfig cfg = new ClientConfig();
		cfg.getSerializationConfig()
				.addSerializerConfig(addDocumentConfig)
				.addSerializerConfig(deleteDocumentConfig)
				.addSerializerConfig(getDocumentConfig)
				.addSerializerConfig(getDocumentMetadataConfig);

		return HazelcastClient.newHazelcastClient(cfg);
	}
}
