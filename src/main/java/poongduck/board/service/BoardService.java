package poongduck.board.service;

import poongduck.board.entity.BoardEntity;
import poongduck.response.entity.PoongduckResponseEntity;


public interface BoardService {

	PoongduckResponseEntity selectBoardList(int page);

	void saveBoard(BoardEntity board);

	BoardEntity selectBoardDetail(int id);

}
