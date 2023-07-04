package JyHwa.LolData.Controller;

import JyHwa.LolData.Dto.MatchDto.MatchDto;
import JyHwa.LolData.Dto.UserDto;
import JyHwa.LolData.Entity.Kakao;
import JyHwa.LolData.Entity.User;
import JyHwa.LolData.Repository.KakaoRepository;
import JyHwa.LolData.Service.KakaoService;
import JyHwa.LolData.Service.SearchMainService;
import JyHwa.LolData.Service.MatchService;
import JyHwa.LolData.Service.SummonerService;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
@ToString
@PropertySource(value = "classpath:kakaoApiKey.properties")
public class SearchMainController {

    private final SearchMainService searchMainService;
    private final SummonerService summonerService;
    private final MatchService matchService;
    private final KakaoService kakaoService;
    private final KakaoRepository kakaoRepository;

    @Value("${kakao.restapi.key}")
    private String client_id;

    @Value("${redirect.uri}")
    private String redirect_uri;

    @GetMapping("/")
    public String indexPage(HttpSession session, Model model) {
        String kakaoTokenUrl = kakaoService.getKakaoCodeUrl(); //코드 불러오기
        log.info("kakaoTokenUrl = "+kakaoTokenUrl);
        model.addAttribute("kakaoTokenUrl",kakaoTokenUrl);

        Long kakaoId = (Long) session.getAttribute("kakaoId");

        log.info("kakaoId from session = "+kakaoId);

        boolean isSaveKakao = kakaoService.isSaveKakao(kakaoId);;
        model.addAttribute("isSaveKakao",kakaoService.isSaveKakao(kakaoId));
        model.addAttribute("kakaoId",kakaoId);
        if(isSaveKakao==true){
            Optional<Kakao> byId = kakaoRepository.findById(kakaoId);
            log.info("byId = "+byId.toString());
            if(byId.isPresent()){
                model.addAttribute("accessToken",byId.get().getToken());
            }
        }
        return "index";
    }


    @GetMapping("/search")
    public String SearchByName( String summonerName, Model model,HttpSession session){
        UserDto userDto = searchMainService.showUserInfo(summonerName, model);

        searchMainService.showMatchInfo(userDto,model);
        log.info("userDto = "+userDto.toString());

        searchMainService.saveUser(userDto,model); //db에 유저 저장, userId mode// l

        Long kakaoId = (Long) session.getAttribute("kakaoId");
        model.addAttribute("kakaoId",kakaoId);

        return "searchForm";
    }

    @GetMapping("/matchDetails/{matchId}")
    public String matchDetails(@PathVariable String matchId,Model model){
        MatchDto matchDto = matchService.callRiotAPIMatchByMatchId(matchId);
        model.addAttribute("matchDto",matchDto);
        return "matchDetails";
    }
}
