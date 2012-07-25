package com.qunb.geosearch.geoObject;

import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.json.JSONObject;


import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;

public class GeoName {
	private String toponymName;
	private String name;
	private double lat;
	private double lng;
	private Long geonameId;
	private int population;
	private String countryCode;
	private String countryName;
	private String fcl;
	private String fcode;
	private JSONObject alternateNames;
	
	public GeoName(){
		
	}
	
	public void setToponymName(String toponymName) {
		this.toponymName = toponymName;
	}
	public String getToponymName() {
		return toponymName;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLat() {
		return lat;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	public double getLng() {
		return lng;
	}
	public void setGeonameId(Long geonameId) {
		this.geonameId = geonameId;
	}
	public Long getGeonameId() {
		return geonameId;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setFcode(String fcode) {
		this.fcode = fcode;
	}
	public String getFcode() {
		return fcode;
	}
	public void setFcl(String fcl) {
		this.fcl = fcl;
	}
	public String getFcl() {
		return fcl;
	}
	public JSONObject getAlternateNames(){
		return alternateNames;
	}
	public void setAlternateNames(JSONObject alternatenames){
		this.alternateNames = alternatenames;
	}
	public int getPopulation(){
		return population;
	}
	public void setPopulation(int population){
		this.population=population;
	}
	public Entity toEntity(){
		Key geo_key = KeyFactory.createKey("GeonameId", this.getGeonameId());
		Entity geo = new Entity("GeoName",geo_key);
		geo.setProperty("GeonameId", this.getGeonameId());
		geo.setProperty("countryName", this.getCountryName());
		geo.setProperty("countryCode", this.getCountryCode());
		geo.setProperty("ToponymName", this.getToponymName());
		geo.setProperty("Lat", this.getLat());
		geo.setProperty("Lng", this.getLng());
		geo.setProperty("AlternateNames", this.getAlternateNames());
		geo.setProperty("Fcl", this.getFcl());
		geo.setProperty("Fcode", this.getFcode());
		geo.setProperty("Population", this.getPopulation());
		return geo;
	}
}
