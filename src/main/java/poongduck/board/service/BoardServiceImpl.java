package poongduck.board.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import poongduck.board.entity.BoardEntity;
import poongduck.board.repository.BoardRepository;

@Service
public class BoardServiceImpl implements BoardService{
	
	@Autowired
	private BoardRepository boardRepository;

	
	@Override
	public List<BoardEntity> selectBoardList() {
		
		return boardRepository.findAllByOrderByIdDesc();
	}
}
