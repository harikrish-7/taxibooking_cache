package com.taxi;

public class Booking_data
{
	public String  name;
	public int pick_loc,drop_loc,start_time,pick_time,drop_time,cost,taxi_alloted;

	public Booking_data(String nme,int pickloc,int droploc,int starttime,int picktime,int droptime,int cst,int taxialloted)
	{
		name=nme;
		pick_loc=pickloc;
		drop_loc=droploc;
		start_time=starttime;
		pick_time=picktime;
		drop_time=droptime;
		cost=cst;
		taxi_alloted=taxialloted;
	}
}