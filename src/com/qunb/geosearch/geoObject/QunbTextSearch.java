package com.qunb.geosearch.geoObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jclouds.aws.s3.blobstore.*;
import org.jclouds.blobstore.BlobMap;
import org.jclouds.blobstore.domain.Blob;
import org.jclouds.blobstore.domain.StorageMetadata;
import org.jclouds.blobstore.domain.StorageType;
import org.jclouds.blobstore.options.ListContainerOptions;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;


import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;

public class QunbTextSearch {
	private DatastoreService datastore;
	private String input;
	private String type;
	private String country;
	private String lang;
	private List<Entity> result;
	
	
	public QunbTextSearch(String input,String type,String country,String lang,String datastore) throws Exception{
		this.datastore=  DatastoreServiceFactory.getDatastoreService();
		if(input.contains(",")){
			
			this.input =input.substring(0, input.indexOf(","));
		}
		else{
			this.input=input;
		}
		if(lang!=null&&!lang.isEmpty()){
			this.lang=lang;
		}
		else{
			this.lang="en";
		}
		this.type = type;
		this.country = country;
		this.result = this.constructResult();
	}
	
	//TODO if ever not found, do a fuzzy match
	@SuppressWarnings("deprecation")
	public  List<Entity> getData_3parameters() throws IOException{
		List<Entity> output = new ArrayList<Entity>();
//		Query q = new Query("GeoName");
//		q.addFilter("Name", FilterOperator.EQUAL, this.input);
//		
//	
//		
//		
//		
//		
//		
//
//		/*
//		if(output.size()==0){
//			//add fuzzy match
//			System.out.println("----Launch the Fuzzy Match----");
//			for (String item:list_item) {
//				if(item.endsWith(".json")){
//					String name = item;
//					String content = new String(mystore.getService().getItem(name),"utf-8");
//					result = (JSONObject) JSONValue.parse(content);
//					if(LetterSimilarity.isSimilarEnough(result.get("toponymName").toString().toLowerCase(), this.input.toLowerCase(), CouplingLevel.LOW)&&result.get("countryName").toString().toLowerCase().equals(this.country.toLowerCase())){
//						System.out.println("---Match Found at Qunb---");
//						output.add(result);
//					}
//				}
//			}
//		}*/
//		
		return output;
	}
	
	public void serchQuery(){
		
	}
	@SuppressWarnings("deprecation")
	public List<Entity> getData_1parameter() throws IOException{
		List<Entity> output = new ArrayList<Entity>();
		Query q = new Query("GeoName");
		q.addFilter("name", FilterOperator.EQUAL, this.input);
		PreparedQuery pq = datastore.prepare(q);
		for (Entity result : pq.asIterable()) {
			String name = (String) result.getProperty("name");
			String id = String.valueOf(result.getProperty("geonameId")) ;
			  System.out.println(name + " " + id );
			  output.add(result);
			}
		
//		JSONObject result = new JSONObject();
//		QunbS3Store mystore = new QunbS3Store(this.datastore);
//		System.out.println("There are "+mystore.getService().listItems().size()+" data in qunbstore");
//		List<String> list_item = mystore.getService().listItems();
//		for (String item:list_item) {
//			if(item.endsWith(".json")){
//				String name = item;
//				String content = new String(mystore.getService().getItem(name),"utf-8");
//				result = (JSONObject) JSONValue.parse(content);
//					if(result.get("toponymName").toString().toLowerCase().equals(this.input.toLowerCase())){
//						System.out.println("---Result Found at Qunb---");
//						output.add(result);
//					}
//			}
//		}
//		System.out.println("---"+output.size()+" results found---");
//		if(output.size()==0){
//			for (String item:list_item) {
//				if(item.endsWith(".json")){
//					String name = item;
//					String content = new String(mystore.getService().getItem(name),"utf-8");
//					result = (JSONObject) JSONValue.parse(content);
//					if(LetterSimilarity.isSimilarEnough(result.get("toponymName").toString().toLowerCase(), this.input.toLowerCase(), CouplingLevel.MODERATE)){
//						System.out.println("---Match Found at Qunb---");
//						output.add(result);
//					}
//				}
//			}
//			System.out.println("---"+output.size()+" match found---");
//		}
//		if(output.size()==0){
//			System.out.println("---Result Not Found at Qunb---");
//		}
		return output;
	}
	//problem!!!
	public List<Entity> constructResult() throws Exception{
		List<Entity> mydata = new ArrayList<Entity>();

			System.out.println("Search with  parameters");
			mydata = this.getData_1parameter();
		if(mydata.isEmpty()){
			System.out.println("no results found");
			return null;
		}
		return mydata;
	}
	
