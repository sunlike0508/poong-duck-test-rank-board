package poongduck.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import poongduck.board.entity.BoardEntity;

public interface BoardRepository extends CrudRepository<BoardEntity, Integer>{

	Page<BoardEntity> findAll(Pageable pageable);
}
