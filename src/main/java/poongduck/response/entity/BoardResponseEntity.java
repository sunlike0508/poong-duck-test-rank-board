package poongduck.response.entity;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import poongduck.board.entity.BoardEntity;

@ApiModel(value="BoardResponseEntity : 게시판 응답 값", description="게시판 응답 값")
@Data
public class BoardResponseEntity {
	@ApiModelProperty(value="게시글 리스트")
	List<BoardEntity> board_list;
	@ApiModelProperty(value="게시글 상세내용")
	BoardEntity content;
	@ApiModelProperty(value="게시글 이전 페이지")
	String previous_page;
	@ApiModelProperty(value="게시글 다음 페이지")
	String after_page;
}
