package JyHwa.LolData.Service;

import JyHwa.LolData.Dto.KakaoDto;
import JyHwa.LolData.Entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CheckingUserMainService {

    private final KakaoService kakaoService;

    public void someMethod(String message) throws IOException {

        String accessToken = kakaoService.getAccessToken();

        if (accessToken!= null){
            String response = kakaoService.sendNotification(message, accessToken);
            log.info("AlarmService someMethod response = "+response);
        }else {
            log.warn("accessToken이 존재하지 않습니다.");
        }

    }

    public void checkingUser(User user){
        String accessToken = kakaoService.getAccessToken();
        KakaoDto kakaoDto = kakaoService.getKakaoInfo(accessToken);
        kakaoService.saveUser(user, kakaoDto.getId());
    }

    public void showCheckingUser(Long kakaoId, Model model){
        List<User> users = kakaoService.findUsers(kakaoId);
        model.addAttribute("users",users);
    }
    public void deleteCheckingUser(Long kakaoId, Long userId){
        List<User> users = kakaoService.findUsers(kakaoId);
        kakaoService.kakaoDeleteUser(kakaoId,userId);
    }
}
