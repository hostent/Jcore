package com.jcore.Frame;


public interface ITran {
	
	void run();
	
	void onError();
	
	void onSucceed();
	
}
