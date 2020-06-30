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

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class BoardControllerTest {
	
	private static final String PROPERTIES = "application.properties";
	private static final String DRIVER = "spring.datasource.driver-class-name";
	private static final String URL = "spring.datasource.url";
	private static final String USERNAME = "spring.datasource.username";
	private static final String PASSWARD = "spring.datasource.password";
	private static final String SCHEMA = "spring.datasource.hikari.schema";
	
	private static final String TABLE_BOARD = "board";
	
	private static final String ID = "id";
	private static final String USER_ID = "user_id";
	private static final String CONTENTS = "contents";

	@Autowired
	MockMvc mockMvc;

	IDatabaseConnection iDatabaseConnection;

	@BeforeEach
	public void setUp() throws ClassNotFoundException, DatabaseUnitException, SQLException, IOException{
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(PROPERTIES);
		Properties properties = new Properties();
		properties.load(inputStream);
		
		Class.forName(properties.getProperty(DRIVER));
		iDatabaseConnection = new MySqlConnection(
				DriverManager.getConnection(properties.getProperty(URL), properties.getProperty(USERNAME), properties.getProperty(PASSWARD)),
				properties.getProperty(SCHEMA));
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
		//when
		ITable actualTable = iDatabaseConnection.createDataSet().getTable(TABLE_BOARD);
		//expected
		ITable expectedTable = setRepalcementDataSet("Expected_Board.xml").getTable(TABLE_BOARD);
		//except comparison column
		String[] ignoreCols = {"create_at", "update_at"};
		Assertion.assertEqualsIgnoreCols(expectedTable, actualTable, ignoreCols);
	}

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
    	assertReflectionEquals(expectedBoardList(), actualList, ReflectionComparatorMode.LENIENT_DATES);
    }

	@Test
	@DisplayName("게시글 작성 메소드 테스트")
	public void testWriteBoard() throws Exception {
		
		mockMvc.perform(post(BoardController.BOARD_WRITE_URL)
    			.param(USER_ID, "sunlike0303")
    			.param(CONTENTS, "호드를 위하여"))
    			.andExpect(redirectedUrl(BoardController.BOARD_LIST_URL))
    			.andExpect(status().is3xxRedirection())
    			.andExpect(view().name(BoardController.BOARD_LIST_REDIRECT_URL))
    			.andDo(print());
    }
	
	public ReplacementDataSet setRepalcementDataSet(String fileName) throws MalformedURLException, DataSetException {
		
		ReplacementDataSet dataSet = new ReplacementDataSet(new FlatXmlDataSetBuilder().build(new File(fileName)));
		
		dataSet.addReplacementObject("[null]", null);
		dataSet.addReplacementObject("[date]", new Date(System.currentTimeMillis()));
		
		return dataSet;
	}
	
	public List<BoardEntity> expectedBoardList() throws DataSetException, MalformedURLException {
		
		List<BoardEntity> expectList = new ArrayList<BoardEntity>();
    	ITable expectedTable = setRepalcementDataSet("Expected_Board_List.xml").getTable(TABLE_BOARD);
    	
    	for(int row = 0; row < expectedTable.getRowCount(); row++) {
    		BoardEntity boardEntity = makeBoardEntity(expectedTable, row);
    		expectList.add(boardEntity);
    	}
    	
		return expectList;
	}

	public BoardEntity makeBoardEntity(ITable expectedTable, int row) throws DataSetException {
		BoardEntity boardEntity = new BoardEntity();
		boardEntity.setId(Integer.parseInt(expectedTable.getValue(row, ID).toString()));
		boardEntity.setContents(expectedTable.getValue(row, CONTENTS).toString());
		boardEntity.setUser_id(expectedTable.getValue(row, USER_ID).toString());
		
		return boardEntity;
	}
}
