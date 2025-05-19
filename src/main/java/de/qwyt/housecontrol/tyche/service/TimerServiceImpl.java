package de.qwyt.housecontrol.tyche.service;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

@Service
public class TimerServiceImpl {
	
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	private final Map<String, ScheduledFuture<?>> scheduledTasks;
	
	private final ThreadPoolTaskScheduler taskScheduler;
	
	private final EventServiceImpl eventService;
	
	private final Map<String, Instant> timerTargetTimes;
	
	
	public TimerServiceImpl(EventServiceImpl eventService) {
		this.scheduledTasks = new ConcurrentHashMap<>();
        this.taskScheduler = new ThreadPoolTaskScheduler();
        this.timerTargetTimes = new ConcurrentHashMap<>();
		// 20 parallel timers possible
		this.taskScheduler.setPoolSize(20);
		this.taskScheduler.initialize();
		this.eventService = eventService;
	}
	
	
	public void startTimer(String id, long delayMillis, Runnable callback) {
		cancelTimer(id);
		// calc target time
		Instant targetTime = Instant.now().plusMillis(delayMillis);
		timerTargetTimes.put(id, targetTime);
		
		ScheduledFuture<?> future = taskScheduler.schedule(() -> {
			callback.run();
			scheduledTasks.remove(id);
			timerTargetTimes.remove(id);
			LOG.debug("Timer " + id + " fired!");
		}, Instant.now().plusMillis(delayMillis));
		
		scheduledTasks.put(id, future);
		LOG.debug("Timer started for {}", id);
	}
	
	
	public void startRepeatingTimer(String id, long initialDelay, Duration interval, Runnable callback) {
		cancelTimer(id);

		// TODO: target time not stored
        ScheduledFuture<?> future = taskScheduler.scheduleAtFixedRate(
                callback,
                Instant.now().plusMillis(initialDelay),
                interval
        );

        scheduledTasks.put(id, future);
        LOG.debug("Repeating timer started for {} every {}", id, interval);
	}
	
    public void cancelTimer(String id) {
        ScheduledFuture<?> future = scheduledTasks.remove(id);
        if (future != null) {
            future.cancel(false);
            LOG.debug("Timer canceled for {}", id);
        }
        timerTargetTimes.remove(id);
    }

    public boolean isTimerCreated(String id) {
    	return scheduledTasks.containsKey(id);
    }
    
    public void cancelAllTimers() {
        scheduledTasks.forEach((id, future) -> {
            future.cancel(false);
            LOG.debug("Timer canceled for {}", id);
        });
        scheduledTasks.clear();
        timerTargetTimes.clear();
    }
    
    public Set<String> getRunningTimerIds() {
    	return scheduledTasks.keySet();
    }
    
    public Optional<Duration> getRemainingTime(String id) {
    	Instant target = timerTargetTimes.get(id);
    	
    	if (target == null) {
    		return Optional.empty();
    	}
    	
    	Duration remaining = Duration.between(Instant.now(), target);
    	return remaining.isNegative() ? Optional.of(Duration.ZERO) : Optional.of(remaining);
    }
}
