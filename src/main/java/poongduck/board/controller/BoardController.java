package poongduck.board.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import poongduck.board.entity.BoardEntity;
import poongduck.board.service.BoardService;

@RestController
public class BoardController {
	
	public static final String BOARD_LIST_URL = "/board";
	public static final String BOARD_WRITE_URL = "/board/write";
	
	@Autowired
	private BoardService boardService;
	
	@GetMapping(BOARD_LIST_URL)
	public List<BoardEntity> openBoardList() throws Exception{
		
		return boardService.selectBoardList();
	}
	
	@PostMapping(path = BOARD_WRITE_URL, produces = "application/json;charset=UTF-8")
	public List<BoardEntity> writeBoard(@RequestBody BoardEntity board) throws Exception{

		boardService.saveBoard(board);
		
		return boardService.selectBoardList();
	}
	
	@GetMapping("/board/detail/{id}")
	public BoardEntity getBoardDetail(@PathVariable(required = true) int id) throws Exception{
		
		BoardEntity board = new BoardEntity();
		board.setId(id);
		board.setUser_id("sunlike0301");
		board.setContents("내 목숨을 아이어에");
		
		return board;
	}
}
