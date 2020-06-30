package poongduck.board.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.dbunit.Assertion;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.mysql.MySqlConnection;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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

import static org.unitils.reflectionassert.ReflectionAssert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class BoardControllerTest {

	@Autowired
	MockMvc mockMvc;

	IDatabaseConnection iDatabaseConnection;

	@BeforeEach
	public void setUp() throws ClassNotFoundException, DatabaseUnitException, SQLException, IOException{
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("application.properties");
		Properties properties = new Properties();
		properties.load(inputStream);
		
		final String dirver = properties.getProperty("spring.datasource.driver-class-name");
		final String url = properties.getProperty("spring.datasource.url");
		final String username = properties.getProperty("spring.datasource.username");
		final String passward = properties.getProperty("spring.datasource.password");
		final String schema = properties.getProperty("spring.datasource.hikari.schema");
		
		Class.forName(dirver);
		iDatabaseConnection = new MySqlConnection(DriverManager.getConnection(url, username, passward), schema);
		iDatabaseConnection.getConfig().setProperty(DatabaseConfig.FEATURE_QUALIFIED_TABLE_NAMES, false);
		
		ReplacementDataSet dataSet = setRepalcementDataSet("Board.xml");
		
		DatabaseOperation.CLEAN_INSERT.execute(iDatabaseConnection, dataSet);
    }

	@AfterEach
	public void tearDown() throws SQLException {
		if(!iDatabaseConnection.getConnection().isClosed()) {
			iDatabaseConnection.close();
		}
	}

	@Test
	public void testIDatabaseTester() throws Exception {

		ITable actualTable = iDatabaseConnection.createDataSet().getTable("board");
		
		ITable expectedTable = setRepalcementDataSet("Expected_Board.xml").getTable("board");
		
		String[] ignoreCols = {"create_at", "update_at"};
		Assertion.assertEqualsIgnoreCols(expectedTable, actualTable, ignoreCols);
	}

	public ReplacementDataSet setRepalcementDataSet(String fileName) throws MalformedURLException, DataSetException {
		
		ReplacementDataSet dataSet = new ReplacementDataSet(new FlatXmlDataSetBuilder().build(new File(fileName)));
		
		dataSet.addReplacementObject("[null]", null);
		dataSet.addReplacementObject("[date]", new Date(System.currentTimeMillis()));
		
		return dataSet;
	}
	@Disabled
	@Test
	@DisplayName("게시글 리스트 출력 메소드 테스트")
	public void testOpenBoardList() throws Exception {
		
    	//when : openBoardList 메소드를 실행하여 list 화면에 결과 값을 출력 할때
    	MvcResult result = mockMvc.perform(get(BoardController.BOARD_LIST_URL))
    			.andExpect(status().isOk())
    			.andExpect(view().name(BoardController.BOARD_LIST_VIEW))
    			.andExpect(model().attributeExists(BoardController.BOARD_LIST_ATTRIBUTE))
    			.andDo(print())
    			.andReturn();
    	
    	//그 결과 값이 boardList라고 하자.
    	Object actualList = result.getModelAndView().getModel().get(BoardController.BOARD_LIST_ATTRIBUTE);
    	
    	//expected : boardList 변수 값은 다음 expectList 변수와 값이 같아야 한다.
    	List<BoardEntity> expectList = createMockBoardList();

    	assertReflectionEquals(expectList, actualList, ReflectionComparatorMode.LENIENT_DATES);
    }
	
	@Test
	@DisplayName("게시글 작성 메소드 테스트")
	public void testWriteBoard() throws Exception {
		
		mockMvc.perform(post(BoardController.BOARD_WRITE_URL)
    			.param("user_id", "sunlike0303")
    			.param("contents", "호드를 위하여"))
    			.andExpect(redirectedUrl(BoardController.BOARD_LIST_URL))
    			.andExpect(status().is3xxRedirection())
    			.andExpect(view().name(BoardController.BOARD_LIST_REDIRECT_URL))
    			.andDo(print());
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
