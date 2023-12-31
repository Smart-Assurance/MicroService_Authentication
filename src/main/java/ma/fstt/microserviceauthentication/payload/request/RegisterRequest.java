package ma.fstt.microserviceauthentication.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
public class RegisterRequest {
    @NotBlank
    @Size(max = 30)
    private String l_name;
    @NotBlank
    @Size(max = 30)
    private String f_name;
    @NotBlank
    @Size(max = 20)
    @Email
    private String username;
    @NotBlank
    @Size(min = 6, max = 40)
    private String password;
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;
    @NotBlank
    @Size(max = 20)
    private String phone;
    @NotBlank
    @Size(max = 50)
    private String city;
    @NotBlank
    @Size(max = 200)
    private String address;
    @Size(max = 200)
    private String add_wallet_cli;
    @NotBlank
    @Size(max = 12)
    private String cin;
    @NotBlank
    private Date date_of_birth;
}
