package JyHwa.LolData.Controller;

import JyHwa.LolData.Dto.KakaoDto;
import JyHwa.LolData.Dto.UserDto;
import JyHwa.LolData.Entity.User;
import JyHwa.LolData.Service.CheckingUserMainService;
import JyHwa.LolData.Service.KakaoService;
import JyHwa.LolData.Service.SearchMainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CheckingUserMainController {

    private final SearchMainService searchMainService;
    private final CheckingUserMainService checkingUserMainService;
    private final KakaoService kakaoService;



    @PostMapping("/checkingUser")
    public String checkingUser(Long userId, HttpSession session, RedirectAttributes redirectAttributes){
        log.info("userId = "+userId);
        log.info("kakaoId = "+session.getAttribute("kakaoId"));

        if(userId == null||session.getAttribute("kakaoId")==null){
            log.error("userId or kakaoId is null");
            return "";
        }

        Long kakaoId = (Long) session.getAttribute("kakaoId");
        User user = checkingUserMainService.checkingUser(userId, kakaoId);
//        String encodedSummonerName = URLEncoder.encode(user.getSummonerName(), StandardCharsets.UTF_8);
        log.info("유저 저장 마지막 코드 끝");

        redirectAttributes.addAttribute("summonerName",user.getSummonerName());

        return "redirect:/searchBySummonerName";
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
