package com.su.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.su.core.data.DataService;

@Service
public class AccountService {
	@Autowired
	private DataService dataService;
	
	
}
