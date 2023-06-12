package JyHwa.LolData.Service;

import JyHwa.LolData.Dto.KakaoDto;
import JyHwa.LolData.Dto.UserDto;
import JyHwa.LolData.Entity.Kakao;
import JyHwa.LolData.Entity.User;
import JyHwa.LolData.Repository.KakaoRepository;
import JyHwa.LolData.Repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Service
@PropertySource(ignoreResourceNotFound = false, value = "classpath:kakaoApiKey.properties")
@Slf4j
@RequiredArgsConstructor
public class KakaoService {
    private final UserRepository userRepository;
    private final KakaoRepository kakaoRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${kakao.restapi.key}")
    private String clientId;

    @Value("${redirect.uri}")
    private String redirectUri;

    private String grantType = "authorization_code";

    public KakaoDto getKakaoInfo(String accessToken) {

        try{
            String getInfoUrl = "https://kauth.kakao.com/oauth/token";

            Map<String,String> params = new HashMap<>();
            params.put("grant_type",grantType);
            params.put("client_id",clientId);
            params.put("redirect_uri",redirectUri);
            params.put("code",accessToken);

            String requestBody = objectMapper.writeValueAsString(params);

            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(getInfoUrl);
            httpPost.setHeader("Content-Type","application/x-www-form-urlencoded");
            httpPost.setEntity(new StringEntity(requestBody));


            CloseableHttpResponse response = httpClient.execute(httpPost);

            HttpEntity entity = response.getEntity();
            KakaoDto kakaoDto = objectMapper.readValue(entity.getContent(), KakaoDto.class);
            return kakaoDto;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return null;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
    public String sendNotification(String message,String accessToken) throws IOException {
        String sendMeUrl = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(sendMeUrl);
        httpPost.setHeader("Authorization","Bearer"+accessToken);
        httpPost.setHeader("Content-Type","application/x-www-form-urlencoded");

        List<NameValuePair> urlParameters = new ArrayList<>();
        String jsonMessage = String.format("{/\"object_type\":\"text\",\"text\":\"%s\"}", message);
        urlParameters.add(new BasicNameValuePair("template_object",jsonMessage));

        try{
            httpPost.setEntity(new UrlEncodedFormEntity(urlParameters,"UTF-8"));
            CloseableHttpResponse response = httpClient.execute(httpPost);

            return response.toString();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }finally{
            httpClient.close();
        }
    }
    public String getAccessToken(){
        RestTemplate restTemplate = new RestTemplate();


        String tokenRequestUrl = String.format("https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=%s&redirect_uri=%s", clientId,redirectUri);
        log.info("getAccessToeken tokenRequestUrl : "+tokenRequestUrl);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(tokenRequestUrl, null, String.class);
        String responseBody = responseEntity.getBody();

        try{
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            String access_token = jsonNode.get("access_token").asText();
            log.info("access_token = "+ access_token);
            return access_token;
        } catch (JsonMappingException e) {
            e.printStackTrace();
            return null;

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
    @Transactional
    public void saveUser(User user, Long kakaoId){
        Optional<Kakao> kakao = kakaoRepository.findById(kakaoId);
        if(kakao.isPresent()){
            UserDto usersDto = new UserDto(user);

            usersDto.setKakao(kakao.get()); // UserDto 객체에 Kakao 객체 삽입

            user = usersDto.toEntity();

            kakao.get().getUsers().add(user);

            kakaoRepository.save(kakao.get()); // Kakao 객체 DB에 저장
        }
    }
    @Transactional
    public List<User> findUsers(Long kakaoId){
        Optional<Kakao> kakao = kakaoRepository.findById(kakaoId);
        List<User> users = kakao.get().getUsers();
        return users;
    }
    @Transactional
    public void kakaoDeleteUser(Long kakaoId, Long userId){
        List<User> users = findUsers(kakaoId);
        Optional<User> userById = userRepository.findById(userId);
        users.remove(userById);
    }
}
