package poongduck.board.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import poongduck.board.entity.BoardEntity;

@Service
public class BoardServiceImpl implements BoardService{
	
	@Override
	public List<BoardEntity> selectBoardList() {
    	BoardEntity be = new BoardEntity();
		be.setId(1);
		be.setUser_id("sunlike0301");
		be.setContents("내 목숨을 호드에");
		List<BoardEntity> list = new ArrayList<BoardEntity>();
		list.add(be);
		
		return list;
	}
}
