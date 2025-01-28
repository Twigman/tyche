package de.qwyt.housecontrol.tyche.model.websocket;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NOT_USED_RaspbeeMessage {
		@JsonProperty("e")
		private String event;
		
		@JsonProperty("id")
		private String resourceId;
		
		@JsonProperty("r")
		private String resourceType;
		
		@JsonProperty("t")
		private String messageType;
		
		@JsonProperty("attr")
		private NOT_USED_RaspbeeAttr attr;
		
		@JsonProperty("state")
		private NOT_USED_RaspbeeState state;
		
		@JsonProperty("uniqueid")
		private String uniqueId;
		
		
		public boolean hasAttr() {
			if (this.attr == null) {
				return false;
			} else {
				return true;
			}
		}
		
		public boolean hasState() {
			if (this.state == null) {
				return false;
			} else {
				return true;
			}
		}
		
		public String getInfo() {
			return "event: " + this.event +
					", resourceId: " + this.resourceId +
					", uniqueIde:" + this.uniqueId +
					", resourceType: " + this.resourceType +
					", messageType: " + this.messageType +
					", hasAttr: " + this.hasAttr() +
					", hasState: " + this.hasState();
		}
}
