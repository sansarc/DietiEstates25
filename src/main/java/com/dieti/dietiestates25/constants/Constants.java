package com.dieti.dietiestates25.constants;

public final class Constants {
    private Constants() {} // Prevent instantiation

    public static final class ApiEndpoints {
        private ApiEndpoints() {}

        public static final String PORT = "8082";
        private static final String BASE_URL =  System.getenv().getOrDefault("API_BASE_URL", "http://localhost")  + ":" + PORT;
        public static final String LOGIN = BASE_URL + "/login";
        public static final String LOGOUT = BASE_URL + "/logout";
        public static final String SIGNUP = BASE_URL + "/createUser";
        public static final String CONFIRM_USER = BASE_URL + "/confirmUser";
        public static final String CREATE_AGENCY = BASE_URL + "/insertCompany";
        public static final String CREATE_AGENT = BASE_URL + "/createAgent";
        public static final String INSERT_AD = BASE_URL + "/insertAd";
        public static final String DELETE_AD = BASE_URL + "/deleteAd";
        public static final String SEND_BID = BASE_URL + "/insertBid";
        public static final String GET_REGIONS = BASE_URL + "/getRegions";
        public static final String GET_PROVINCES = BASE_URL + "/getProvinces";
        public static final String GET_CITIES = BASE_URL + "/getCities";
        public static final String GET_AGENTS = BASE_URL + "/getAgentsByCompany";
        public static final String GENERATE_OTP = BASE_URL + "/generateOtp";
        public static final String CHANGE_PWD = BASE_URL + "/changePwd";
        public static final String UPLOAD_IMAGE = BASE_URL + "/uploadImage";
        public static final String GET_IMAGES = BASE_URL + "/getImagesByAd";
        public static final String SEARCH_AD = BASE_URL + "/searchAd";
        public static final String GET_BIDS = BASE_URL + "/getBids";
        public static final String CANCEL_BID = BASE_URL + "/cancelBid";
        public static final String ACCEPT_OR_REFUSE_BID = BASE_URL + "/acceptOrRefuseBid";
        public static final String ACCEPT_OR_REFUSE_COUNTEROFFER = BASE_URL + "/acceptOrRefuseCounteroffer";
        public static final String LOGIN_3_PART = BASE_URL + "/login3part";
    }

    public static final class Codes {
        private Codes() {}

        public static final int OK = 200;
        public static final int CREATED = 201;
        public static final int INTERNAL_SERVER_ERROR = 500;
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

    public static final class LumoSpacing {
        private LumoSpacing() {}

        public static final String L = "var(--lumo-space-l)";
        public static final String M = "var(--lumo-space-m)";
        public static final String S = "var(--lumo-space-s)";
        public static final String XS = "var(--lumo-space-xs)";
    }
}
