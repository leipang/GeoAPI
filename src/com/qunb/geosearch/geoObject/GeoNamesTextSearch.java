package com.qunb.geosearch.geoObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;

import org.geonames.Toponym;
import org.geonames.ToponymSearchResult;
import org.geonames.WebService;
import org.jclouds.blobstore.BlobStore;
import org.jclouds.blobstore.domain.Blob;
import org.jclouds.blobstore.domain.BlobBuilder;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.*;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;
import com.google.gson.Gson;




public class GeoNamesTextSearch {
	private String input;
	private String lang;
	private List<Entity> result;
	private DatastoreService datastore;
	
	@SuppressWarnings("static-access")
	public GeoNamesTextSearch(String input,String type,String country,String lang) throws Exception{
		this.datastore=  DatastoreServiceFactory.getDatastoreService();
		this.input = input;
		if(lang!=null&&!lang.isEmpty()){
			this.lang=lang;
		}
		else{
			this.lang="en";
		}
		this.result = this.constructResult();
	}
	
	@SuppressWarnings("static-access")
	public List<Entity> getData() throws Exception{
		List<Entity> output = new ArrayList<Entity>();
		String text = this.input;
		output = searchGeonames(text);
		return output;
	}
	public List<Entity> searchGeonames(String text) throws IOException{
		List<GeoName> search_result = new ArrayList<GeoName>();
		List<Entity> result = new ArrayList<Entity>();
		try {
			String res = "";
            URL url = new URL("http://api.geonames.org/search?formatted=true&type=json" +
            		"&q="+text+"&lang="+this.lang+"&maxRows=10&username="+"leipang");
            URLConnection urlConnection = url.openConnection();
            urlConnection.setReadTimeout(20000);
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                res += line;
            }
            reader.close();

            Gson gson = new Gson();
            GeoNames geoNames = gson.fromJson(res, GeoNames.class);

            if(geoNames!=null)
            {
            	search_result = geoNames.getGeonames();
            }
		} catch (Exception ex) {
				ex.printStackTrace();
		}
		for(GeoName geo:search_result){
			result.add(constructGeo(geo.getGeonameId()).toEntity());
		}
		return result;
	}
	public GeoName JsonToGeoName(JSONObject json){
			GeoName geo = new GeoName();
			geo.setGeonameId(Long.parseLong(json.get("geonameId").toString()));
			geo.setToponymName(json.get("toponymName").toString());
			//geo.setAlternateNames((JSONObject)json.get("alternateNames"));
			geo.setCountryName(json.get("countryName").toString());
			geo.setCountryCode(json.get("countryCode").toString());
			geo.setLat(Double.parseDouble(json.get("lat").toString()));
			geo.setLng(Double.parseDouble(json.get("lng").toString()));
			geo.setFcl(json.get("fcl").toString());
			geo.setFcode(json.get("fcode").toString());
			if(json.get("population").toString().equals(null)){
				System.out.println("no population");
				geo.setPopulation(0);
			}
			else{
				geo.setPopulation(Integer.parseInt(json.get("population").toString()));
			}
		return geo;
	}
	
	public GeoName constructGeo(long geoid) throws IOException{
			String res = "";
            URL url = new URL("http://api.geonames.org/getJSON?formatted=true" +"&geonameId="+String.valueOf(geoid)+"&lang="+this.lang+"&username="+"jbtheard24");
            URLConnection urlConnection = url.openConnection();
            urlConnection.setReadTimeout(20000);
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                res += line;
            }
            reader.close();
            JSONObject json_result = (JSONObject) JSONValue.parse(res);
            GeoName geoName = JsonToGeoName(json_result);
    		return geoName;
	}

	private List<Entity> constructResult() throws Exception{
		return this.getData();
	}
	
	public void storeData() throws Exception{
		System.out.println("Geonames API find "+this.getData().size()+" results!");
		List<Entity> datalist= this.getData();
		for(int i =0;i<datalist.size();i++){
			Entity geo = datalist.get(i);
			this.datastore.put(geo);
			System.out.println("data added");
		}
	}
	
	
	public void setFeatureClassName(Toponym topo){
		FeatureClass featureclass = FeatureClass.valueOf(topo.getFeatureClass().name());
		switch (featureclass){
		case A:
			topo.setFeatureClassName("country, state, region");
			break;
		case P:
			topo.setFeatureClassName("city, village");
			break;
		case H:
			topo.setFeatureClassName("stream, lake");
			break;
		case R:
			topo.setFeatureClassName("road, railroad");
			break;
		case S:
			topo.setFeatureClassName("spot, building, farm");
			break;
		case U:
			topo.setFeatureClassName("undersea");
			break;
		case T:
			topo.setFeatureClassName("mountain,hill,rock");
			break;
		case V:
			topo.setFeatureClassName("forest,heath");
			break;
		case L:
			topo.setFeatureClassName("parks,area");
			break;
		}
	}
	
	public List<Entity> getResult(){
		return this.result;
	}
	public enum FeatureClass {

	    A,P,R,H,L,S,U,T,V
	}
}
