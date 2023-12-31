package ma.fstt.microserviceauthentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
//@EnableDiscoveryClient
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
// For testing implements CommandLineRunner
@EnableMongoRepositories
public class MicroserviceAuthenticationApplication{
//    @Autowired
//    UserRepository userRepository;
//
//
//    public void testSave(){
//        // Save a user
//        Client user = new Client();
//        user.setL_name("Doe");
//        user.setF_name("John");
//        user.setUsername("john_doe");
//        user.setPassword("password123");
//        user.setEmail("john.doe@example.com");
//        user.setPhone("123456789");
//        user.setCity("City");
//        user.setAddress("123 Street");
//        user.setRole("USER"); // or set the appropriate role
//
//// Fields specific to Client
//        user.setAdd_wallet_cli("wallet123");
//        user.setCin("CIN123");
//        user.setDate_of_birth(new Date());
//
//// Save the user to the repository
//        userRepository.save(user);
//        ;
//    }
//    @Override
//    public void run(String... args) {
//        System.out.println("=========save========");
//        testSave();
//    }

    public static void main(String[] args) {
        SpringApplication.run(MicroserviceAuthenticationApplication.class, args);
    }

}
