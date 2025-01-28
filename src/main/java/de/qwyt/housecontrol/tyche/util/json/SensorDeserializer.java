package de.qwyt.housecontrol.tyche.util.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import de.qwyt.housecontrol.tyche.model.sensor.zha.Sensor;

public class SensorDeserializer extends JsonDeserializer<Sensor> {

	/**
	 * Takes the "type" attribute of a sensor and creates a copy of it in the "state".
	 */
	@Override
	public Sensor deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException, JacksonException {
		ObjectMapper mapper = (ObjectMapper) parser.getCodec();
        ObjectNode rootNode = (ObjectNode) mapper.readTree(parser);

        // get "type" from sensor node
        String type = rootNode.get("type").asText();

        // insert "type" in den "state"-node
        if (rootNode.has("state")) {
            ObjectNode stateNode = (ObjectNode) rootNode.get("state");
            
            if (stateNode != null && stateNode.isObject()) {
            	stateNode.put("type", type);
            } else {
            	// TODO exception
            }
        }

        return mapper.treeToValue(rootNode, Sensor.class);
	}

}
