package poongduck.board.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.unitils.reflectionassert.ReflectionComparatorMode;

import poongduck.board.entity.BoardEntity;
import poongduck.board.repository.BoardRepository;
import poongduck.board.service.BoardService;

import static org.unitils.reflectionassert.ReflectionAssert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class BoardControllerTest{

	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	BoardService boardService;
	
	@Autowired
	BoardRepository boardRepository;
	
	@Test
	@DisplayName("openBoardList 메소드 테스트")
	public void test_openBoardList() throws Exception {
		//should
    	List<BoardEntity> expectList = createMockBoardList();
    	
    	//when
    	MvcResult result =mockMvc.perform(get("/board"))
    			.andExpect(status().isOk())
    			.andExpect(view().name("/board/list"))
    			.andExpect(model().attributeExists("boardList"))
    			.andDo(print())
    			.andReturn();
        
    	Object actualList = result.getModelAndView().getModel().get("boardList");

    	assertReflectionEquals(expectList, actualList, ReflectionComparatorMode.LENIENT_DATES);
    }
    
	public List<BoardEntity> createMockBoardList() {
		BoardEntity boardEntity_One = new BoardEntity();
		boardEntity_One.setId(1);
		boardEntity_One.setUser_id("sunlike0301");
		boardEntity_One.setContents("내 목숨을 아이어에");
		
		BoardEntity boardEntity_Two = new BoardEntity();
		boardEntity_Two.setId(2);
		boardEntity_Two.setUser_id("sunlike0302");
		boardEntity_Two.setContents("내 목숨을 호드에");
		
		List<BoardEntity> expectedBoardList = new ArrayList<BoardEntity>();
		expectedBoardList.add(boardEntity_Two);
		expectedBoardList.add(boardEntity_One);
		
		return expectedBoardList;
	}

}
