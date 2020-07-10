package poongduck.rank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import poongduck.rank.service.RankService;
import poongduck.response.entity.RankResponseEntity;

@Api("랭크 게시판 REST API")
@CrossOrigin
@RestController
public class RankController {
	
	@Autowired
	private RankService rankService;
	
	public static final String RANK = "/rank";

	@ApiOperation(value = "랭크 조회")
	@GetMapping(value=RANK)
	public RankResponseEntity getRankList() throws Exception{
		
		return rankService.getRankList();
	}
}
