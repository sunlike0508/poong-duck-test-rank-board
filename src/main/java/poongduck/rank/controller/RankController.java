package poongduck.rank.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import poongduck.rank.entity.RankUser;

@RestController
public class RankController {
	
	public static final String RANK = "/rank";

	@GetMapping(value=RANK)
	public List<RankUser> getRankList() throws Exception{
		
		RankUser rankUser = new RankUser();
		rankUser.setId("sunlike0301");
		
		List<RankUser> rankList = new ArrayList<RankUser>();
		rankList.add(rankUser);
		
		return rankList;
	}
}
