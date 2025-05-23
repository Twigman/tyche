package de.qwyt.housecontrol.tyche.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import de.qwyt.housecontrol.tyche.service.SpotifyServiceImpl;

@RestController
@RequestMapping("/api/spotify")
public class SpotifyController {
	
	@Autowired
	private SpotifyServiceImpl spotifyService;
	
	@PutMapping(path = "/volume")
	public ResponseEntity<Void> putVolume(@RequestParam("vol") int percent) {
		spotifyService.setVolume(percent);
		
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping(path = "/play")
	public ResponseEntity<Void> putPlay() {
		boolean res = spotifyService.play();
		
		if (res) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.internalServerError().build();
		}
	}

	@PutMapping(path = "/pause")
	public ResponseEntity<Void> putPause() {
		boolean res = spotifyService.pause();
		
		if (res) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.internalServerError().build();
		}
	}
	
	@PutMapping(path = "/next")
	public ResponseEntity<Void> putNext() {
		boolean res = spotifyService.next();
		
		if (res) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.internalServerError().build();
		}
	}
	
	@PutMapping(path = "/previous")
	public ResponseEntity<Void> putPrevious() {
		boolean res = spotifyService.previous();
		
		if (res) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.internalServerError().build();
		}
	}
}
