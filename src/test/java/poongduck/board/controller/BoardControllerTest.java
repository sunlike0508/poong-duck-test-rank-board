package poongduck.board.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.javacrumbs.jsonunit.core.Option;
import poongduck.board.entity.BoardEntity;
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


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
class BoardControllerTest {

	private static final String APPLICATION_PROPERTIES = "application.properties";
	
	private static final String DRIVER_CLASS_NAME = "spring.datasource.driver-class-name";
	private static final String DRIVER_CLASS_URL = "spring.datasource.url";
	private static final String DRIVER_CLASS_USERNAME = "spring.datasource.username";
	private static final String DRIVER_CLASS_PASSWORD = "spring.datasource.password";
	private static final String DRIVER_CLASS_SCHEMA = "spring.datasource.hikari.schema";

	private static final String JSON_IGNORE_CREATE_AT = "$..create_at";
	private static final String JSON_IGNORE_UPDATE_AT = "$..update_at";
	
	private static final String EXPECTED_BOARD_LIST_JSON = "expected_board_list.json";
	private static final String EXPECTED_BOARD_LIST_JSON_01 = "expected_board_list_01.json";
	private static final String EXPECTED_BOARD_LIST_JSON_02 = "expected_board_list_02.json";
	private static final String EXPECTED_BOARD_LIST_JSON_03 = "expected_board_list_03.json";
	
	private static final String BOARD_XMl_DATA_2 = "src/main/resources/Board.xml";
	private static final String BOARD_XMl_DATA_12 = "src/main/resources/Board_01.xml";
	
	public static final String JSON_UTF8 = "application/json;charset=UTF-8";
	
	private static final int FIRST_PAGE = 1;
	private static final int SECOND_PAGE = 2;
	private static final int LAST_PAGE = 3;
	
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
		
		setDataBase(BOARD_XMl_DATA_2);
		
    	//when
    	MvcResult mockMVcResult = mockMvc.perform(get(BoardController.BOARD_LIST_URL + FIRST_PAGE)
						    			.accept(JSON_UTF8))
						    			.andExpect(status().isOk())
						    			.andDo(print())
						    			.andReturn();
		
