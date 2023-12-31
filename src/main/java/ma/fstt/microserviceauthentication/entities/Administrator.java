package ma.fstt.microserviceauthentication.entities;


import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@TypeAlias("ADMIN")
public class Administrator extends User{
}
