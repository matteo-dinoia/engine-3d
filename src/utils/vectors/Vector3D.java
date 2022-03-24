package utils.vectors;

import java.text.DecimalFormat;

public class Vector3D {
	
	//TODO dico che angolo è radianti
	private double x=0, y=0, z=0, module=0, latitude=0,  longitude=0;
	public Vector3D(double x, double y, double z) {
		this.set(x, y, z);
	}
	public Vector3D(Vector3D vect) {
		this(vect.x, vect.y, vect.z);
	}
	
	
	//VECTORS OPERATION --------------------------------------------------
	public double distance(Vector3D vect) {
		return distance(vect.x, vect.y, vect.z);
	}
	public double distance(double x2, double y2, double z2) {
		return getModule(x2-x, y2-y, z2-z);
	}
	
	public void normalize() {
		double module=getModule();
		this.divide(module);
		
	}
	
	public void sum(Vector3D vect) {
		sum(vect.x, vect.y, vect.z);
	}
	public void sum(double x2, double y2, double z2) {
		this.set(this.x+x2, this.y+y2, this.z+z2);
	}
	public void difference(Vector3D vect) {
		difference(vect.x, vect.y, vect.z);
	}
	public void difference(double x2, double y2,double z2) {
		sum(-x2, -y2, -z2);
	}
	public void divide(double n) {
		multiply(1/n);
	}
	public void multiply(double n) {
		this.set(this.x*n, this.y*n, this.z*n);
	}
	
	public static Vector3D getSummed(Vector3D vect1, Vector3D vect2) {
		if(vect1==null || vect2==null) return null;
		
		Vector3D ris=new Vector3D(vect1);
		ris.sum(vect2);
		
		return ris;
	}
	public static Vector3D getDiffernce(Vector3D vect1, Vector3D vect2) {
		return getSummed(vect1, new Vector3D(-vect2.x, -vect2.y, -vect2.z));
	}
	public static Vector3D getDevided(Vector3D vect1, double n) {
		return getMultiplied(vect1, 1/n);
	}
	public static Vector3D getMultiplied(Vector3D vect, double n) {
		if(vect==null) return null;
		
		Vector3D ris=new Vector3D(vect);
		ris.multiply(n);
		
		return ris;
	}
	private static double getModule(double x, double y, double z) {
		//Pitagora
		return Math.sqrt(x*x+y*y+z*z);
	}
	
	
	//SETTER AND GETTER (+POLAR) -----------------------------------------
	public double getModule() {
		return module;
	}
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public double getZ() {
		return z;
	}
	public double getLatitude() {
		return latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setX(double x) {
		set(x, this.y, this.z);
	}
	public void setY(double y) {
		set(this.x, y, this.z);
	}
	public void setZ(double z) {
		set(this.x, this.y, z);
	}
	public void set(Vector3D v) {
		set(v.x, v.y, v.z);
	}
	//Angle TODO CHECK PLS!!!!  e ottimizzo
	public void set(double x, double y, double z) {
		/*if(Double.isNaN(x) || Double.isNaN(y) || Double.isNaN(z)) {
			System.err.println("NaN in set");
			System.exit(-1);
		}*/
		this.x=x;
		this.y=y;
		this.z=z;
		
		this.module=getModule(x, y, z);
		
		
		this.longitude=getLongitude(x, y, z); //hotizontal
		this.latitude=getLatitude(x, y, z); //vertical
		
		/*if(Double.isNaN(longitude) || Double.isNaN(latitude)) {
			System.err.println("NaN in setByLatLon setting latLon");
			System.exit(-1);
		}*/
	}
	private static double getLatitude(double x, double y, double z) {
		//Use y and module
		double module=getModule(x, y, z);
		if(module==0) return 0; //error handling
		
		return Math.asin(y/module);
	}
	private static double getLongitude(double x, double y, double z) {
		//Cases with vertital line
		double module=getModule(x, y, z), ris=0, cosLat=0;
		if(module==0) return 0; //error handling
		
		//cosLat=Math.sqrt(1-y/module);
		cosLat=Math.cos(getLatitude(x, y, z));
		
		ris=Math.acos(x/(module *cosLat));
		
		if(!Double.isNaN(ris)) {
			if(z<0)ris*=-1;
			return ris;
		}
		else {
			if(z==0) return 0;
			else if(x==0) {
				if(z>=0) return Math.PI/2; 
				else return -Math.PI/2; 
			}
			else if(y==0) {
				ris=Math.atan(z/x);
				if(x<0) ris+=Math.PI;
				return ris;
			}
			else return 0;
		}
	}
	
	public void rotateLongitude(double angle) {
		if(angle==0) return;
		
		double lon=angle+getLongitude();
		double lat=getLatitude();
		
		this.setByLatLon(lat, lon);
	}
	public void rotateLatitude(double angle) {
		if(angle==0) return;
		
		double lat=angle+getLatitude(); //TODO FULL ROTATION
		if(lat>=Math.PI/2 || lat<=-Math.PI/2) return;
		double lon=getLongitude();
		
		this.setByLatLon(lat, lon);
	}
	private void setByLatLon(double lat, double lon) {
		/*if(Double.isNaN(lat)|| Double.isNaN(lon)) {
			System.err.println("NaN in setByLatLon");
			System.exit(-1);
		}*/
		double x=module*Math.cos(lat)*Math.cos(lon);
		double y=module*Math.sin(lat);
		double z=module*Math.cos(lat)*Math.sin(lon);
		
		this.set(x, y, z);
	}
	
	
	//TO-STRING
	private static final DecimalFormat df = new DecimalFormat("0.000");
	public String toString() {
		return "x: "+x+"   y: "+y+"  z:"+z+"\n\t"
				+"module:"+module
				+"   latitude:"+latitude+"   longitude:"+longitude;
	}
	public String toStringAbbrev() {
		return "x: "+df.format(x)+"   y: "+df.format(y)+"  z:"+df.format(z)+"\n\t"
				+"module:"+df.format(module)
				+"   latitude:"+df.format(latitude)+"   longitude:"+df.format(longitude);
	}


}
