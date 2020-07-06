package poongduck.board.service;

import javax.servlet.http.HttpServletRequest;

import poongduck.board.entity.BoardEntity;
import poongduck.response.entity.PoongduckResponseEntity;


public interface BoardService {

	PoongduckResponseEntity selectBoardList(int page, HttpServletRequest request);

	PoongduckResponseEntity saveBoard(BoardEntity board);

	PoongduckResponseEntity selectBoardDetail(int id);

}
