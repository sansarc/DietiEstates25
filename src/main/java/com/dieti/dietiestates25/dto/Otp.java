package com.dieti.dietiestates25.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Otp {

    private String email;
    private String otp;

    public Otp(String email, String otp) {
        this.email = email;
        this.otp = otp;
    }

    @Getter
    @Setter
    public static class NewPassword {

        String email;

        @SerializedName("pwd")
        private String oldPwd;

        @SerializedName("temporaryPwd")
        private String newPwd;

        public NewPassword(String email, String oldPwd, String newPwd) {
            this.email = email;
            this.oldPwd = oldPwd;
            this.newPwd = newPwd;
        }
    }
}
