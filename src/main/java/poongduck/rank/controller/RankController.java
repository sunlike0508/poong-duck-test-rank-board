package poongduck.rank.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import poongduck.rank.entity.RankUser;
import poongduck.rank.service.RankService;

@RestController
public class RankController {
	
	@Autowired
	private RankService rankService;
	
	public static final String RANK = "/rank";

	@GetMapping(value=RANK)
	public List<RankUser> getRankList() throws Exception{
		
		return rankService.getRankList();
	}
}
