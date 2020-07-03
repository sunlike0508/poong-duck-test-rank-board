package poongduck.rank.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import poongduck.rank.entity.RankUser;

@Service
public class RankServiceImpl implements RankService {

	@Override
	public List<RankUser> getRankList() {
		RankUser rankUser = new RankUser();
		rankUser.setId("1");
		rankUser.setNickname("sunlike0301");
		
		List<RankUser> rankList = new ArrayList<RankUser>();
		rankList.add(rankUser);
		
		return rankList;
	}

}
