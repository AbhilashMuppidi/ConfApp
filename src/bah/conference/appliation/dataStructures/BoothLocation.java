package bah.conference.appliation.dataStructures;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;

public class BoothLocation {
	public int pk_id = -1;
	public String boothName = "";
	public String mapName = "";
	private ArrayList<Corner> corners = new ArrayList<Corner>();
	public BoothLocation(){
		this.boothName = "";
		this.corners = new ArrayList<Corner>();
	}
	public BoothLocation(Cursor cursor){
		if(cursor.getColumnIndex("pk_id")>-1)
		this.pk_id = cursor.getInt(cursor.getColumnIndex("pk_id"));
		if(cursor.getColumnIndex("boothname")>-1)
		this.boothName = cursor.getString(cursor.getColumnIndex("boothname"));
		if(cursor.getColumnIndex("mapname")>-1)
		this.mapName = cursor.getString(cursor.getColumnIndex("mapname"));
	}
	public ArrayList<Corner> getCorners(){
		return this.corners;
	}
	public void setBoothName(String name){
		this.boothName = name;
	}
	public void addPoint(int x, int y){
		corners.add(new Corner(x,y));
	}
	public int size(){
		return corners.size();
	}
    public boolean contains(int x, int y){
    	int numIntersects = 0;

    	//these are values that are impossible to reach for viable geoPoints.  This was done to
    	//ensure that the seed points were not already within a polygon which would invert the desired results
    	double x0 = -1; //long x
    					//lat y
    	double y0 = -1;
    	for(Edge polyEdge : getEdges()){
    		double x2 = polyEdge.start.x;
    		double x3 = polyEdge.end.x;
    		double y2 = polyEdge.start.y;
    		double y3 = polyEdge.end.y;
    		if(intersects(x0,y0,x,y,x2,y2,x3,y3))
    			numIntersects += 1;
    	}
    	if(numIntersects%2 != 0)
    		return true;
    	return false;
    }
	
	public ContentValues toContentValues(){
		ContentValues c = new ContentValues();
		c.put("boothname", this.boothName);
		c.put("mapname", this.mapName);
		return c;
	}
	
	public String toJson(){
		String output = "{\"pk_id\":\""+this.pk_id+"\",\"boothName\":\""+this.boothName+"\", \"mapName\":\""+this.mapName+"\",\"locations\":[";
		boolean first = true;
		for(Corner corner : corners){
			if(!first){
				output += ",";
			}
			output += "{\"x\":\""+corner.x+"\",\"y\":\""+corner.y+"\"}";
			first = false;
		}
		output += "]}";
		return output;
	}
	public void fromJson(JSONObject json){
		try {
			if(json.has("pk_id"))
				this.pk_id=json.getInt("pk_id");
			if(json.has("boothName"))
				this.boothName=json.getString("boothName");
			if(json.has("mapName"))
				this.mapName=json.getString("mapName");
			if(json.has("locations")){
				JSONArray locations = json.getJSONArray("locations");
				for(int i = 0; i < locations.length();i++){
					JSONObject corner = locations.getJSONObject(i);
					if(corner.has("x") && corner.has("y"))
						this.addPoint(corner.getInt("x"), corner.getInt("y"));
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
	public pointsToContentValues(int boothId){
		
		ContentValues c = new ContentValues();
		c.put("fk_boothid", boothId);
		c.put("mapname", this.mapName);
		return c;
	}

	location.put("pk_id", "INTEGER NOT NULL PRIMARY KEY");
	location.put("fk_boothid", "INTEGER NOT NULL");
	location.put("sequence", "INTEGER NOT NULL");
	location.put("x", "INTEGER NOT NULL");
	location.put("y", "INTEGER NOT NULL"
	*/

	private ArrayList<Edge> getEdges(){
		ArrayList<Edge> edges = new ArrayList<Edge>();
		if(corners.size() > 2){
			Corner previous = null;
			for(Corner corner : corners){
				if(previous == null){
					Corner a = corners.get(this.corners.size()-1);
					Corner b = corner;
					Edge edge = new Edge(a,b);
					edges.add(edge);
				}
				else{
					Corner a= previous;
					Corner b= corner;
					Edge edge = new Edge(a,b);
					edges.add(edge);
				}
				previous = corner;
			}
		}
		return edges;
	}
	
    private boolean intersects(double x1,double y1, double x2, double y2,double x3,double y3, double x4, double y4){
    	double x12 = x1 - x2;
    	double x34 = x3 - x4;
    	double y12 = y1 - y2;
    	double y34 = y3 - y4;

    	double c = x12 * y34 - y12 * x34;

    	if (c != 0){
    	  double a = x1 * y2 - y1 * x2;
    	  double b = x3 * y4 - y3 * x4;
    	  
    	  double x = (a * x34 - b * x12) / c;
    	  double y = (a * y34 - b * y12) / c;
    	  if(inSegment(x3,x4,x)&&inSegment(y3,y4,y))
    		  if(inSegment(x1,x2,x)&&inSegment(y1,y2,y)){
    			  return true;
    		  }
    	}
    	return false;
    }
    private boolean inSegment(double x1,double x2, double x){
    	if(x1 >= x){
    		if(x2 <= x){
    			return true;
    		}
    	}
    	if(x1 <= x){
       		if(x2 >= x){
       			return true;
       		}
    	}
    	return false;
    }

	private class Edge{
		Corner start;
		Corner end;
		Edge(Corner start, Corner end){
			this.start = start;
			this.end = end;
		}
	}
}
