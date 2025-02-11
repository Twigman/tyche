package de.qwyt.housecontrol.tyche.model.profile.automation;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document(collection = "ActiveAutomationProfiles")
public class ActiveAutomationProfile {
	
	@Id
	private String id;
	
	@Indexed
	private AutomationProfileType profileType;
	
	@LastModifiedDate
	private Instant timestamp;
	
	public ActiveAutomationProfile(AutomationProfileType profileType) {
		this.id = null;
		this.profileType = profileType;
		this.timestamp = null;
	}

}
