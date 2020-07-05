package poongduck.board.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import poongduck.board.entity.BoardEntity;
import poongduck.board.repository.BoardRepository;
import poongduck.response.entity.PoongduckResponseEntity;

@Service
public class BoardServiceImpl implements BoardService{
	
	@Autowired
	private BoardRepository boardRepository;

	
	@Override
	public PoongduckResponseEntity selectBoardList(int page) {
		PoongduckResponseEntity poongduckResponseEntity = new PoongduckResponseEntity();
		
		poongduckResponseEntity.setBoard_list(boardRepository.findAll(PageRequest.of(0, 5, Direction.DESC, "id")));
		
		return poongduckResponseEntity;
	}

	@Override
	public void saveBoard(BoardEntity board) {
		boardRepository.save(board);
	}

	@Override
	public BoardEntity selectBoardDetail(int id) {
		return boardRepository.findById(id).get();
	}
}
