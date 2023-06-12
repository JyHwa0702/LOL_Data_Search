package JyHwa.LolData.Controller;

import JyHwa.LolData.Dto.MatchDto.MatchDto;
import JyHwa.LolData.Dto.UserDto;
import JyHwa.LolData.Service.SearchMainService;
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

@Controller
@RequiredArgsConstructor
@Slf4j
@ToString
public class SearchMainController {

    private final SearchMainService searchMainService;
    private final SummonerService summonerService;
    private final MatchService matchService;

    @GetMapping("/")
    public String indexPage(){
        return "index";
    }

    @PostMapping("/searchBySummonerName")
    public String SearchByName(String summonerName, Model model){
        UserDto usersDto = searchMainService.SearchBySummonerName(summonerName,model);
        searchMainService.showRankedEmblemByTier(usersDto.getTier(),model);
        searchMainService.showProfileIconUrlByUserDto(usersDto,model);//프로필 아이콘 표시
        List<MatchDto> matchDtos = searchMainService.matchDtosByUserPuuid(usersDto.getPuuid(), model);
        searchMainService.showSpellImageUrlByMatchDtos(matchDtos,model); //스펠 이미지 보여주기
        searchMainService.showChampionImageUrlByMatchDtos(matchDtos,model); //챔피언 이미지 보여주기
        searchMainService.showItemImageUrlByMatchDtos(matchDtos,model); //아이템 이미지 보여주기
        searchMainService.showRuneImageUrlByMatchDtos(matchDtos,model); //룬 이미지 보여주기
        return "searchForm";
    }


    @GetMapping("/matchDetails/{matchId}")
    public String matchDetails(@PathVariable String matchId,Model model){
        MatchDto matchDto = matchService.callRiotAPIMatchByMatchId(matchId);
        model.addAttribute("matchDto",matchDto);
        return "matchDetails";
    }
}
