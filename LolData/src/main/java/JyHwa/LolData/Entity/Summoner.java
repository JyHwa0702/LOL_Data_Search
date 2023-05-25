package JyHwa.LolData.Entity;


import JyHwa.LolData.Dto.SummonerDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Summoner {

    @Id
    @Column(name = "id", nullable = false)
    private String id;


    private String accountId;
    private int profileIconId;
    private long revisionDate;
    private String name;
    private String puuid;
    private long summonerLevel;
    private int checkField;


    public Summoner(SummonerDto summonerDto) {
        this.accountId = summonerDto.getAccountId();
        this.profileIconId = summonerDto.getProfileIconId();
        this.revisionDate = summonerDto.getRevisionDate();
        this.name = summonerDto.getName();
        this.id = summonerDto.getId();
        this.puuid = summonerDto.getPuuid();
        this.summonerLevel = summonerDto.getSummonerLevel();
        this.checkField = summonerDto.getCheckField();
    }
}
