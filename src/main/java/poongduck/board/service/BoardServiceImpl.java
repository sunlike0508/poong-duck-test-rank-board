package poongduck.board.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import poongduck.board.entity.BoardEntity;
import poongduck.board.repository.BoardRepository;
import poongduck.response.entity.BoardResponseEntity;

@Service
public class BoardServiceImpl implements BoardService{
	
	private static final String ID = "id";
	private static final int FIVE = 5;
	private static final Direction DESC = Direction.DESC;
	private static final String QUERY_PAGE = "?page=";
	
	@Autowired
	private BoardRepository boardRepository;

	
	@Override
	public BoardResponseEntity selectBoardList(int page, HttpServletRequest request) {
		BoardResponseEntity boardResponseEntity = new BoardResponseEntity();
		Page<BoardEntity> boardPage = boardRepository.findAll(PageRequest.of(page-1, FIVE, DESC, ID));
		boardResponseEntity.setBoard_list(boardPage.getContent());
		
		if(boardPage.hasPrevious()) {
			boardResponseEntity.setPrevious_page(request.getRequestURL().toString() + QUERY_PAGE + (page - 1));
		}
		
		if(boardPage.hasNext()) {
			boardResponseEntity.setAfter_page(request.getRequestURL().toString() + QUERY_PAGE + (page + 1));
		}
		
		return boardResponseEntity;
	}

	@Override
	public BoardResponseEntity saveBoard(BoardEntity board) {
		BoardResponseEntity boardResponseEntity = new BoardResponseEntity();
		
		boardRepository.save(board);
		boardResponseEntity.setContent(board);
		
		return boardResponseEntity;
	}

	@Override
	public BoardResponseEntity selectBoardDetail(int id) {
		BoardResponseEntity boardResponseEntity = new BoardResponseEntity();
		
		boardResponseEntity.setContent(boardRepository.findById(id).get());
		
		return boardResponseEntity;
	}
}
