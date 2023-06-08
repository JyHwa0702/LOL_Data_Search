package JyHwa.LolData.Dto.MatchDto;

import lombok.Data;

import java.util.List;

@Data
public class InfoDto {
    private long gameCreation;
    private long gameDuration;
    private long gameEndTimestamp;
    private long gameId;
    private String gameMode;
    private String gameName;
    private long gameStartTimestamp;
    private String gameType;
    private String gameVersion;
    private int mapId;
    private List<ParticipantDto> participants;
    private String platformId;
    private int queueId;
    private List<TeamDto> teams;
    private String tournamentCode;
}
