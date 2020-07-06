package poongduck.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import poongduck.board.entity.BoardEntity;
import poongduck.board.service.BoardService;
import poongduck.response.entity.PoongduckResponseEntity;

@RestController
public class BoardController {
	
	public static final String BOARD_LIST_URL = "/board/";
	public static final String BOARD_WRITE_URL = "/board/write";
	public static final String BOARD_DETAIL = "/board/detail/";
	
	public static final String JSON_UTF8 = "application/json;charset=UTF-8";
	
	private static final String ID = "{id}";
	private static final String PAGE = "{page}";
	
	@Autowired
	private BoardService boardService;

	@GetMapping(BOARD_LIST_URL + PAGE)
	public PoongduckResponseEntity openBoardList(@PathVariable(required = true) int page) throws Exception{
		
		return boardService.selectBoardList(page);
	}

	@PostMapping(path = BOARD_WRITE_URL, produces = JSON_UTF8)
	@ResponseStatus(code = HttpStatus.CREATED)
	public void writeBoard(@RequestBody BoardEntity board) throws Exception{
		
		boardService.saveBoard(board);
	}

	@GetMapping(BOARD_DETAIL + ID)
	public BoardEntity getBoardDetail(@PathVariable(required = true) int id) throws Exception{
		
		return boardService.selectBoardDetail(id);
	}
}
