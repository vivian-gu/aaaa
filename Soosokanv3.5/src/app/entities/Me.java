package app.entities;

import java.sql.Date;

import android.app.Application;

public class Me extends Application {
	
	private String name;
	private String _id;
	private String password; //null
	private double longtitude;
	private double latitude;
	private String email;
	private String telNumber;
	private Date expireDate;//Date time= new java.sql.Date(new java.util.Date().getTime());
	private String description;
	
	public Me() {
		super();
	}
	
	
	

}


