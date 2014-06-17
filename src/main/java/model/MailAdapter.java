package model;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class MailAdapter implements JsonDeserializer<Mail>
{
	@Override
	public Mail deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException
	{
		final String mailType = json.getAsJsonObject().get("mailType").getAsString();
		if (mailType == null || mailType.length() == 0)
		{
			throw new JsonParseException("Could not find attribute mailType in " + json);
		}
		return context.deserialize(json, MailType.valueOf(mailType).getType());
	}
}
