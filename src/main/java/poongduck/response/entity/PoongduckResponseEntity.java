package poongduck.response.entity;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import poongduck.board.entity.BoardEntity;
import poongduck.rank.entity.RankUser;

@ApiModel(value="PoongduckResponseEntity : 응답 값")
@Data
public class PoongduckResponseEntity {
	@ApiModelProperty(value="게시글 리스트")
	List<BoardEntity> board_list;
	@ApiModelProperty(value="게시글 상세 내용")
	BoardEntity content;
	@ApiModelProperty(value="랭크 리스트")
	List<RankUser> ranking;
	@ApiModelProperty(value="게시글 이전 페이지")
	String previous_page;
	@ApiModelProperty(value="게시글 다음 페이지")
	String after_page;
}
