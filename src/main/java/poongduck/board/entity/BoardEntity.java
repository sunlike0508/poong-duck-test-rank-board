package poongduck.board.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value="BoardEntity : 게시글 내용", description="게시글 내용")
@Entity
@Table(name="poong_duck_test_board")
@NoArgsConstructor
@Data
public class BoardEntity {
	
	private static final String DATE_PATTERN = "yyyy-MM-dd";
	private static final String DATE_TIMEZONE = "Asia/Seoul";
    
	@ApiModelProperty(value="게시글 ID")
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	@ApiModelProperty(value="사용자 ID")
	@Column(nullable=false)
	private int user_id;
	
	@ApiModelProperty(value="글 제목")
	@Column(nullable=false)
	private String title;
	
	@ApiModelProperty(value="글 내용")
	@Column(nullable=false)
	private String contents;
	
	@ApiModelProperty(value="글 작성 시간")
	@Column(nullable=false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN, timezone = DATE_TIMEZONE)
	private Date created_at = new Date(System.currentTimeMillis());
	
	@ApiModelProperty(value="글 수정 시간")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN, timezone = DATE_TIMEZONE)
	private Date updated_at;

}
