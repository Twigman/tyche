package de.qwyt.housecontrol.tyche.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import de.qwyt.housecontrol.tyche.controller.response.SpotifyTokenResponse;
import de.qwyt.housecontrol.tyche.util.Symbole;

@Component
public class SpotifyApiClient {
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());

	@Value("${spotify.credentials.clientId}")
	private String clientId;
	
	@Value("${spotify.credentials.clientSecret}")
	private String clientSecret;
	
	@Value("${spotify.api.refresh_token}")
	private String refreshToken;
	
	@Value("${spotify.urls.tokenUrl}")
	private String tokenUrl;
	
	@Value("${spotify.urls.playerUrl}")
	private String playerUrl;
	
	@Value("${spotify.api.expires_in}")
	private long defaultExpiresIn;
	
	private String accessToken;
	
	private int expiresIn;
	
	private final RestTemplate restTemplate;
	
	@Autowired
	public SpotifyApiClient(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	public boolean refreshAccessToken() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setBasicAuth(clientId, clientSecret);
		
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", "refresh_token");
		body.add("refresh_token", refreshToken);
		
		LOG.debug("Requesting spotify access token...");
		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
		ResponseEntity<SpotifyTokenResponse> response = restTemplate.postForEntity(tokenUrl, request, SpotifyTokenResponse.class);
		
		if (response.getStatusCode() == HttpStatus.OK) {
			accessToken = response.getBody().getAccess_token();
			expiresIn = (int) response.getBody().getExpires_in();
			LOG.info("Spotify access token refreshed. Expires in {} minutes", (expiresIn / 60));
			
			if (expiresIn != defaultExpiresIn) {
				LOG.warn("Time for expiration changed from {} to {} minutes!", (defaultExpiresIn / 60), (expiresIn / 60));
			}
			
			return true;
		} else {
			LOG.error("Error during spotify token refresh! Status Code: {}", response.getStatusCode());
			return false;
		}
	}
	
	public void setVolume(int percent) {
		if (percent > 100) {
			percent = 100;
		} else if (percent < 0) {
			percent = 0;
		}
		
		String url = playerUrl + "/volume?volume_percent=" + percent;
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + accessToken);
		
		HttpEntity<String> request = new HttpEntity<>(headers);
		restTemplate.exchange(url, HttpMethod.PUT, request, String.class);
		
		LOG.info("Spotify volume {} {}%", Symbole.ARROW_RIGHT, percent);
	}
	
	public boolean play() {
		return sendCommand("/play", HttpMethod.PUT);
	}
	
	public boolean pause() {
		return sendCommand("/pause", HttpMethod.PUT);
	}
	
	public boolean next() {
		return sendCommand("/next", HttpMethod.POST);
	}
	
	public boolean previous() {
		return sendCommand("/next", HttpMethod.POST);
	}
	
	private boolean sendCommand(String endpoint, HttpMethod method) {
		HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Void> response = restTemplate.exchange(
            		playerUrl + endpoint, method, requestEntity, Void.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                LOG.info("Spotify command executed: " + endpoint);
                return true;
            } else {
            	LOG.error("Spotify API returned error: " + response.getStatusCode());
                return false;
            }
        } catch (Exception e) {
        	LOG.error("Error communicating with Spotify API: " + e.getMessage());
            return false;
        }
	}
	
	public int getExpiresIn() {
		return expiresIn;
	}
	
	public long getDefaultExpiresInMsWithBuffer() {
		// default time - 5 min
		return (defaultExpiresIn - (5 * 60)) * 1000;
	}
}
