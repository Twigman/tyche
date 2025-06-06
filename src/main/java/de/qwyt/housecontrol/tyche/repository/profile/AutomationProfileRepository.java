package de.qwyt.housecontrol.tyche.repository.profile;

import org.springframework.data.mongodb.repository.MongoRepository;

import de.qwyt.housecontrol.tyche.model.profile.automation.AutomationProfile;

public interface AutomationProfileRepository extends MongoRepository<AutomationProfile, String> {

}
