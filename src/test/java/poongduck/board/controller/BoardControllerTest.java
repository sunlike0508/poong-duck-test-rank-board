package poongduck.board.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.javacrumbs.jsonunit.core.Option;
import poongduck.board.entity.BoardEntity;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.mysql.MySqlConnection;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
class BoardControllerTest {
	
	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), 
																MediaType.APPLICATION_JSON.getSubtype(), 
																Charset.forName("UTF-8"));

	private static final String APPLICATION_PROPERTIES = "application.properties";
	
	private static final String DRIVER_CLASS_NAME = "spring.datasource.driver-class-name";
	private static final String DRIVER_CLASS_URL = "spring.datasource.url";
	private static final String DRIVER_CLASS_USERNAME = "spring.datasource.username";
	private static final String DRIVER_CLASS_PASSWORD = "spring.datasource.password";
	private static final String DRIVER_CLASS_SCHEMA = "spring.datasource.hikari.schema";
	
	private static final String JSON_IGNORE_ID = "[*].id";
	private static final String JSON_IGNORE_CREATE_AT = "[*].create_at";
	private static final String JSON_IGNORE_UPDATE_AT = "[*].update_at";
	
	private static final String BOARD_JSON = "board.json";
	private static final String BOARD_XMl = "src/main/resources/Board.xml";
	
	private ObjectMapper objectMapper = new ObjectMapper();

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

		DatabaseOperation.CLEAN_INSERT.execute(iDatabaseConnection, new FlatXmlDataSetBuilder().build(new File(BOARD_XMl)));
    }

	@AfterEach
	public void tearDown() throws SQLException {
		if(!iDatabaseConnection.getConnection().isClosed()) {
			iDatabaseConnection.close();
		}
	}
	
	@Test
	@DisplayName("게시글 리스트 출력 메소드 테스트")
	public void testOpenBoardList() throws Exception {
		
    	//when
    	MvcResult mockMVcResult = mockMvc.perform(get(BoardController.BOARD_LIST_URL)
						    			.accept(APPLICATION_JSON_UTF8))
						    			.andExpect(status().isOk())
						    			.andDo(print())
						    			.andReturn();
		
    	//then
		assertThatJson(mockMVcResult.getResponse().getContentAsString())
						.whenIgnoringPaths(JSON_IGNORE_ID, JSON_IGNORE_CREATE_AT, JSON_IGNORE_UPDATE_AT)
						.when(Option.IGNORING_ARRAY_ORDER)
						.isEqualTo(objectMapper.readValue(
								getClass().getClassLoader().getResourceAsStream(BOARD_JSON), BoardEntity[].class));
    }

	@Test
	@DisplayName("게시글 작성 메소드 테스트")
	public void testWriteBoard() throws Exception {
		
		//given
		BoardEntity givenBoardEntity = makeGivenBoardEntity();
		
		//when
		MvcResult mockMVcResult = mockMvc.perform(post(BoardController.BOARD_WRITE_URL)
										.content(objectMapper.writeValueAsString(givenBoardEntity))
										.contentType(APPLICATION_JSON_UTF8))
										.andExpect(status().isOk())
						    			.andDo(print())
						    			.andReturn();

		//expected
    	List<BoardEntity> expectedBoardEntitylist = new ObjectMapper().readValue(
    			getClass().getClassLoader().getResourceAsStream(BOARD_JSON), new TypeReference<List<BoardEntity>>() {});
    	expectedBoardEntitylist.add(givenBoardEntity);
    	
    	//then
		assertThatJson(mockMVcResult.getResponse().getContentAsString())
						.whenIgnoringPaths(JSON_IGNORE_ID, JSON_IGNORE_CREATE_AT, JSON_IGNORE_UPDATE_AT)
						.when(Option.IGNORING_ARRAY_ORDER)
						.isEqualTo(expectedBoardEntitylist);
	}

	public BoardEntity makeGivenBoardEntity() {
		BoardEntity givenBoardEntity = new BoardEntity();
		givenBoardEntity.setUser_id("sunlike0303");
		givenBoardEntity.setContents("호드를 위하여");
		
		return givenBoardEntity;
	}

}
