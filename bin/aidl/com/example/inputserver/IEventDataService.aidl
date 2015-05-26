package com.example.inputserver;


import com.example.inputserver.IEventListener;

interface IEventDataService{

	void SetTouchListener(in IEventListener listener);
	
}