package JyHwa.LolData.Service;

import JyHwa.LolData.Dto.KakaoDto;
import JyHwa.LolData.Dto.UserDto;
import JyHwa.LolData.Entity.Kakao;
import JyHwa.LolData.Entity.User;
import JyHwa.LolData.Repository.KakaoRepository;
import JyHwa.LolData.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CheckingUserMainService {

    private final KakaoService kakaoService;
    private final UserRepository userRepository;
    private final KakaoRepository kakaoRepository;

    public void someMethod(String message) throws IOException {

//        String accessToken = kakaoService.getKakaoAccessToken();
//
//        if (accessToken!= null){
//            String response = kakaoService.sendNotification(message, accessToken);
//            log.info("AlarmService someMethod response = "+response);
//        }else {
//            log.warn("accessToken이 존재하지 않습니다.");
//        }

    }

    public User checkingUser(Long userId,Long kakaoId){
        Optional<User> userById = userRepository.findById(userId);
        Optional<Kakao> kakaoById = kakaoRepository.findById(kakaoId);

        KakaoDto kakaoDto = new KakaoDto(kakaoById.get());
        List<User> users = kakaoDto.getUsers();
        users.add(userById.get());
        kakaoDto.setUsers(users);



        log.info("kakaoDto.getUsers() = "+kakaoDto.getUsers().toString());

        return userById.get();
    }

    public void showCheckingUser(Long kakaoId, Model model){
        List<User> users = kakaoService.findUsers(kakaoId);
        log.info("showCheckingUser users = "+users.toArray().toString());
        model.addAttribute("users",users);
    }
    public void deleteCheckingUser(Long kakaoId, Long userId){
        List<User> users = kakaoService.findUsers(kakaoId);
        kakaoService.kakaoDeleteUser(kakaoId,userId);
    }
}
