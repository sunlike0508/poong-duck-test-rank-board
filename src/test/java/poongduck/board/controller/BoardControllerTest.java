package poongduck.board.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.RequestResultMatchers;
import org.springframework.web.servlet.ModelAndView;

import com.mysql.cj.Session;
import com.mysql.cj.xdevapi.Result;

import net.bytebuddy.agent.VirtualMachine.ForHotSpot.Connection.Response;
import poongduck.board.entity.BoardEntity;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class BoardControllerTest {

	@Autowired
    MockMvc mockMvc;

	@Disabled
    @Test
    @DisplayName("hello controller 작동 테스트")
    public void hello() throws Exception {
        mockMvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello World"))
                .andDo(print());
    }
    
    @Test
    @DisplayName("Board Controller 기본 테스트")
    public void board() throws Exception {
    	BoardEntity be = new BoardEntity();
		be.setId(1);
		be.setUser_id("sunlike0301");
		be.setContents("내 목숨을 아이어에");
		
		List<BoardEntity> list = new ArrayList<BoardEntity>();
		list.add(be);
    	
        mockMvc.perform(get("/board"))
                .andExpect(status().isOk())
                .andExpect(view().name("/board/boardList"))
                .andExpect(model().attributeExists("list"))
                .andExpect(model().attribute("list", list.get(0)))
                .andDo(print());
    }

}
