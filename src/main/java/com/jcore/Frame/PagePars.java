package com.jcore.Frame;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

public class PagePars {
	
	 public PagePars()
     {
		 Pars = new Hashtable<String, Object>();
     }
	 
	 public PagePars(HttpServletRequest request)
     {
		 Pars = new Hashtable<String, Object>();
		 
		 if(request.getParameter("pageSize")!=null)
		 {
			 PageSize = Integer.parseInt(request.getParameter("pageSize"));
		 }
		 if(request.getParameter("pageIndex")!=null)
		 {
			 PageIndex = Integer.parseInt(request.getParameter("pageIndex"));
		 }
		 if(request.getParameter("order")!=null)
		 {
			 Order = request.getParameter("order");
		 }
		 
     }

     public Hashtable<String, Object> Pars;

     public int PageSize;

     public int PageIndex;

     public String Order;
}
