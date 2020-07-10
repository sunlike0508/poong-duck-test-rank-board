package poongduck.rank.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value="RankUser : 랭크 유저", description="랭크 유저")
@Entity
@Table(name="rank_user")
@NoArgsConstructor
@Data
public class RankUser {
	
	@ApiModelProperty(value="랭크유저 자동생성 ID")
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@ApiModelProperty(value="랭크유저 nick name")
	@Column(nullable=false)
	private String nickname;
	
	@ApiModelProperty(value="패")
	@Column(nullable=false)
	private String club;
	
	@ApiModelProperty(value="점수")
	@Column
	private int point;
}
