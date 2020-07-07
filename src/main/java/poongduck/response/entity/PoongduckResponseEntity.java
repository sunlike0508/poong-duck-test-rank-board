package poongduck.response.entity;

import java.util.List;

import lombok.Data;
import poongduck.board.entity.BoardEntity;

@Data
public class PoongduckResponseEntity {
	List<BoardEntity> board_list;
	BoardEntity content;
	String previous_page;
	String after_page;
}
