package JyHwa.LolData.Controller;

import JyHwa.LolData.Dto.KakaoDto;
import JyHwa.LolData.Dto.UserDto;
import JyHwa.LolData.Entity.User;
import JyHwa.LolData.Service.CheckingUserMainService;
import JyHwa.LolData.Service.KakaoService;
import JyHwa.LolData.Service.SearchMainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class CheckingUserMainController {

    private final SearchMainService searchMainService;
    private final CheckingUserMainService checkingUserMainService;
    private final KakaoService kakaoService;


    @PostMapping("/checkingUser")
    public String checkingUser(UserDto usersDto){

        User savedUser = searchMainService.saveUser(usersDto);
        checkingUserMainService.checkingUser(savedUser);
        return "redirect:/searchBySummonerName?summonerName=" + savedUser.getSummonerName();
    }

    @GetMapping("/checkingUser/{kakaoId}")
    public String checkingUser(@PathVariable Long kakaoId, Model model){
        checkingUserMainService.showCheckingUser(kakaoId,model);
        return "checkingUsersForm";
    }

    @DeleteMapping("/checkingUser/{kakaoId}/{userId}")
    public String deleteChickingUser(@PathVariable Long kakaoId,@PathVariable Long userId,Model model){
        checkingUserMainService.deleteCheckingUser(kakaoId,userId);
        checkingUserMainService.showCheckingUser(kakaoId,model);

        return "redirect:/checkingUser/"+kakaoId;
    }

}
