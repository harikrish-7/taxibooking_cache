package com.taxi;

import java.util.ArrayList;

public class GeneralData
{
	public int charge,penalty,taxi_count,locat_idx_start,locat_idx_end,booking_count;

	public ArrayList<TaxisData> taxi = null;

	private static GeneralData generalDataObj = null;

	private GeneralData()
	{
		charge=10;
		penalty=5;
		locat_idx_start=1;
		locat_idx_end=5;
		taxi_count=5;
		booking_count=0;
		taxi=new ArrayList<TaxisData>();
	}
	public static GeneralData getInstance()
	{
		if(generalDataObj == null){
			generalDataObj = new GeneralData();
		}
		return generalDataObj;
	}
}