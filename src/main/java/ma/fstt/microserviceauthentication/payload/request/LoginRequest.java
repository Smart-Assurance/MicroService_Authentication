package ma.fstt.microserviceauthentication.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class LoginRequest {
    @NotBlank
    @Size(max = 20)
    private String username;

    @Size(min = 6, max = 40)
    private String password;
}
