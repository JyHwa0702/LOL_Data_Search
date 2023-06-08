package JyHwa.LolData.Dto;

import JyHwa.LolData.Entity.Summoner;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SummonerDto {
    private String accountId;
    private int profileIconId;
    private long revisionDate;
    private String name;

    private String id;
    private String puuid;
    private long summonerLevel;
    @JsonIgnore
    private int checkField;

    public SummonerDto(Summoner summoner) {
        this.accountId = summoner.getAccountId();
        this.profileIconId = summoner.getProfileIconId();
        this.revisionDate = summoner.getRevisionDate();
        this.name = summoner.getName();
        this.id = summoner.getId();
        this.puuid = summoner.getPuuid();
        this.summonerLevel = summoner.getSummonerLevel();
        this.checkField = summoner.getCheckField();

    }

    public Summoner toEntity(){
        Summoner summoner = new Summoner(this);
        return summoner;
    }


}
