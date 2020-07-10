package poongduck.rank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import poongduck.rank.service.RankService;
import poongduck.response.entity.RankResponseEntity;

@CrossOrigin
@RestController
public class RankController {
	
	@Autowired
	private RankService rankService;
	
	public static final String RANK = "/rank";

	@GetMapping(value=RANK)
	public RankResponseEntity getRankList() throws Exception{
		
		return rankService.getRankList();
	}
}
