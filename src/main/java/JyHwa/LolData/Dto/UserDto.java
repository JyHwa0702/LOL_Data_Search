package JyHwa.LolData.Dto;

import JyHwa.LolData.Entity.Kakao;
import JyHwa.LolData.Entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {
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
    private String puuid;
    private int profileIconId;
    private long summonerLevel;
    private int checkField;
    private Kakao kakao;

    public UserDto(User user){
        this.id = user.getId();
        this.summonerName = user.getSummonerName();
        this.queueType = user.getQueueType();
        this.tier = user.getTier();
        this.rank = user.getRank();
        this.leaguePoints = user.getLeaguePoints();
        this.wins = user.getWins();
        this.losses = user.getLosses();
        this.puuid = user.getPuuid();
        this.profileIconId = user.getProfileIconId();
        this.summonerLevel = user.getSummonerLevel();
        this.checkField = user.getCheckField();
        this.kakao = user.getKakao();
    }

    public User toEntity(){
        User user = new User(this);
        return user;
    }
}
