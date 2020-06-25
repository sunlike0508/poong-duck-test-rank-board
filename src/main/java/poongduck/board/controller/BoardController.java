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
	
	@Autowired
	private BoardService boardService;
	
	@RequestMapping(value="/board", method=RequestMethod.GET)
	public ModelAndView openBoardList(ModelMap model) throws Exception{
		ModelAndView mv = new ModelAndView("/board/list");

		List<BoardEntity> list = boardService.selectBoardList();
		mv.addObject("list", list);
		
		return mv;
	}
}
