package poongduck.board.service;

import java.util.List;

import poongduck.board.entity.BoardEntity;

public interface BoardService {

	List<BoardEntity> selectBoardList();

}
