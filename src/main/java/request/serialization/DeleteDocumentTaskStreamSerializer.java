package request.serialization;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.StreamSerializer;
import request.task.DeleteDocumentTask;

import java.io.IOException;

public class DeleteDocumentTaskStreamSerializer implements StreamSerializer<DeleteDocumentTask>
{
	@Override
	public void write(final ObjectDataOutput out, final DeleteDocumentTask object) throws IOException
	{
		out.writeUTF(object.getUserID());
		out.writeUTF(object.getKey());
	}

	@Override
	public DeleteDocumentTask read(final ObjectDataInput in) throws IOException
	{
		return new DeleteDocumentTask(in.readUTF(), in.readUTF());
	}

	@Override
	public int getTypeId()
	{
		return StreamSerializerId.DELETE_DOCUMENT_TASK.ordinal();
	}

	@Override
	public void destroy()
	{
	}
}
