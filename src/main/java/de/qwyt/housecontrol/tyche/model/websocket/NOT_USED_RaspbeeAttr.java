package de.qwyt.housecontrol.tyche.model.websocket;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NOT_USED_RaspbeeAttr {
	
	private String id;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
	private Date lastannounced;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm'Z'")
	private Date lastseen;
	
	private String manufacturername;
	
	private int mode;
	
	private String modelid;
	
	private String name;
	
	private String productname;
	
	private String swversion;
	
	private String type;
	
	private String uniqueid;
	
	private int colorcapabilities;
	
	private int ctmax;
	
	private int ctmin;
}