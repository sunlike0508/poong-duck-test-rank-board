package poongduck.board.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import poongduck.board.entity.BoardEntity;
import poongduck.board.service.BoardService;
import poongduck.response.entity.PoongduckResponseEntity;

@Api("게시판 REST API")
@CrossOrigin
@RestController
public class BoardController {
	
	public static final String BOARD_LIST_URL = "/board/";
	public static final String BOARD_WRITE_URL = "/board/write";
	public static final String BOARD_DETAIL = "/board/detail/";
	
	private static final String PAGE = "page";
	private static final String ID = "id";
	
	@Autowired
	private BoardService boardService;

	@ApiOperation(value = "게시판 목록 조회")
	@GetMapping(BOARD_LIST_URL)
	public PoongduckResponseEntity openBoardList(@RequestParam(PAGE) int page, HttpServletRequest request) throws Exception{
		
		return boardService.selectBoardList(page, request);
	}

	@ApiOperation(value = "게시글 작성")
	@PostMapping(path = BOARD_WRITE_URL)
	@ResponseStatus(code = HttpStatus.CREATED)
	public PoongduckResponseEntity writeBoard(@RequestBody BoardEntity board) throws Exception{
		
		return boardService.saveBoard(board);
	}

	@ApiOperation(value = "게시글 상세 내용 조회")
	@GetMapping(BOARD_DETAIL)
	public PoongduckResponseEntity getBoardDetail(@RequestParam(ID) int id) throws Exception{
		
		return boardService.selectBoardDetail(id);
	}
}
