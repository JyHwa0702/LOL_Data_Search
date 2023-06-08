package JyHwa.LolData.Dto.MatchDto;

import lombok.Data;

import java.util.List;

@Data
public class PerksDto {
    private PerkStatsDto statPerks;
    private List<PerkStyleDto> styles;
}
