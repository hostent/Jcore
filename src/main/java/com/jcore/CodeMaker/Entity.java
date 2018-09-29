package com.jcore.CodeMaker;

 
import java.util.List;

public class Entity {
	
	private String name;
	
	private List<Column> cols;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Column> getCols() {
		return cols;
	}

	public void setCols(List<Column> cols) {
		this.cols = cols;
	}

 
	
/*	private void tt()
	{
		for (String key : cols.keySet()) {
			cols.get(key);
		}
	}*/

}
