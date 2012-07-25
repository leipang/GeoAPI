package com.qunb.geosearch.TextSearch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.geonames.WebService;
import org.json.*;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.google.appengine.api.datastore.Entity;
import com.qunb.geosearch.geoObject.GeoNamesTextSearch;
import com.qunb.geosearch.geoObject.QunbTextSearch;


import javax.ws.rs.Path;


@Path("/geosearch/textsearch/search")
public class QunbGeoTextSearchResource extends ServerResource{
	@SuppressWarnings("static-access")
	@Get
	public JSONObject searchGeoID() throws Exception{

		String input = getQuery().getValues("q");
		String type = getQuery().getValues("type");
		String country = getQuery().getValues("country");
		String lang = getQuery().getValues("lang");
		String limit = getQuery().getValues("limit");
		QunbTextSearch mysearch2 = new QunbTextSearch(input,type,country,lang, limit);
		//mysearch2.storeData();
		List<Entity> myresult = mysearch2.getData_1parameter();
		if(myresult.isEmpty()){
			GeoNamesTextSearch mysearch = new GeoNamesTextSearch(input,type,country,lang);
			mysearch.storeData();
			//QunbTextSearch mysearch1 = new QunbTextSearch(input,type,country,lang, limit);
			myresult = mysearch.getResult();
		}
		
		JSONObject json = toJson(myresult.get(0));
		return json;
	}
	
	public JSONObject toJson(Entity geo) throws JSONException{
		JSONObject obj=new JSONObject();
		obj.put("qunb:geoId", geo.getProperty("geonameId"));
		obj.put("qunb:geoName", geo.getProperty("name"));
		//obj.put("qunb:AlternateNames", geo.getProperty("AlternateNames"));
		return obj;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject getSearchResult(List<JSONObject> mylist){
		System.out.println(mylist.size());
		JSONObject output = new JSONObject();
		for(int i = 0;i<mylist.size();i++){
			 if(i==0){
				   try {
					   output.put("result", mylist.get(i));
				   } catch (JSONException e) {
					   e.printStackTrace();
				   }
			   }
			   else{
				   try {
					   output.accumulate("result", mylist.get(i));
				   } catch (JSONException e) {
					   e.printStackTrace();
				   }
			   }
  
		}
		return output;
	}

}


