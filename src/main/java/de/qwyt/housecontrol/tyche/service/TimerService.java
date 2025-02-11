package de.qwyt.housecontrol.tyche.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TimerService {
	
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	private final Map<String, Timer> timerMap;
	
	private final Map <String, TimerTask> taskMap;
	
	
	public TimerService() {
		this.timerMap = new HashMap<>();
		this.taskMap = new HashMap<>();
	}
	
	public void startTimer(String id, long delay, Runnable callback) {
		// cancel running timer and task
		if (taskMap.containsKey(id)) {
			taskMap.get(id).cancel();
		}
		if (timerMap.containsKey(id)) {
			timerMap.get(id).cancel();
		}
		
		// create new Timer-Task
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				callback.run();
				timerMap.remove(id);
				taskMap.remove(id);
			}
		};
		
		Timer timer = new Timer();
		timer.schedule(task, delay);
		
		LOG.debug("Timer started for {}", id);
		
		timerMap.put(id, timer);
		taskMap.put(id, task);
	}
	
	public void cancelTimer(String id) {
		if (taskMap.containsKey(id)) {
			taskMap.get(id).cancel();
			taskMap.remove(id);
			LOG.debug("Task canceled for {}", id);
		}
		if (timerMap.containsKey(id)) {
			timerMap.get(id).cancel();
			timerMap.remove(id);
			LOG.debug("Timer canceled for {}", id);
		}
	}
	
	public void cancelAllTimers() {
		if (taskMap.size() != 0) {
			taskMap.forEach((id, task) -> {
				task.cancel();
				LOG.debug("Task canceled for {}", id);
			});
			taskMap.clear();
		}
		if (timerMap.size() != 0) {
			timerMap.forEach((id, timer) -> {
				timer.cancel();
				LOG.debug("Timer canceled for {}", id);
			});
			timerMap.clear();
		}
	}
}
