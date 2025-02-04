package de.qwyt.housecontrol.tyche.repository.light;

import de.qwyt.housecontrol.tyche.model.light.hue.HueLightState;

public interface CustomHueLightStateRepository {
	<T extends HueLightState> T saveNew(T hueLightState);
}
