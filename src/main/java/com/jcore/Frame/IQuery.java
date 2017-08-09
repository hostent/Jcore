package com.jcore.Frame;



public interface IQuery<T> {
	

    

    T Get(Object id);

    T GetUnique(Object unique);

}
