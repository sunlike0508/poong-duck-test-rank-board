package poongduck.board.service;

import javax.servlet.http.HttpServletRequest;

import poongduck.board.entity.BoardEntity;
import poongduck.response.entity.BoardResponseEntity;


public interface BoardService {

	BoardResponseEntity selectBoardList(int page, HttpServletRequest request);

	BoardResponseEntity saveBoard(BoardEntity board);

	BoardResponseEntity selectBoardDetail(int id);

}
