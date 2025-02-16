package com.dieti.dietiestates25.constants;

public final class Constants {
    private Constants() {} // Prevent instantiation

    public static final class ApiEndpoints {
        private ApiEndpoints() {}

        private static final String PORT = "8082";
        private static final String BASE_URL = "http://localhost:" + PORT;
        public static final String LOGIN = BASE_URL + "/login";
        public static final String SIGNUP = BASE_URL + "/createUser";
        public static final String OTP_CONFIRMATION = BASE_URL + "/confirmUser";
    }

    public static final class Codes {
        private Codes() {}

        public static final int OK = 200;
        public static final int CREATED = 201;
        public static final int NOT_FOUND = 404;
    }
}
