package com.su.data.rmi;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.su.common.rmi.DataRmiService;

@Service
public class DataRmiServiceImpl implements DataRmiService {

	@Override
	public void save() {
		System.out.println("rmi invoke save");
	}

}
