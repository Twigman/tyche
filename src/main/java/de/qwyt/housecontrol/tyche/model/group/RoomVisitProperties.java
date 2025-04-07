package de.qwyt.housecontrol.tyche.model.group;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomVisitProperties {
	
	private boolean watchVisits;
	
	private int visitThreshold;
	
	private int visitCounter;
	
	private int visitTimespanInSec;
	
	private List<Instant> visitTimestamps;
	
	public RoomVisitProperties() {
		visitTimestamps = new ArrayList<>();
	}
	
	public void increaseVisitCounterBy(int value) {
		this.visitCounter += value;
	}
	
	public long getVisitCounterForLast(long seconds) {
		Instant cufoff = Instant.now().minusSeconds(seconds);
		
		return visitTimestamps.stream().filter(timestamp -> timestamp.isAfter(cufoff)).count();
	}
	
	public long getVisitCounterForTimespan() {
		return getVisitCounterForLast(this.visitTimespanInSec);
	}
}
