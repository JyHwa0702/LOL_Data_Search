package JyHwa.LolData.Controller;

import JyHwa.LolData.Entity.Kakao;
import JyHwa.LolData.Service.KakaoService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
@Slf4j
@RequiredArgsConstructor
public class KakaoController {

    private final KakaoService kakaoService;

    @GetMapping("/oauth/kakao")
    public String kakaoLogin(@RequestParam("code") String code, HttpSession session){
        log.info("code를 받습니다 = "+code);
        String accessToken = kakaoService.getKakaoAccessToken(code);
        JsonNode kakaoInfo = kakaoService.getKakaoInfo(accessToken);
        log.info("kakaoInfo = "+kakaoInfo);

        Kakao kakao = kakaoService.saveKakao(kakaoInfo,accessToken);// 카카오 아이디 저장 및 kakaoId model넘기기

        session.setAttribute("kakaoId",kakao.getId());

        return "redirect:/";
    }

    @PostMapping("/oauth/kakao/logout")
    public String kakaoLogout(String accessToken){
        Long kakaoLogoutId = kakaoService.kakaoLogout(accessToken);
        log.info("로그아웃된 kakaoId = "+kakaoLogoutId);

        return "/";
    }

}
