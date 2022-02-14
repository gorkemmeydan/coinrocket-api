package com.gorkemmeydan.coinrocketapi;

import com.gorkemmeydan.coinrocketapi.dto.AppUserDto;
import org.apache.commons.lang.RandomStringUtils;

public class TestUtils {
    public static AppUserDto generateNewUserRequest() {
        String email = generateRandomEmail(10);
        AppUserDto newUser = new AppUserDto();
        newUser.setEmail(email);
        return newUser;
    }

    public static String generateRandomEmail(int length) {
        String allowedChars = "abcdefghijklmnopqrstuvwxyz" + "1234567890" + "_-.";
        String email = "";
        String temp = RandomStringUtils.random(length, allowedChars);
        email = temp.substring(0, temp.length() - 9) + "@testdata.com";
        return email;
    }
}
