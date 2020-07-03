package poongduck.rank.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
class RankControllerTest {

	@Autowired
	MockMvc mockMvc;
	
	@Test
	@DisplayName("랭크 리스트 출력 메소드 테스트")
	public void testOpenBoardList() throws Exception {
		
		mockMvc.perform(get(RankController.RANK))
						.andExpect(status().isOk())
						.andExpect(jsonPath("$.[0].id", is(1)))
						.andExpect(jsonPath("$.[0].nickname", is("내 목숨을 아이어에")))
						.andExpect(jsonPath("$.[0].point", is(100)))
						.andExpect(jsonPath("$.[1].id", is(2)))
						.andExpect(jsonPath("$.[1].nickname", is("내 목숨을 호드에")))
						.andExpect(jsonPath("$.[1].point", is(50)))
    			
    			.andDo(print());
	}
}
