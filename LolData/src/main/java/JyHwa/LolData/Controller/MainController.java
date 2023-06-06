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
import java.util.Map;
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

    @PostMapping("/searchBySummonerName")
    public String SearchByName(String summonerName, Model model){
        UserDto userDto = mainService.SearchBySummonerName(summonerName,model);
        mainService.showRankedEmblemByTier(userDto.getTier(),model);
        mainService.showProfileIconUrlByUserDto(userDto,model);//프로필 아이콘 표시
        List<MatchDto> matchDtos = mainService.matchDtosByUserPuuid(userDto.getPuuid(), model);
        mainService.showSpellImageUrlByMatchDtos(matchDtos,model); //스펠 이미지 보여주기
        mainService.showChampionImageUrlByMatchDtos(matchDtos,model); //챔피언 이미지 보여주기
        mainService.showItemImageUrlByMatchDtos(matchDtos,model); //아이템 이미지 보여주기
        mainService.showRuneImageUrlByMatchDtos(matchDtos,model); //룬 이미지 보여주기
        return "searchForm";
    }

    @GetMapping("/matchDetails/{matchId}")
    public String matchDetails(@PathVariable String matchId,Model model){
        MatchDto matchDto = matchService.callRiotAPIMatchByMatchId(matchId);
        model.addAttribute("matchDto",matchDto);
        return "matchDetails";
    }
}
