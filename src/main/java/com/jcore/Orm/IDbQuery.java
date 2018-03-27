package com.jcore.Orm;

import java.util.List;

 
public interface IDbQuery<T> {

	IDbQuery<T> Where(String exp,Object... par);

	IDbQuery<T> OrderBy(String exp);

	IDbQuery<T> OrderByDesc(String exp);

	IDbQuery<T> Limit(int form, int length);

	IDbQuery<T> Distinct();   

    T First();
    
    List<T> ToList();

    long Count();

    boolean Exist();
    
//    <E> E  FirstSelect(Class<E> cla);
//    
//    <E> List<E> ToListSelect(Class<E> cla);
//    
    
}
