package poongduck.rank.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
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
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
class RankControllerTest {
	
	private static final String APPLICATION_PROPERTIES = "application.properties";
	
	private static final String DRIVER_CLASS_NAME = "spring.datasource.driver-class-name";
	private static final String DRIVER_CLASS_URL = "spring.datasource.url";
	private static final String DRIVER_CLASS_USERNAME = "spring.datasource.username";
	private static final String DRIVER_CLASS_PASSWORD = "spring.datasource.password";
	private static final String DRIVER_CLASS_SCHEMA = "spring.datasource.hikari.schema";
	
	private static final String RANK_XMl = "src/main/resources/rank.xml";
	
	private static final String JSON_UTF8 = "application/json;charset=UTF-8";
	
	private static final String FIRST_ARRAY = "$.ranking[0].";
	private static final String SECOND_ARRAY = "$.ranking[1].";
	private static final String THIRD_ARRAY = "$.ranking[2].";
	
	private static final String ID = "id";
	private static final String NICKNAME = "nickname";
	private static final String CLUB = "club";
	private static final String POINT = "point";

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

		DatabaseOperation.CLEAN_INSERT.execute(iDatabaseConnection, new FlatXmlDataSetBuilder().build(new File(RANK_XMl)));
    }
	
	@Test
	@DisplayName("랭크 리스트 출력 메소드 테스트")
	public void test_getRankList() throws Exception {
		
		mockMvc.perform(get(RankController.RANK)
						.accept(JSON_UTF8))
						.andExpect(status().isOk())
						.andExpect(jsonPath(FIRST_ARRAY + ID, is(3)))
						.andExpect(jsonPath(FIRST_ARRAY + NICKNAME, is("바다의태양")))
						.andExpect(jsonPath(FIRST_ARRAY + CLUB, is("굴렁쇠")))
						.andExpect(jsonPath(FIRST_ARRAY + POINT, is(150)))
						.andExpect(jsonPath(SECOND_ARRAY + ID, is(1)))
						.andExpect(jsonPath(SECOND_ARRAY + NICKNAME, is("sunlike0301")))
						.andExpect(jsonPath(SECOND_ARRAY + CLUB, is("산소래")))
						.andExpect(jsonPath(SECOND_ARRAY + POINT, is(100)))
						.andExpect(jsonPath(THIRD_ARRAY + ID, is(2)))
						.andExpect(jsonPath(THIRD_ARRAY + NICKNAME, is("sunlike0302")))
						.andExpect(jsonPath(THIRD_ARRAY + CLUB, is("사통백이")))
						.andExpect(jsonPath(THIRD_ARRAY + POINT, is(50)))
						.andDo(print());
	}
	
	@AfterEach
	public void tearDown() throws SQLException {
		if(!iDatabaseConnection.getConnection().isClosed()) {
			iDatabaseConnection.close();
		}
	}
}
