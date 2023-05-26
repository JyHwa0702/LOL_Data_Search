package JyHwa.LolData.Entity;

import JyHwa.LolData.Dto.UserDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //LeagueEntryDto
    private String summonerName;
    private String queueType;
    private String tier;
    private String rank;
    private int leaguePoints;
    private int wins;
    private int losses;

    //SummonerDto
    private long summonerLevel;
    private int checkField;

    public User(UserDto userDto){
        this.id =userDto.getId();
        this.summonerName =userDto.getSummonerName();
        this.queueType =userDto.getQueueType();
        this.tier =userDto.getTier();
        this.rank =userDto.getRank();
        this.leaguePoints =userDto.getLeaguePoints();
        this.wins =userDto.getWins();
        this.losses =userDto.getLosses();
        this.summonerLevel =userDto.getSummonerLevel();
        this.checkField =userDto.getCheckField();
    }

}
