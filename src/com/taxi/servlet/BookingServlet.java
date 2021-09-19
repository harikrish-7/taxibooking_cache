package com.taxi.servlet;

import java.util.*;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.taxi.*;

public class BookingServlet extends HttpServlet
{
	int itr=0,taxi_count;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		GeneralData generalDataObj = GeneralData.getInstance();

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		ArrayList<TaxisData> taxi = generalDataObj.taxi;

		if(itr<generalDataObj.taxi_count)
		{
			taxi.add(new TaxisData(itr));
			taxi_count=(itr+1);
		}
		itr++;

		String pick_up_time = request.getParameter("pick_up_time");
		int colonIndex = pick_up_time.indexOf(':');
		int hour=Integer.parseInt(pick_up_time.substring(0,colonIndex));
		int min=Integer.parseInt(pick_up_time.substring(colonIndex+1));
		int pick=Integer.parseInt(request.getParameter("pickloc"));
		int drop=Integer.parseInt(request.getParameter("droploc"));
		String name=request.getParameter("name");

		int time_int=min+hour*60;
		int travel_time=Math.abs(pick-drop)*15;
		
		if(travel_time != 0)
		{
			List<Integer> available=new ArrayList<Integer>();
			int present_loc[]=new int[taxi_count];

			for(int i=0;i<taxi_count;i++)
			{
				int ji;
				int presentloc=1,max=-1;
				for(ji=0;ji<taxi.get(i).booking_count;ji++)
				{
					if(time_int>(taxi.get(i).History.get(ji).drop_time))
					{
						if((taxi.get(i).History.get(ji).drop_time)>max)
						{													//Getting location of taxis at time of current booking
							max=taxi.get(i).History.get(ji).drop_time;
							presentloc=taxi.get(i).History.get(ji).drop_loc;
						}
					}
				}
				present_loc[i]=presentloc;
				System.out.println("Present location of "+i+" is "+present_loc[i]);
			}

			int mini_dist=15*(generalDataObj.locat_idx_end-generalDataObj.locat_idx_start)+1;

			for(int i=0;i<taxi_count;i++)									//Checking for free taxis
			{
				int j,lapse;
				lapse=Math.abs(pick-present_loc[i])*15;

				if(lapse<mini_dist){
					mini_dist=lapse;
				}
				for(j=0;j<taxi.get(i).booking_count;j++)
				{
					int droptime=taxi.get(i).History.get(j).drop_time;
					int starttime=taxi.get(i).History.get(j).start_time;
					int curnt_start=time_int-lapse;
					int curnt_end=time_int+travel_time;
					if((time_int<=droptime)&&(time_int>=starttime))
						break;
					else if((curnt_start<=droptime)&&(curnt_start>=starttime))
						break;
					else if((curnt_end  <=droptime)&&(curnt_end  >=starttime))
						break;
					else if((droptime  <=curnt_end)&&(droptime   >=curnt_start))
						break;
					else if((starttime<=curnt_end )&&(starttime  >=curnt_start))
						break;
				}
				if(j==taxi.get(i).booking_count){
					available.add(i);
				}
			}
			int jour_spend=mini_dist*generalDataObj.penalty;
			int jour_cost=travel_time*generalDataObj.charge;
			if(available.size()==0)
			{
				out.print("Oops! \nTaxi unavailable at specified time and place \n");
			}
			else
			{
				int alloted_taxi;
				if(available.size()==1)
				{
					alloted_taxi=available.get(0);
				}
				else
				{
					for(int i=0;i<available.size();i++)
					{
						int dist=(present_loc[available.get(i)]-pick)*15;
						if(dist<0){
							dist*=-1;										//Retaining mini dist only
						}
						if(dist>mini_dist)
						{
							available.remove(i);
							i--;
						}
					}
					System.out.println("Min dist is "+mini_dist+" and mini_dist taxis are "+available);

					if(available.size()==1)
					{
						alloted_taxi=available.get(0);
					}
					else
					{
						int min_earn=taxi.get(available.get(available.size()-1)).earning;
						int ans=0;
						for(int i=available.size()-1;i>=0;i--)
						{
							int currentAvailableElement = available.get(i);
							TaxisData currentAvailableTaxiObj = taxi.get(currentAvailableElement);
							if(currentAvailableTaxiObj.earning<=min_earn)
							{
								min_earn=currentAvailableTaxiObj.earning;
								ans=currentAvailableElement;
							}
						}
						alloted_taxi=ans;
					}
				}
				out.print("Alloted Taxi is Taxi number "+(alloted_taxi+1)+" \n");
				out.print("The price of the journey is "+jour_cost);

				TaxisData allotedTaxiObj = taxi.get(alloted_taxi);
				allotedTaxiObj.earning+=(jour_cost-jour_spend);
				allotedTaxiObj.booking_count+=1;
				allotedTaxiObj.History.add(new Booking_data(name,pick,drop,(time_int-mini_dist),time_int,(time_int+travel_time),jour_cost,alloted_taxi));
				generalDataObj.booking_count+=1;
			}
		}
		else{
			out.print("Oops!<br>Invalid Entry!");
		}
		RequestDispatcher rd=request.getRequestDispatcher("bookorhistory.html");
		rd.include(request, response);
	}

}