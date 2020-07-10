package poongduck.response.entity;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import poongduck.rank.entity.RankUser;

@ApiModel(value="RankResponseEntity : 랭크 응답 값", description="랭크 응답 값")
@Data
public class RankResponseEntity {
	@ApiModelProperty(value="랭크 리스트")
	List<RankUser> ranking;
}
