package JyHwa.LolData.Entity;

import JyHwa.LolData.Dto.UserDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "users")
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "kakao_id")
    private Kakao kakao;

    //LeagueEntryDto
    private String summonerName;
    private String queueType;
    private String tier;
    private String rank;
    private int leaguePoints;
    private int wins;
    private int losses;

    //SummonerDto
    private String puuid;
    private int profileIconId;
    private long summonerLevel;
    private int checkField;

    public User(UserDto usersDto){
        this.id = usersDto.getId();
        this.summonerName = usersDto.getSummonerName();
        this.queueType = usersDto.getQueueType();
        this.tier = usersDto.getTier();
        this.rank = usersDto.getRank();
        this.leaguePoints = usersDto.getLeaguePoints();
        this.wins = usersDto.getWins();
        this.losses = usersDto.getLosses();
        this.puuid = usersDto.getPuuid();
        this.profileIconId= usersDto.getProfileIconId();
        this.summonerLevel = usersDto.getSummonerLevel();
        this.checkField = usersDto.getCheckField();
        this.kakao= usersDto.getKakao();
    }

}