    	//then
		assertThatJson(actualJson(mockMVcResult))
						.whenIgnoringPaths(JSON_IGNORE_CREATE_AT, JSON_IGNORE_UPDATE_AT)
						.when(Option.IGNORING_ARRAY_ORDER)
						.isEqualTo(expectedJson(EXPECTED_BOARD_LIST_JSON));
    }
	
	@Test
	@DisplayName("게시글 리스트 출력 메소드 테스트. 총 데이터 12개이고 page 첫 번째일 경우 기대값 expected_board_list_01.json")
	public void Given_BoardData12AndPage1_When_openBoardList_Then_expected_board_list_01_json() throws Exception {
		
		setDataBase(BOARD_XMl_DATA_12);
		
    	//when
    	MvcResult mockMVcResult = mockMvc.perform(get(BoardController.BOARD_LIST_URL + FIRST_PAGE)
						    			.accept(JSON_UTF8))
						    			.andExpect(status().isOk())
						    			.andDo(print())
						    			.andReturn();
		
    	//then
		assertThatJson(actualJson(mockMVcResult))
						.whenIgnoringPaths(JSON_IGNORE_CREATE_AT, JSON_IGNORE_UPDATE_AT)
						.when(Option.IGNORING_ARRAY_ORDER)
						.isEqualTo(expectedJson(EXPECTED_BOARD_LIST_JSON_01));
    }
	
	@Test
	@DisplayName("게시글 리스트 출력 메소드 테스트. 총 데이터 12개이고 page 두 번째일 경우 기대값 expected_board_list_02.json")
	public void Given_BoardData12AndPage2_When_openBoardList_Then_expected_board_list_02_json() throws Exception {
		
		setDataBase(BOARD_XMl_DATA_12);
		
    	//when
    	MvcResult mockMVcResult = mockMvc.perform(get(BoardController.BOARD_LIST_URL + SECOND_PAGE)
						    			.accept(JSON_UTF8))
						    			.andExpect(status().isOk())
						    			.andDo(print())
						    			.andReturn();
		
    	//then
		assertThatJson(actualJson(mockMVcResult))
						.whenIgnoringPaths(JSON_IGNORE_CREATE_AT, JSON_IGNORE_UPDATE_AT)
						.when(Option.IGNORING_ARRAY_ORDER)
						.isEqualTo(expectedJson(EXPECTED_BOARD_LIST_JSON_02));
    }
	
	@Test
	@DisplayName("게시글 리스트 출력 메소드 테스트. 총 데이터 12개이고 마지막 page일 경우 기대값 expected_board_list_03.json")
	public void Given_BoardData12AndLastPage_When_openBoardList_Then_expected_board_list_03_json() throws Exception {
		
		setDataBase(BOARD_XMl_DATA_12);
		
    	//when
    	MvcResult mockMVcResult = mockMvc.perform(get(BoardController.BOARD_LIST_URL + LAST_PAGE)
						    			.accept(JSON_UTF8))
						    			.andExpect(status().isOk())
						    			.andDo(print())
						    			.andReturn();
		
    	//then
		assertThatJson(actualJson(mockMVcResult))
						.whenIgnoringPaths(JSON_IGNORE_CREATE_AT, JSON_IGNORE_UPDATE_AT)
						.when(Option.IGNORING_ARRAY_ORDER)
						.isEqualTo(expectedJson(EXPECTED_BOARD_LIST_JSON_03));
    }

	@Test
	@DisplayName("게시글 작성 메소드 테스트")
	public void Given_new_BoardEntity_When_writeBoard_then_selectBoardList_of_boardService() throws Exception {
		setDataBase(BOARD_XMl_DATA_2);
		
		//given : makeGivenBoardEntity(), when : BOARD_WRITE_URL, then : andExpect
		mockMvc.perform(post(BoardController.BOARD_WRITE_URL)
						.content(objectMapper.writeValueAsString(makeGivenBoardEntity()))
						.accept(JSON_UTF8)
						.contentType(JSON_UTF8))
						.andExpect(jsonPath("$.content.user_id", is("sunlike0303")))
						.andExpect(jsonPath("$.content.title", is("LG 잡고 5위로 가즈아~")))
						.andExpect(jsonPath("$.content.contents", is("원태인 삼성의 황태자~~~!!!")))
		    			.andDo(print())
		    			.andReturn();
	}

	@Test
	@DisplayName("게시글 상세 내용 출력 메소드 테스트")
	public void testGetBoardDetail() throws Exception {
		setDataBase(BOARD_XMl_DATA_2);
		
		//given : id = 1, when : BOARD_DETAIL, then : andExpect
		mockMvc.perform(get(BoardController.BOARD_DETAIL + 1)
						.accept(JSON_UTF8))
						.andExpect(status().isOk())
						.andExpect(jsonPath("$.content.id", is(1)))
						.andExpect(jsonPath("$.content.user_id", is("sunlike0301")))
						.andExpect(jsonPath("$.content.title", is("최강 삼성 승리하리라~")))
						.andExpect(jsonPath("$.content.contents", is("내 목숨을 아이어에")))
						.andDo(print());
    }
	
	public void setDataBase(String databaseXML) throws DatabaseUnitException, SQLException, MalformedURLException, DataSetException {
		DatabaseOperation.CLEAN_INSERT.execute(iDatabaseConnection, new FlatXmlDataSetBuilder().build(new File(databaseXML)));
	}
	
	private String actualJson(MvcResult mockMVcResult) throws UnsupportedEncodingException {
		return mockMVcResult.getResponse().getContentAsString();
	}
	
	private PoongduckResponseEntity expectedJson(String expectedJson) throws IOException, JsonParseException, JsonMappingException {
		return objectMapper.readValue(getClass().getClassLoader().getResourceAsStream(expectedJson), PoongduckResponseEntity.class);
	}

	public BoardEntity makeGivenBoardEntity() {
		BoardEntity givenBoardEntity = new BoardEntity();
		givenBoardEntity.setUser_id("sunlike0303");
		givenBoardEntity.setTitle("LG 잡고 5위로 가즈아~");
		givenBoardEntity.setContents("원태인 삼성의 황태자~~~!!!");
		
		return givenBoardEntity;
	}

}
