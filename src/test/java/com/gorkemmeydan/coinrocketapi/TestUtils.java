package com.gorkemmeydan.coinrocketapi;

import com.gorkemmeydan.coinrocketapi.dto.AppUserDto;

import java.util.Random;

public class TestUtils {
    public static AppUserDto generateNewUserRequest() {
        AppUserDto newUser = new AppUserDto();
        String email = "test_" + randomString(10) + "@test.com";
        newUser.setEmail(email);
        newUser.setFullName("test");
        newUser.setPassword("testpassword");
        return newUser;
    }

    public static String randomString(int len) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .limit(len)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
