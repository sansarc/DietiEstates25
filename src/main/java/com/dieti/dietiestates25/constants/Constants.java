package com.dieti.dietiestates25.constants;

public final class Constants {
    private Constants() {} // Prevent instantiation

    public static final class ApiEndpoints {
        private ApiEndpoints() {}

        public static final String PORT = "8082";
        public static final String BASE_URL = "http://localhost:" + PORT;
        public static final String LOGIN = BASE_URL + "/login";
        public static final String SIGNUP = BASE_URL + "/createUser";
        public static final String OTP_CONFIRMATION = BASE_URL + "/confirmUser";
        public static final String CREATE_AGENCY = BASE_URL + "/insertCompany";
        public static final String INSERT_AD = BASE_URL + "/insertAd";
        public static final String SEND_BID = BASE_URL + "/insertBid";
        public static final String GET_REGIONS = BASE_URL + "/getRegions";
        public static final String GET_PROVINCES = BASE_URL + "/getProvinces";
        public static final String GET_CITIES = BASE_URL + "/getCities";
    }

    public static final class Codes {
        private Codes() {}

        public static final int OK = 200;
        public static final int CREATED = 201;
        public static final int UNAUTHORIZED = 401;
        public static final int BAD_REQUEST = 400;
        public static final int INTERNAL_SERVER_ERROR = 500;
        public static final int CONFLICT = 409;
    }

    public static final class StaticPaths {
        private StaticPaths() {}

        public static final String FAVICON = "icons/favicon.ico";
        public static final String BACKGROUND = "images/background.jpg";
    }

    public static final class Colors {
        private Colors() {}

        public static final String PRIMARY_BLUE = "var(--lumo-primary-color)";
        public static final String SECONDARY_GRAY = "var(--lumo-secondary-color)";
        public static final String GRAY_OVER_WHITEMODE = "#E0E0E0";
            public static final String GRAY_OVER_DARKMODE = "#2D2F31";
    }
}
