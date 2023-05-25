package JyHwa.LolData.Dto.MatchDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import lombok.Data;

@Data
@JsonIgnoreType
public class MatchDto {
    private MetadataDto metadata;
    private InfoDto info;
}
