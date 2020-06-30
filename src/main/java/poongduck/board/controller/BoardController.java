package poongduck.board.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import poongduck.board.entity.BoardEntity;
import poongduck.board.service.BoardService;

@Controller
public class BoardController {
	
	public static final String BOARD_LIST_URL = "/board";
	public static final String BOARD_LIST_VIEW  = "/board/list";
	public static final String BOARD_LIST_ATTRIBUTE = "boardList";
	public static final String BOARD_WRITE_URL = "/board/write";
	public static final String BOARD_LIST_REDIRECT_URL = "redirect:/board";
	
	@Autowired
	private BoardService boardService;
	
	@RequestMapping(value=BOARD_LIST_URL, method=RequestMethod.GET)
	public ModelAndView openBoardList(ModelMap model) throws Exception{
		ModelAndView mv = new ModelAndView(BOARD_LIST_VIEW);

		List<BoardEntity> boardList = boardService.selectBoardList();
		mv.addObject(BOARD_LIST_ATTRIBUTE, boardList);
		
		return mv;
	}
	
	@RequestMapping(value=BOARD_WRITE_URL, method=RequestMethod.POST)
	public String writeBoard(BoardEntity board) throws Exception{
		
		boardService.saveBoard(board);
		
		return BOARD_LIST_REDIRECT_URL;
	}
}
