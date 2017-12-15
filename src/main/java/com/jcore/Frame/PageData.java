package com.jcore.Frame;

import java.util.*;

public class PageData<T> {
	
	public int total;
    public List<T> rows;
    

    

    public PageData()
    {
    }

    public PageData(int count, List<T> page)
    {
        total = count;
        rows = page;
    }
}
