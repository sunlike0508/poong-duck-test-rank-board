package poongduck.board.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import poongduck.board.entity.BoardEntity;
import poongduck.board.repository.BoardRepository;
import poongduck.response.entity.PoongduckResponseEntity;

@Service
public class BoardServiceImpl implements BoardService{
	
	private static final String ID = "id";
	private static final int FIVE = 5;
	private static final Direction DESC = Direction.DESC;
	
	@Autowired
	private BoardRepository boardRepository;

	
	@Override
	public PoongduckResponseEntity selectBoardList(int page) {
		PoongduckResponseEntity poongduckResponseEntity = new PoongduckResponseEntity();
		Page<BoardEntity> boardPage = boardRepository.findAll(PageRequest.of(page-1, FIVE, DESC, ID));
		poongduckResponseEntity.setBoard_list(boardPage.getContent());
		
		if(boardPage.hasPrevious()) {
			poongduckResponseEntity.setPrevious_page("https://localhost:8080/board/" + (page-1));
		}
		
		if(boardPage.hasNext()) {
			poongduckResponseEntity.setAfter_page("https://localhost:8080/board/" + (page+1));
		}
		
		return poongduckResponseEntity;
	}

	@Override
	public PoongduckResponseEntity saveBoard(BoardEntity board) {
		PoongduckResponseEntity poongduckResponseEntity = new PoongduckResponseEntity();
		
		boardRepository.save(board);
		poongduckResponseEntity.setContent(board);
		
		return poongduckResponseEntity;
	}

	@Override
	public BoardEntity selectBoardDetail(int id) {
		return boardRepository.findById(id).get();
	}
}
