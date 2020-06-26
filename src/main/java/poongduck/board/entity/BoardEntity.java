package poongduck.board.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.NoArgsConstructor;
import poongduk.common.constant.Constant;

@Entity
@Table(name="board")
@NoArgsConstructor
@Data
public class BoardEntity {
    
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	@Column(nullable=false)
	private String user_id;
	
	@Column(nullable=false)
	private String contents;
	
	@Column(nullable=false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.DATE_PATTERN, timezone = Constant.DATE_TIMEZONE)
	private Date create_at = new Date(System.currentTimeMillis());
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.DATE_PATTERN, timezone = Constant.DATE_TIMEZONE)
	private Date update_at;

}
