package JyHwa.LolData.Controller;

import JyHwa.LolData.Dto.MatchDto.MatchDto;
import JyHwa.LolData.Dto.MatchDto.ParticipantDto;
import JyHwa.LolData.Dto.UserDto;
import JyHwa.LolData.Service.MainService;
import JyHwa.LolData.Service.MatchService;
import JyHwa.LolData.Service.SummonerService;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@Slf4j
@ToString
public class MainController {

    private final MainService mainService;
    private final SummonerService summonerService;
    private final MatchService matchService;

    @GetMapping("/")
    public String indexPage(){
        return "index";
    }

//    @GetMapping("/spells/{spellKey}")
//    public String showSpellByKey(@PathVariable int spellKey, Model model){
//        return"";
//    }



    @PostMapping("/searchBySummonerName")
    public String SearchByName(String summonerName, Model model){
        UserDto userDto = mainService.SearchBySummonerName(summonerName);
        model.addAttribute("user",userDto);

        String rankedEmblem = mainService.showRankedEmblemByTier(userDto.getTier());
        model.addAttribute("rankedEmblem",rankedEmblem); //랭크 엠블럼 표시


        String profileIconUrl = mainService.showProfileIconUrlByUserDto(userDto);
        model.addAttribute("profileIconUrl",profileIconUrl); //프로필 아이콘 표시

        String[] matchIds = matchService.callRiotAPIMatchIdByPuuid(userDto.getPuuid());
        List<MatchDto> matchDtos = matchService.callRiotAPIMatchsByMatchIds(matchIds);

        Set<String> spellKeys = matchService.extractSpellKeysFromMatches(matchDtos); //매치의 스펠키 가져옴
        matchService.modelSpellUrlBySpellKey(spellKeys,model); //스펠키로 스펠 이름 뽑아서 모델넣음

        for (int i=0; i<matchDtos.toArray().length; i++){
            MatchDto matchDto = matchDtos.get(i);
            List<ParticipantDto> participants = matchDto.getInfo().getParticipants();

        }
        model.addAttribute("matchDtos",matchDtos);
        return "searchForm";
    }

    @GetMapping("/matchDetails/{matchId}")
    public String matchDetails(@PathVariable String matchId,Model model){
        MatchDto matchDto = matchService.callRiotAPIMatchByMatchId(matchId);
        model.addAttribute("matchDto",matchDto);
        return "matchDetails";
    }
}
