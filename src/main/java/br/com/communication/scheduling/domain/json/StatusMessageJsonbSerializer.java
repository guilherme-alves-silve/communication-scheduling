package br.com.communication.scheduling.domain.json;

import br.com.communication.scheduling.domain.entity.StatusMessage;

import javax.json.bind.serializer.JsonbSerializer;
import javax.json.bind.serializer.SerializationContext;
import javax.json.stream.JsonGenerator;

public class StatusMessageJsonbSerializer implements JsonbSerializer<StatusMessage> {

    private static final int FALSE = 0;

    @Override
    public void serialize(StatusMessage obj, JsonGenerator generator, SerializationContext ctx) {
        generator.write(FALSE == obj.ordinal());
        ctx.serialize(obj, generator);
    }
}
