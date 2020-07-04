package poongduck.rank.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import poongduck.rank.entity.RankUser;
import poongduck.rank.repository.RankRepository;

@Service
public class RankServiceImpl implements RankService {
	
	@Autowired
	private RankRepository rankRepository;

	@Override
	public List<RankUser> getRankList() {
		
		return rankRepository.findAllByOrderByPointDesc();
	}

}
