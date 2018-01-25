package com.revature.drail.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.revature.drail.dto.DrailChartDTO;
import com.revature.drail.service.GetBChartService;

@RestController
public class ViewBChartCtrl {
	
	@Autowired
	GetBChartService chartServ;
	
	
	
	
	
	@PostMapping("/viewchart")
	public ResponseEntity<DrailChartDTO<Long, Integer>> viewBChart(@RequestBody DrailChartDTO<Long, Integer> chartDto, HttpSession session) {
		
		if (session.getAttribute("user") != null) {
			
			return new ResponseEntity<DrailChartDTO<Long, Integer>>(chartServ.getChartDto(chartDto),HttpStatus.OK);
		} else {
			return new ResponseEntity<DrailChartDTO<Long, Integer>>(HttpStatus.NO_CONTENT);
		}
		
		
	}
	
	

}