package poongduck.response.entity;

import java.util.List;

import lombok.Data;
import poongduck.board.entity.BoardEntity;
import poongduck.rank.entity.RankUser;

@Data
public class PoongduckResponseEntity {
	List<BoardEntity> board_list;
	BoardEntity content;
	List<RankUser> ranking;
	String previous_page;
	String after_page;
}
