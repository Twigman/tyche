package de.qwyt.housecontrol.tyche.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import de.qwyt.housecontrol.tyche.client.SpotifyApiClient;
import de.qwyt.housecontrol.tyche.util.Symbole;
import de.qwyt.housecontrol.tyche.event.types.HousecontrolModule;
import de.qwyt.housecontrol.tyche.event.types.LogLevel;

@Service
public class SpotifyServiceImpl {
	
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	private final SpotifyApiClient spotifyApiClient;
	
	private final EventServiceImpl eventService;
	
	@Autowired
	public SpotifyServiceImpl(SpotifyApiClient spotifyApiClient, EventServiceImpl eventService) {
		this.spotifyApiClient = spotifyApiClient;
		this.eventService = eventService;
	}

	public boolean refreshToken() {
		boolean refreshed = spotifyApiClient.refreshAccessToken();
		
		if (refreshed) {
			eventService.fireLogEvent(HousecontrolModule.SYSTEM, LogLevel.INFO, "Spotify access token refreshed");
		} else {
			eventService.fireLogEvent(HousecontrolModule.SYSTEM, LogLevel.ERROR, "Couldn't refresh spotify access token");
		}
		
		return refreshed;
	}
	
	public void setVolume(int percent) {
		spotifyApiClient.setVolume(percent);
		eventService.fireLogEvent(HousecontrolModule.SPOTIFY, LogLevel.INFO, "Volume " + Symbole.WEB_ARROW_RIGHT + " " + percent + "%");
	}
	
	@Scheduled(fixedRateString = "#{@spotifyApiClient.getDefaultExpiresInMsWithBuffer}")
	public void scheduledTokenRefresh() {
		refreshToken();
	}

	public boolean play() {
		boolean res = spotifyApiClient.play();
		
		if (res) {
			eventService.fireLogEvent(HousecontrolModule.SPOTIFY, LogLevel.INFO, "Playing music");
		} else {
			eventService.fireLogEvent(HousecontrolModule.SPOTIFY, LogLevel.ERROR, "Can't play music");
		}
		
		return res;
	}
	
	public boolean pause() {
		boolean res = spotifyApiClient.pause();
		
		if (res) {
			eventService.fireLogEvent(HousecontrolModule.SPOTIFY, LogLevel.INFO, "Paused music");
		} else {
			eventService.fireLogEvent(HousecontrolModule.SPOTIFY, LogLevel.ERROR, "Can't pause music");
		}
		
		return res;
	}
	
	public boolean next() {
		boolean res = spotifyApiClient.next();
		
		if (res) {
			eventService.fireLogEvent(HousecontrolModule.SPOTIFY, LogLevel.INFO, "Next track");
		} else {
			eventService.fireLogEvent(HousecontrolModule.SPOTIFY, LogLevel.ERROR, "Can't go to next song");
		}
		
		return res;
	}
	
	public boolean previous() {
		boolean res = spotifyApiClient.previous();
		
		if (res) {
			eventService.fireLogEvent(HousecontrolModule.SPOTIFY, LogLevel.INFO, "Previous track");
		} else {
			eventService.fireLogEvent(HousecontrolModule.SPOTIFY, LogLevel.ERROR, "Can't rewind to last song");
		}
		
		return res;
	}

}
