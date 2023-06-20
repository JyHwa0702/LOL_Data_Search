package JyHwa.LolData.Service;

import JyHwa.LolData.Dto.MatchDto.MatchDto;
import JyHwa.LolData.Dto.MatchDto.ParticipantDto;
import JyHwa.LolData.Entity.Kakao;
import JyHwa.LolData.Entity.User;
import JyHwa.LolData.Repository.KakaoRepository;
import JyHwa.LolData.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MatchCheckerService {
    // 이전 단계에서 구현한 전적 검색 및 사용자 목록 처리 로직 ...
    private final MatchService matchService;
    private final KakaoRepository kakaoRepository;
    private final UserRepository userRepository;
    private final KakaoService kakaoService;

    @Scheduled(fixedRate = 300000)
    private void checkRecentMatches(){

        List<User> usersByCheckFiled = userRepository.findByCheckField(1);

        if (usersByCheckFiled != null) {
            for (User user: usersByCheckFiled){
                String[] matcheIds = matchService.callRiotAPIMatchIdByPuuid(user.getPuuid());
                List<MatchDto> matchDtos = matchService.callRiotAPIMatchsByMatchIds(matcheIds);
                int loseCount = loseCount(user.getSummonerName(),matchDtos);

                kakaoService.


            }

        }
        // 로그인한 유저 목록을 가져와서
        // 각 유저의 match에서 최근 게임(최근 3경기)이 3연속 패배인지 확인 (isThreeLosesInARow 함수 참조)
        // 확인한 후 3연속 패배일 경우 카카오톡 메시지 전송 로직 실행
    }

    private int loseCount(String summonerName,List<MatchDto> matchDtos) {

        if (matchDtos == null) {
            return 0;
        }

        int count = 0;

        for (MatchDto match : matchDtos) {
            List<ParticipantDto> participants = match.getInfo().getParticipants();

            for (ParticipantDto participantDto : participants) {
                if (participantDto.getSummonerName() == summonerName) {

                    if (!participantDto.isWin()) {
                        count++;
                    } else if (participantDto.isWin()) {
                        return count;
                    }
                }
            }
        }
        return count;
    }
}
