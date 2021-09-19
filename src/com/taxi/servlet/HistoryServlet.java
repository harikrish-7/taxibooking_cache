package com.taxi.servlet;

import java.util.List;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.taxi.*;
public class HistoryServlet extends HttpServlet
{
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		int history_taxi=Integer.parseInt(request.getParameter("taxino"));

		out.print("<h1>History</h1><br>");
		out.print("<h3>History of Taxi number "+history_taxi+"</h3><br>");

		int i=0;

		TaxisData taxiData;
		try{
			taxiData = GeneralData.getInstance().taxi.get(history_taxi-1);
		}
		catch(IndexOutOfBoundsException ex)
		{
			taxiData = null;
		}

		if(taxiData!=null)
		{
			List<Booking_data> History=taxiData.History;
			for(Booking_data bookingData : History)
			{
				int pick_time = bookingData.pick_time;
				int drop_time = bookingData.drop_time;
				out.print((i+1)+") Picked "+bookingData.name+" from "+bookingData.pick_loc+" to "+bookingData.drop_loc+" and billed "+bookingData.cost);
				out.print(". Picked at "+pick_time/60+" hr "+pick_time%60+" min and Dropped at "+drop_time/60+" hr "+drop_time%60+" min<br>");
				i++;
			}
			out.print("<br><b>Net Amount earned = "+taxiData.earning+"</b><br>");
		}

		if(i==0){
			out.print("<b>No trips thus far!</b>");
		}

		RequestDispatcher rd=request.getRequestDispatcher("bookorhistory.html");
		rd.include(request, response);
	}

}