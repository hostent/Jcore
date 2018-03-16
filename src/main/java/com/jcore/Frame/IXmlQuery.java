package com.jcore.Frame;

import java.util.*;


public interface IXmlQuery<T> {
	List<T> QueryXml(String reportName, Hashtable<String, Object> par);

    PageData<T> QueryXml(String reportName, PagePars param);
    
    List<T> QueryXml(Hashtable<String, Object> par);

    PageData<T> QueryXml(PagePars param);
}
