package poongduck.board.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.javacrumbs.jsonunit.core.Option;
import poongduck.board.entity.BoardEntity;
import poongduck.board.repository.BoardRepository;
import poongduck.board.service.BoardService;
import poongduck.response.entity.PoongduckResponseEntity;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.mysql.MySqlConnection;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
class BoardControllerTest {
	
	private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), 
																MediaType.APPLICATION_JSON.getSubtype(), 
																Charset.forName("UTF-8"));

	private static final String APPLICATION_PROPERTIES = "application.properties";
	
	private static final String DRIVER_CLASS_NAME = "spring.datasource.driver-class-name";
	private static final String DRIVER_CLASS_URL = "spring.datasource.url";
	private static final String DRIVER_CLASS_USERNAME = "spring.datasource.username";
	private static final String DRIVER_CLASS_PASSWORD = "spring.datasource.password";
	private static final String DRIVER_CLASS_SCHEMA = "spring.datasource.hikari.schema";
	
	private static final String JSON_IGNORE_ID = "board_list[*].id";
	private static final String JSON_IGNORE_CREATE_AT = "board_list[*].create_at";
	private static final String JSON_IGNORE_UPDATE_AT = "board_list[*].update_at";
	
	private static final String EXPECTED_BOARD_LIST_JSON = "expected_board_list.json";
	private static final String EXPECTED_BOARD_LIST_JSON_02 = "expected_board_list_02.json";
	private static final String EXPECTED_BOARD_WRITE_JSON = "expected_board_write.json";
	
	private static final String BOARD_XMl = "src/main/resources/Board.xml";
	private static final String BOARD_XMl_02 = "src/main/resources/Board_02.xml";
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	BoardService boardService;

	@Autowired
	MockMvc mockMvc;

	IDatabaseConnection iDatabaseConnection;

	@BeforeEach
	public void setUp() throws ClassNotFoundException, DatabaseUnitException, SQLException, IOException{
		Properties properties = new Properties();
		properties.load(getClass().getClassLoader().getResourceAsStream(APPLICATION_PROPERTIES));
		
		final String dirver = properties.getProperty(DRIVER_CLASS_NAME);
		final String url = properties.getProperty(DRIVER_CLASS_URL);
		final String username = properties.getProperty(DRIVER_CLASS_USERNAME);
		final String passward = properties.getProperty(DRIVER_CLASS_PASSWORD);
		final String schema = properties.getProperty(DRIVER_CLASS_SCHEMA);
		
		Class.forName(dirver);
		iDatabaseConnection = new MySqlConnection(DriverManager.getConnection(url, username, passward), schema);
		iDatabaseConnection.getConfig().setProperty(DatabaseConfig.FEATURE_QUALIFIED_TABLE_NAMES, false);
    }

	@AfterEach
	public void tearDown() throws SQLException {
		if(!iDatabaseConnection.getConnection().isClosed()) {
			iDatabaseConnection.close();
		}
	}
	
	@Test
	@DisplayName("게시글 리스트 출력 메소드 테스트. 총 데이터 2개이고 page 첫 번째일 경우 기대 값 expected_board_list.json")
	public void Given_BoardDataTwoAndPage1_When_openBoardList_Then_expected_board_list_json() throws Exception {
		
		setDataBase(BOARD_XMl);
		
    	//when
    	MvcResult mockMVcResult = mockMvc.perform(get(BoardController.BOARD_LIST_URL + "0")
						    			.accept(APPLICATION_JSON_UTF8))
						    			.andExpect(status().isOk())
						    			.andDo(print())
						    			.andReturn();
		
    	//then
		assertThatJson(mockMVcResult.getResponse().getContentAsString())
						.whenIgnoringPaths(JSON_IGNORE_CREATE_AT, JSON_IGNORE_UPDATE_AT)
						.when(Option.IGNORING_ARRAY_ORDER)
						.isEqualTo(objectMapper.readValue(
								getClass().getClassLoader().getResourceAsStream(EXPECTED_BOARD_LIST_JSON), PoongduckResponseEntity.class));
    }
	
	@Test
	@DisplayName("게시글 리스트 출력 메소드 테스트. 총 데이터 12개이고 page 첫 번째일 경우 기대값 expected_board_list_02.json")
	public void Given_BoardData12AndPage1_When_openBoardList_Then_expected_board_list_02_json() throws Exception {
		
		setDataBase(BOARD_XMl_02);
		
    	//when
    	MvcResult mockMVcResult = mockMvc.perform(get(BoardController.BOARD_LIST_URL + "0")
						    			.accept(APPLICATION_JSON_UTF8))
						    			.andExpect(status().isOk())
						    			.andDo(print())
						    			.andReturn();
		
    	//then
		assertThatJson(mockMVcResult.getResponse().getContentAsString())
						.whenIgnoringPaths(JSON_IGNORE_CREATE_AT, JSON_IGNORE_UPDATE_AT)
						.when(Option.IGNORING_ARRAY_ORDER)
						.isEqualTo(objectMapper.readValue(
								getClass().getClassLoader().getResourceAsStream(EXPECTED_BOARD_LIST_JSON_02), PoongduckResponseEntity.class));
    }

	@Test
	@DisplayName("게시글 작성 메소드 테스트")
	public void testWriteBoard() throws Exception {
		setDataBase(BOARD_XMl);
		
		//given
		BoardEntity givenBoardEntity = makeGivenBoardEntity();
		
		//when
		mockMvc.perform(post(BoardController.BOARD_WRITE_URL)
					.content(objectMapper.writeValueAsString(givenBoardEntity))
					.contentType(APPLICATION_JSON_UTF8))
					.andExpect(status().isCreated())
	    			.andDo(print())
	    			.andReturn();
		
		PoongduckResponseEntity actualPoongduckResponseEntity = boardService.selectBoardList(0);
		//expected
		PoongduckResponseEntity expectedPoongduckResponseEntity = new ObjectMapper().readValue(
    			getClass().getClassLoader().getResourceAsStream(EXPECTED_BOARD_WRITE_JSON), PoongduckResponseEntity.class);
    	
    	//then
		assertThatJson(actualPoongduckResponseEntity)
						.whenIgnoringPaths(JSON_IGNORE_ID, JSON_IGNORE_CREATE_AT, JSON_IGNORE_UPDATE_AT)
						.when(Option.IGNORING_ARRAY_ORDER)
						.isEqualTo(expectedPoongduckResponseEntity);
	}
	@Disabled
	@Test
	@DisplayName("게시글 상세 내용 출력 메소드 테스트")
	public void testGetBoardDetail() throws Exception {
		
		mockMvc.perform(get(BoardController.BOARD_DETAIL + "1")
						.accept(APPLICATION_JSON_UTF8))
						.andExpect(status().isOk())
						.andExpect(jsonPath("$.id", is(1)))
						.andExpect(jsonPath("$.user_id", is("sunlike0301")))
						.andExpect(jsonPath("$.title", is("최강 삼성 승리하리라~")))
						.andExpect(jsonPath("$.contents", is("내 목숨을 아이어에")))
						.andDo(print());
    }
	
	public void setDataBase(String databaseXML)
			throws DatabaseUnitException, SQLException, MalformedURLException, DataSetException {
		DatabaseOperation.CLEAN_INSERT.execute(iDatabaseConnection, new FlatXmlDataSetBuilder().build(new File(databaseXML)));
	}

	public BoardEntity makeGivenBoardEntity() {
		BoardEntity givenBoardEntity = new BoardEntity();
		givenBoardEntity.setUser_id("sunlike0303");
		givenBoardEntity.setTitle("LG 잡고 5위로 가즈아~");
		givenBoardEntity.setContents("원태인 삼성의 황태자~~~!!!");
		
		return givenBoardEntity;
	}

}
