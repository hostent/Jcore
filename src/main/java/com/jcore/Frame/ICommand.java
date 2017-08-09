package com.jcore.Frame;

public interface ICommand<T> {
	
    int Delete(Object id);

    int Update(T t);

	Object Add(T t);
}
