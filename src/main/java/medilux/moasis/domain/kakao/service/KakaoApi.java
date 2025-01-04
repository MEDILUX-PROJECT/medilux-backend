package medilux.moasis.domain.kakao.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;



@Service
@Data
public class KakaoApi {

    @Value("${kakao.api_key}")
    private String kakaoApiKey;

    @Value("${kakao.redirect_uri}")
    private String kakaoRedirectUri;

    private static final Logger log = LoggerFactory.getLogger(KakaoApi.class);

    public String getAccessToken(String code) {
        String tokenUrl = "https://kauth.kakao.com/oauth/token";
        try {
            // 1) 토큰 발급 API 호출 준비
            URL url = new URL(tokenUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
            conn.setDoOutput(true); // POST로 데이터 전송 허용
            log.info("1)호출 준비: " + conn);
            // 2) POST 바디(form data) 작성
            //    grant_type=authorization_code & client_id=... & redirect_uri=... & code=...
            StringBuilder params = new StringBuilder();
            params.append("grant_type=authorization_code");
            params.append("&client_id=47782997c3158f7bee1e2371b2606fb2");
            params.append("&redirect_uri=http://localhost:8080/api/kakao/auth/login/oauth2");
            params.append("&code=").append(code);
            log.info("2)바디 작성: " + params);
            // (선택) client_secret이 ON으로 설정된 경우: params.append("&client_secret=발급받은_시크릿키");

            // 3) 요청 파라미터 전송
            try (DataOutputStream dos = new DataOutputStream(conn.getOutputStream())) {
                dos.writeBytes(params.toString());
                dos.flush();
            }

            // 4) 응답 확인 (200~299: 성공)
            int responseCode = conn.getResponseCode();
            log.info("4)응답확인: " + responseCode);
            BufferedReader br;
            if (responseCode >= 200 && responseCode < 300) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            // 5) 응답(JSON) 읽기
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();

            // 6) 응답(JSON)에서 access_token 파싱 (예시: Gson or Jackson)
            //    - 여기서는 간단히 문자열 그대로 반환
            String result = sb.toString();
            log.info("[KakaoApi.getAccessToken] responseBody = {}", result);

            // 실제로는 JSON 파싱하여 access_token만 추출해도 됨
            // 예: {"token_type":"bearer","access_token":"...","refresh_token":"...","expires_in":...}
            // Gson, Jackson, JsonObject 등을 사용해 access_token을 추출
            //
            // 여기서는 예시로 access_token을 바로 꺼내오는 간단 로직:
            JsonObject json = JsonParser.parseString(result).getAsJsonObject();
            return json.get("access_token").getAsString();
        } catch (IOException e) {
            log.error("토큰 발급 실패", e);
        }
        return null;
    }

    public HashMap<String, Object> getUserInfo(String accessToken) {
        HashMap<String, Object> userInfo = new HashMap<>();
        String reqUrl = "https://kapi.kakao.com/v2/user/me";

        try {
            // 1. 연결 설정
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST"); // POST/GET 둘 다 가능
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
            conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
            conn.setDoOutput(true); // POST 전송 시 true, GET은 굳이 필요 없음

            // (선택) POST에 파라미터를 넘겨주고 싶다면, property_keys 등을 전송할 수 있음
            // 예) --data-urlencode 'property_keys=["kakao_account.email"]'
            //      try (DataOutputStream dos = new DataOutputStream(conn.getOutputStream())) {
            //          String param = "property_keys=[\"kakao_account.email\",\"kakao_account.profile\"]";
            //          dos.writeBytes(param);
            //          dos.flush();
            //      }

            // 2. 응답 코드 확인
            int responseCode = conn.getResponseCode();
            log.info("[KakaoApi.getUserInfo] responseCode : {}", responseCode);

            BufferedReader br;
            if (responseCode >= 200 && responseCode <= 300) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            // 3. 응답(Body) 읽기
            StringBuilder responseSb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                responseSb.append(line);
            }
            String result = responseSb.toString();
            br.close();

            log.info("responseBody = {}", result);

            // 4. JSON 파싱
            JsonElement element = JsonParser.parseString(result);

            // 예: 카카오에서 응답이 정상적으로 왔다면, kakao_account 필드가 존재
            JsonObject kakaoAccount = element.getAsJsonObject().get("kakao_account").getAsJsonObject();
            JsonObject profile = kakaoAccount.get("profile").getAsJsonObject();

            // 5. 필요한 정보 추출 (동의항목에 따라 null이 될 수도 있음)
            //    (1) 이메일
            String email = kakaoAccount.has("email")
                    ? kakaoAccount.get("email").getAsString()
                    : "No email provided";
            //    (2) 닉네임
            String nickname = profile.has("nickname")
                    ? profile.get("nickname").getAsString()
                    : "No nickname provided";
            //    (3) 프로필 사진이 필요하다면 thumbnail_image_url or profile_image_url도 가능
            //    String picture = profile.has("thumbnail_image_url")
            //        ? profile.get("thumbnail_image_url").getAsString()
            //        : "No profile image";

            // 6. 추출한 정보 저장
            userInfo.put("email", email);
            userInfo.put("nickname", nickname);
            // userInfo.put("picture", picture);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return userInfo;
    }

//    public HashMap<String, Object> getUserInfo(String accessToken) {
//        HashMap<String, Object> userInfo = new HashMap<>();
//        String reqUrl = "https://kapi.kakao.com/v2/user/me";
//        try{
//            URL url = new URL(reqUrl);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("POST");
//            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
//            conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//
//            int responseCode = conn.getResponseCode();
//            log.info("[KakaoApi.getUserInfo] responseCode : {}",  responseCode);
//
//            BufferedReader br;
//            if (responseCode >= 200 && responseCode <= 300) {
//                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            } else {
//                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
//            }
//
//            String line = "";
//            StringBuilder responseSb = new StringBuilder();
//            while((line = br.readLine()) != null){
//                responseSb.append(line);
//            }
//            String result = responseSb.toString();
//            log.info("responseBody = {}", result);
//
//            JsonParser parser = new JsonParser();
//            JsonElement element = parser.parse(result);
//
////            JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
//            JsonObject kakaoAccount = element.getAsJsonObject().get("kakao_account").getAsJsonObject();
//            JsonObject profile = kakaoAccount.getAsJsonObject().get("profile").getAsJsonObject();
//
//
////            String email = kakaoAccount.getAsJsonObject().get("email").getAsString();
////            String nickname = profile.getAsJsonObject().get("nickname").getAsString();
////            String picture = profile.getAsJsonObject().get("thumbnail_image_url").getAsString();
//
//            String email = kakaoAccount.has("email") ? kakaoAccount.get("email").getAsString() : "No email provided";
//            String nickname = profile.has("nickname") ? profile.get("nickname").getAsString() : "No nickname provided";
//
//            userInfo.put("nickname", nickname);
//            userInfo.put("email", email);
//
//            br.close();
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return userInfo;
//    }
}


