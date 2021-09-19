package com.taxi;

import com.taxi.Booking_data;
import java.util.List;
import java.util.ArrayList;

public class TaxisData
{
	public int taxino,earning,booking_count;
	public List<Booking_data> History=new ArrayList<>();

	public TaxisData(int num)
	{
		taxino=num;
		earning=0;
		booking_count=0;
	}
}