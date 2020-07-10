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
	
	@Autowired
	private BoardRepository boardRepository;

	
	@Override
	public BoardResponseEntity selectBoardList(int page, HttpServletRequest request) {
		BoardResponseEntity poongduckResponseEntity = new BoardResponseEntity();
		Page<BoardEntity> boardPage = boardRepository.findAll(PageRequest.of(page-1, FIVE, DESC, ID));
		poongduckResponseEntity.setBoard_list(boardPage.getContent());
		
		if(boardPage.hasPrevious()) {
			poongduckResponseEntity.setPrevious_page(request.getRequestURL().toString() + (page - 1));
		}
		
		if(boardPage.hasNext()) {
			poongduckResponseEntity.setAfter_page(request.getRequestURL().toString() + (page + 1));
		}
		
		return poongduckResponseEntity;
	}

	@Override
	public BoardResponseEntity saveBoard(BoardEntity board) {
		BoardResponseEntity poongduckResponseEntity = new BoardResponseEntity();
		
		boardRepository.save(board);
		poongduckResponseEntity.setContent(board);
		
		return poongduckResponseEntity;
	}

	@Override
	public BoardResponseEntity selectBoardDetail(int id) {
		BoardResponseEntity poongduckResponseEntity = new BoardResponseEntity();
		
		poongduckResponseEntity.setContent(boardRepository.findById(id).get());
		
		return poongduckResponseEntity;
	}
}
