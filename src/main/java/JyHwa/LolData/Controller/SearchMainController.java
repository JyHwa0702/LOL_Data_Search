package JyHwa.LolData.Controller;

import JyHwa.LolData.Dto.KakaoDto;
import JyHwa.LolData.Dto.MatchDto.MatchDto;
import JyHwa.LolData.Dto.UserDto;
import JyHwa.LolData.Service.KakaoService;
import JyHwa.LolData.Service.SearchMainService;
import JyHwa.LolData.Service.MatchService;
import JyHwa.LolData.Service.SummonerService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
@ToString
@PropertySource(ignoreResourceNotFound = false, value = "classpath:kakaoApiKey.properties")
public class SearchMainController {

    private final SearchMainService searchMainService;
    private final SummonerService summonerService;
    private final MatchService matchService;
    private final KakaoService kakaoService;

    @Value("${kakao.restapi.key}")
    private String client_id;

    @Value("${redirect.uri}")
    private String redirect_uri;

    @GetMapping("/")
    public String indexPage(Model model) {
        String kakaoTokenUrl = kakaoService.getKakaoCodeUrl();
        log.info("kakaoTokenUrl = "+kakaoTokenUrl);
        model.addAttribute("kakaoTokenUrl",kakaoTokenUrl);
        return "index";
    }

    @GetMapping("/oauth/kakao")
    public String kakaoLogin(@RequestParam("code") String code,Model model){
        log.info("code를 받습니다 = "+code);
        String kakaoAccessToken = kakaoService.getKakaoAccessToken(code);
        JsonNode kakaoInfo = kakaoService.getKakaoInfo(kakaoAccessToken);
        log.info("kakaoInfo = "+kakaoInfo);
        kakaoService.saveKakao(kakaoInfo,model); // 카카오 아이디 저장 및 kakaoId model넘기기
        return "redirect:/";
    }


    @PostMapping("/searchBySummonerName")
    public String SearchByName(String summonerName, Optional<Long> kakaoId, Model model){
        UserDto usersDto = searchMainService.SearchBySummonerName(summonerName,model); //userId model
        searchMainService.showRankedEmblemByTier(usersDto.getTier(),model);
        searchMainService.showProfileIconUrlByUserDto(usersDto,model);//프로필 아이콘 표시
        List<MatchDto> matchDtos = searchMainService.matchDtosByUserPuuid(usersDto.getPuuid(), model);
        searchMainService.showSpellImageUrlByMatchDtos(matchDtos,model); //스펠 이미지 보여주기
        searchMainService.showChampionImageUrlByMatchDtos(matchDtos,model); //챔피언 이미지 보여주기
        searchMainService.showItemImageUrlByMatchDtos(matchDtos,model); //아이템 이미지 보여주기
        searchMainService.showRuneImageUrlByMatchDtos(matchDtos,model); //룬 이미지 보여주기

        searchMainService.saveUser(usersDto); //db에 유저 저장

        if (kakaoId.isPresent()){
            model.addAttribute("kakaoId",kakaoId);
        }

        return "searchForm";
    }


    @GetMapping("/matchDetails/{matchId}")
    public String matchDetails(@PathVariable String matchId,Model model){
        MatchDto matchDto = matchService.callRiotAPIMatchByMatchId(matchId);
        model.addAttribute("matchDto",matchDto);
        return "matchDetails";
    }
}
