package com.jcore.Frame;

import java.util.*;

public class PageData<T> {
	
	public long total;
    public List<T> rows;
    

    

    public PageData()
    {
    }

    public PageData(long count, List<T> page)
    {
        total = count;
        rows = page;
    }
}
