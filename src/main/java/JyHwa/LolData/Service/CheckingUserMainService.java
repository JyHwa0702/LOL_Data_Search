package JyHwa.LolData.Service;

import JyHwa.LolData.Dto.KakaoDto;
import JyHwa.LolData.Dto.UserDto;
import JyHwa.LolData.Entity.Kakao;
import JyHwa.LolData.Entity.User;
import JyHwa.LolData.Repository.KakaoRepository;
import JyHwa.LolData.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
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

    @Transactional
    public User checkingUser(Long userId,Long kakaoId){
//        Optional<User> userById = userRepository.findById(userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Kakao kakao = kakaoRepository.findById(kakaoId).orElseThrow(() -> new RuntimeException("Kakao not found"));

        log.info("user = "+user+" kakao = "+kakao);

        UserDto userDto = new UserDto(user);
        userDto.setKakao(kakao);
        userDto.setCheckField(1); //체킹 당했다는 표시

        User userSavedKakao = userDto.toEntity();
        userRepository.save(userSavedKakao);

//        KakaoDto kakaoDto = new KakaoDto(kakaoById.get());
//        List<User> users = kakaoDto.getUsers();
//        users.add(userById.get());
//        kakaoDto.setUsers(users);

        log.info("userSavedKakao = "+userSavedKakao.toString());

        return userSavedKakao;
    }

    public void showCheckingUser(Long kakaoId, Model model){
        List<User> users = new ArrayList<>();

        users = kakaoService.findUsers(kakaoId);
        if(users != null){
            log.info("showCheckingUser users = "+users.toArray().toString());
            model.addAttribute("users",users);
        }
    }
    public void deleteCheckingUser(Long kakaoId, Long userId){
        kakaoService.kakaoDeleteUser(kakaoId,userId);
    }
}
