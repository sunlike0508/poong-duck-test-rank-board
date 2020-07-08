package poongduck.rank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import poongduck.rank.repository.RankRepository;
import poongduck.response.entity.PoongduckResponseEntity;

@Service
public class RankServiceImpl implements RankService {
	
	@Autowired
	private RankRepository rankRepository;

	@Override
	public PoongduckResponseEntity getRankList() {
		
		PoongduckResponseEntity poongduckResponseEntity = new PoongduckResponseEntity();
		poongduckResponseEntity.setRanking(rankRepository.findAllByOrderByPointDesc());
		return poongduckResponseEntity;
	}

}
