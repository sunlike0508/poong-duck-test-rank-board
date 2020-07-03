package poongduck.rank.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import poongduck.rank.entity.RankUser;

public interface RankRepository extends CrudRepository<RankUser, Integer>{
	List<RankUser> findAllByOrderByPointDesc();
}
