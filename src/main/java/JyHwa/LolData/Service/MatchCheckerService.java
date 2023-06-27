package JyHwa.LolData.Service;

import JyHwa.LolData.Dto.MatchDto.MatchDto;
import JyHwa.LolData.Dto.MatchDto.ParticipantDto;
import JyHwa.LolData.Entity.Kakao;
import JyHwa.LolData.Entity.User;
import JyHwa.LolData.Repository.KakaoRepository;
import JyHwa.LolData.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchCheckerService {
    // 이전 단계에서 구현한 전적 검색 및 사용자 목록 처리 로직 ...
    private final MatchService matchService;
    private final KakaoRepository kakaoRepository;
    private final UserRepository userRepository;
    private final KakaoService kakaoService;

    @Scheduled(fixedRate = 600000)
    private void checkRecentMatches(){
        log.info("Runnig CheckRecentMatches()");

        List<User> usersByCheckFiled = userRepository.findByCheckField(1);

        if (usersByCheckFiled != null) {
            for (User user: usersByCheckFiled){
                String[] matcheIds = matchService.callRiotAPIMatchIdByPuuid(user.getPuuid());
                List<MatchDto> matchDtos = matchService.callRiotAPIMatchsByMatchIds(matcheIds);
                int loseCount = loseCount(user.getSummonerName(),matchDtos);

                if(loseCount >=3){
                    log.info("loseCount 값이 3이상.");
                    Optional<Kakao> kakaoOptional = kakaoRepository.findById(user.getKakao().getId());

                    if(kakaoOptional.isPresent()){
                        Kakao kakao = kakaoOptional.get();
                        String message = String.format("%s 님이 현재 %d연속 패배 진행중입니다.", user.getSummonerName(), loseCount);
                        log.info("message = "+message);

                        try{
                            String tryMessage = kakaoService.sendNotification(message, kakao.getToken());
                            log.info(tryMessage);

                        } catch (IOException e) {
                            log.error(kakao.getNickname()+"님 아이디에서 메세지 보내기를 실패 했습니다. 보내려는 아이디 : "+user.getSummonerName());
                            e.printStackTrace();
                        }
                    }
                }


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
                if (participantDto.getSummonerName().equals(summonerName)) {

                    if (!participantDto.isWin()) {
                        count++;
                    } else if (participantDto.isWin()) {
                        log.info(summonerName+"님의 연속 패배 수 : "+count);
                        return count;
                    }
                }
            }
        }
        log.info(summonerName+"님의 연속 패배 수 : "+count);
        return count;
    }
}
