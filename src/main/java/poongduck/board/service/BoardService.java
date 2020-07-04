package poongduck.board.service;

import java.util.List;

import poongduck.board.entity.BoardEntity;


public interface BoardService {

	List<BoardEntity> selectBoardList();

	void saveBoard(BoardEntity board);

	BoardEntity selectBoardDetail(int id);

}
