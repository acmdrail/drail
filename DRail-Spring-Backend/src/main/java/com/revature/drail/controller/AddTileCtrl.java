package com.revature.drail.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.revature.drail.beans.DrailRail;
import com.revature.drail.beans.DrailStation;
import com.revature.drail.beans.DrailTile;
import com.revature.drail.beans.DrailURS;
import com.revature.drail.beans.DrailUser;
import com.revature.drail.beans.DrailUserRole;
import com.revature.drail.dto.DrailTileDTO;
import com.revature.drail.service.AddTileService;
import com.revature.drail.service.GetStationService;
import com.revature.drail.service.GetURSService;

/**
 * 
 * @author Allan Poindexter
 *
 */

@RestController
public class AddTileCtrl {
	
	@Autowired
	AddTileService tsService;
	
	@Autowired
	GetStationService stnService;
	
	@Autowired
	GetURSService ursService;
	
	/**
	 * Adds a tile to a rail
	 * @param dto - The tile to be created. Must send the current Rail ID
	 * @return 401 if the currentUser is null and if the currentUser isn't the role of SCRUM_MASTER;
	 * 201 if the tile was created successfully;
	 * or 409 if there was a conflict.
	 * 
	 */
	@PostMapping("/addtile")
	public ResponseEntity<DrailTile> addTile(@RequestBody DrailTileDTO dto, HttpSession session) {
		
		DrailUser currentUser = (DrailUser)session.getAttribute("user");
		if (currentUser == null) return new ResponseEntity<DrailTile>(HttpStatus.UNAUTHORIZED);
		
		//Use the current Rail to get the Station
		DrailStation station = stnService.getStationByRail(dto.getRailId());
		
		//Now using the Station and the User, get the user's role.
		DrailURS urs = ursService.getStationURS(currentUser, station);
		
		
		if (urs.getRole().getId() != DrailUserRole.SCRUM_MASTER.getId()) {
			return new ResponseEntity<DrailTile>(HttpStatus.UNAUTHORIZED);
		}
			
		try {
			DrailTile tile = new DrailTile();
			tile.setName(dto.getName());
			tile.setPoints(dto.getPoints());
			tile.setNote(dto.getNote());
			tile.setOrder(dto.getOrder());
			
			DrailRail rail = new DrailRail();
			rail.setRailId(dto.getRailId());
			tile.setRail(rail);
			
			tile.setUserCheckedOut(currentUser);
			tile.setCompleted(0);
			
			tile = tsService.addTile(tile);
			
			//Display the new tile somewhere?
			return new ResponseEntity<DrailTile>(HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<DrailTile>(HttpStatus.CONFLICT);
		}
	}
}