	public String getInput(){
		return this.input;
	}
	public String getType(){
		return this.type;
	}
	public List<Entity> getResult(){
		return this.result;
	}
	
//	public List<Map<String,Object>> getResult(){
//		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
//		if(this.result!=null){
//			for(int i = 0;i<this.result.size();i++){
//				Map<String,Object> tmpmap = new HashMap<String,Object>();
//				tmpmap.put("qunb:geoId", this.result.get(i).get("geonameId"));
//				tmpmap.put("qunb:geoName", this.result.get(i).get("toponymName"));
//				tmpmap.put("qunb:geoLat", this.result.get(i).get("lat"));
//				tmpmap.put("qunb:geoLng", this.result.get(i).get("lng"));
//				tmpmap.put("qunb:geoType",this.result.get(i).get("fclName"));
//				tmpmap.put("qunb:geoAlterNames", this.result.get(i).get("alternateNames"));
//				tmpmap.put("qunb:population", this.result.get(i).get("population"));
//				tmpmap.put("qunb:fclcode",this.result.get(i).get("fcl"));
//				tmpmap.put("qunb:fcode",this.result.get(i).get("fcode"));
//				list.add(tmpmap);
//			}
//		}
//		return list;
//		
//	}
	public static List<Map<String,Object>> classResult(List<Map<String,Object>> mylist){
		for(int i =0;i<mylist.size()-1;i++){
			Map<String,Object> tmpmap_j = null;
			Map<String,Object> tmpmap_j_1 = null;
			for(int j=mylist.size()-1;j>i;j--){
				if(Integer.valueOf(mylist.get(j).get("qunb:population").toString())!=0 &&Integer.valueOf(mylist.get(j-1).get("qunb:population").toString())!=0){
					if(Integer.valueOf(mylist.get(j).get("qunb:population").toString())>Integer.valueOf(mylist.get(j-1).get("qunb:population").toString())){
						tmpmap_j=mylist.get(j);
						tmpmap_j_1=mylist.get(j-1);
						mylist.set(j, tmpmap_j_1);
						mylist.set(j-1,tmpmap_j);
					}
					else if(Integer.valueOf(mylist.get(j).get("qunb:population").toString())==Integer.valueOf(mylist.get(j-1).get("qunb:population").toString())){
						if(getfclordre(mylist.get(j))>getfclordre(mylist.get(j-1))){
							tmpmap_j=mylist.get(j);
							tmpmap_j_1=mylist.get(j-1);
							mylist.set(j, tmpmap_j_1);
							mylist.set(j-1,tmpmap_j);
						}
						else{
							if(getfclordre(mylist.get(j))==0&&getfclordre(mylist.get(j-1))==0){
								if(getfcodeorder_A(mylist.get(j))>getfcodeorder_A(mylist.get(j+1))){
									tmpmap_j=mylist.get(j);
									tmpmap_j_1=mylist.get(j-1);
									mylist.set(j, tmpmap_j_1);
									mylist.set(j-1,tmpmap_j);
								}
							}
							else if(getfclordre(mylist.get(j))==1&&getfclordre(mylist.get(j-1))==1){
								if(getfcodeorder_P(mylist.get(j))>getfcodeorder_P(mylist.get(j-1))){
									tmpmap_j=mylist.get(j);
									tmpmap_j_1=mylist.get(j-1);
									mylist.set(j, tmpmap_j_1);
									mylist.set(j-1,tmpmap_j);
								}
							}
						}
					}
				}
				else{ 
						if(getfclordre(mylist.get(j))>getfclordre(mylist.get(j-1))){
							tmpmap_j=mylist.get(j);
							tmpmap_j_1=mylist.get(j-1);
							mylist.set(j, tmpmap_j_1);
							mylist.set(j-1,tmpmap_j);
						}
						else{
							if(getfclordre(mylist.get(j))==0&&getfclordre(mylist.get(j-1))==0){
								if(getfcodeorder_A(mylist.get(j))>getfcodeorder_A(mylist.get(j+1))){
									tmpmap_j=mylist.get(j);
									tmpmap_j_1=mylist.get(j-1);
									mylist.set(j, tmpmap_j_1);
									mylist.set(j-1,tmpmap_j);
								}
							}
							else if(getfclordre(mylist.get(j))==1&&getfclordre(mylist.get(j-1))==1){
								if(getfcodeorder_P(mylist.get(j))>getfcodeorder_P(mylist.get(j-1))){
									tmpmap_j=mylist.get(j);
									tmpmap_j_1=mylist.get(j-1);
									mylist.set(j, tmpmap_j_1);
									mylist.set(j-1,tmpmap_j);
								}
							}
						}
				}
			}
		}
		return mylist;
	}
	public static int getfclordre(Map<String,Object> map){
		if(map.get("qunb:fclcode").equals("A")){
			return 2;
		}
		else if(map.get("qunb:fclcode").equals("P")){
			return 1;
		}
		else {
			return 0;
		}
	}
	public static int getfcodeorder_P(Map<String,Object> map){
		if(map.get("qunb:fcode").equals("PPLC")){
			return 5;
		}
		else if(map.get("qunb:fcode").equals("PPLA")){
			return 4;
		}
		else if(map.get("qunb:fcode").equals("PPLA2")){
			return 3;
		}
		else if(map.get("qunb:fcode").equals("PPLA3")){
			return 2;
		}
		else if(map.get("qunb:fcode").equals("PPLA4")){
			return 1;
		}
		else {
			return 0;
		}
	}
	public static int getfcodeorder_A(Map<String,Object> map){
		if(map.get("qunb:fcode").equals("PCL")){
			return 8;
		}
		else if(map.get("qunb:fcode").equals("PCLD")){
			return 7;
		}
		else if(map.get("qunb:fcode").equals("PCLF")){
			return 6;
		}
		else if(map.get("qunb:fcode").equals("ADMD")){
			return 5;
		}
		else if(map.get("qunb:fcode").equals("ADM1")){
			return 4;
		}
		else if(map.get("qunb:fcode").equals("ADM2")){
			return 3;
		}
		else if(map.get("qunb:fcode").equals("ADM3")){
			return 2;
		}
		else if(map.get("qunb:fcode").equals("ADM4")){
			return 1;
		}
		else {
			return 0;
		}
	}
	
 
}
