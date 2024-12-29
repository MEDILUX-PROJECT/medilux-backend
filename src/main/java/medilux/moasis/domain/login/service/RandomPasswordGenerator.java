package medilux.moasis.domain.login.service;

import java.security.SecureRandom;

// 소셜 로그인시 랜덤으로 비번 생성
public class RandomPasswordGenerator {

    public static String generateRandomPassword() {
        String uppercaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowercaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String specialCharacters = "!@#$%^&*()-_=+[]{}|;:'\",.<>?/";

        String allCharacters = uppercaseLetters + lowercaseLetters + digits + specialCharacters;

        SecureRandom random = new SecureRandom();

        StringBuilder password = new StringBuilder();

        // 각각의 문자열에서 랜덤하게 하나씩 선택하여 비밀번호에 추가
        password.append(getRandomCharacter(uppercaseLetters, random));
        password.append(getRandomCharacter(lowercaseLetters, random));
        password.append(getRandomCharacter(digits, random));
        password.append(getRandomCharacter(specialCharacters, random));

        // 나머지 6자리를 나머지 문자열에서 랜덤하게 선택하여 비밀번호에 추가
        for (int i = 0; i < 6; i++) {
            password.append(allCharacters.charAt(random.nextInt(allCharacters.length())));
        }

        // 문자열을 무작위로 섞는다
        String shuffledPassword = shuffleString(password.toString(), random);

        return shuffledPassword;
    }

    private static char getRandomCharacter(String characterSet, SecureRandom random) {
        return characterSet.charAt(random.nextInt(characterSet.length()));
    }

    private static String shuffleString(String input, SecureRandom random) {
        char[] characters = input.toCharArray();
        for (int i = characters.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            char temp = characters[index];
            characters[index] = characters[i];
            characters[i] = temp;
        }
        return new String(characters);
    }

}
