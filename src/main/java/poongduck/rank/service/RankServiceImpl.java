package poongduck.rank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import poongduck.rank.repository.RankRepository;
import poongduck.response.entity.RankResponseEntity;

@Service
public class RankServiceImpl implements RankService {
	
	@Autowired
	private RankRepository rankRepository;

	@Override
	public RankResponseEntity getRankList() {
		
		RankResponseEntity rankResponseEntity = new RankResponseEntity();
		rankResponseEntity.setRanking(rankRepository.findAllByOrderByPointDesc());
		return rankResponseEntity;
	}

}
