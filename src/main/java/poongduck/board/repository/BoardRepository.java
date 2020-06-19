package poongduck.board.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import poongduck.board.entity.BoardEntity;

public interface BoardRepository extends CrudRepository<BoardEntity, Integer>{

	List<BoardEntity> findAllByOrderByIdDesc();
}
