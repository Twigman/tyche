package de.qwyt.housecontrol.tyche.controller.validator;

import java.security.interfaces.ECPublicKey;
import java.text.ParseException;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.ECDSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jose.jwk.ECKey;

@Component
public class DPoPValidator {
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	// protection against replay-attack: store used jti-values
	private static final ConcurrentHashMap<String, Long> usedJtiTokens = new ConcurrentHashMap<>(); 

	
	public void validateDPoPToken(String dpopHeader, String expectedUrl, String expectedMethod) {
		try {
			SignedJWT signedJWT = SignedJWT.parse(dpopHeader);
			
			// expiry date valid?
			if (new Date().after(signedJWT.getJWTClaimsSet().getExpirationTime())) {
				LOG.warn("DPoP token expired");
				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "DPoP token expired");
			}
			
			String method = signedJWT.getJWTClaimsSet().getStringClaim("htm");
			String url = signedJWT.getJWTClaimsSet().getStringClaim("htu");
			
			if (!method.equals(expectedMethod) || !url.equals(expectedUrl)) {
				LOG.warn("DPoP invalid for this request");
				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "DPoP invalid for this request");
			}
			
			JWK jwk = JWK.parse(signedJWT.getHeader().getJWK().toJSONString());
			
			if (!(jwk instanceof ECKey)) {
				LOG.warn("Invalid DPoP key type");
				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid DPoP key type");
			}
			
			ECPublicKey publicKey = ((ECKey) jwk).toECPublicKey();
			JWSVerifier verifier = new ECDSAVerifier(publicKey);
			
			if (!signedJWT.verify(verifier)) {
				LOG.warn("Invalid DPoP signature");
				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid DPoP signature");
			}
			
			String jti = signedJWT.getJWTClaimsSet().getJWTID();
			
			if (usedJtiTokens.containsKey(jti)) {
				LOG.warn("Replay attack detected");
				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Replay attack detected");
			}
			
			usedJtiTokens.put(jti, System.currentTimeMillis());
			
		} catch (ParseException | JOSEException e) {
			LOG.warn("Invalid UPoP token");
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid UPoP token");
		}
	}
}
