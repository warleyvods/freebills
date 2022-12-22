package controllers;

import com.freebills.FreebillsApplication;
import com.freebills.controllers.dtos.requests.LoginRequestDTO;
import com.freebills.controllers.dtos.responses.UserResponseDTO;
import com.freebills.domains.User;
import com.freebills.exceptions.handler.ExceptionFilters;
import com.freebills.gateways.UserGateway;
import com.freebills.repositories.UserRepository;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Disabled(value = "Ignorando até entender um meio de resolver o bug.")
@SpringBootTest(classes = FreebillsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AUserControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UserGateway userGateway;

    private static ApplicationContext context;

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        context = applicationContext;
    }

    @AfterAll
    public static void setUserAfter() {
        var roleAdmin = new User();
        roleAdmin.setName("Administrator");
        roleAdmin.setLogin("admin");
        roleAdmin.setAdmin(true);
        roleAdmin.setActive(true);
        roleAdmin.setPassword(new BCryptPasswordEncoder().encode("baguvix"));
        roleAdmin.setEmail("email@email.com");

        final UserRepository bean = context.getBean(UserRepository.class);
        final Optional<User> admin = bean.findByLoginIgnoreCase("admin");

        if (admin.isEmpty()) {
            bean.save(roleAdmin);
        }
    }

    @Order(1)
    @Test
    void shouldSaveUserIfUserAreAdmin() {
        userRepository.deleteAll();

        var roleAdmin = new User();
        roleAdmin.setName("Administrator");
        roleAdmin.setLogin("admin");
        roleAdmin.setAdmin(true);
        roleAdmin.setActive(true);
        roleAdmin.setPassword(new BCryptPasswordEncoder().encode("baguvix"));
        roleAdmin.setEmail("email@email.com");
        userRepository.save(roleAdmin);

        final var request = new HttpEntity<>(new LoginRequestDTO("admin", "baguvix"));
        ResponseEntity<Object> objectResponseEntity = testRestTemplate.postForEntity("/v1/auth/login", request, Object.class);
        String token = Objects.requireNonNull(objectResponseEntity.getHeaders().get("Set-Cookie")).get(0);

        final var user = new User();
        user.setName("Pudge silva");
        user.setLogin("pudge01");
        user.setEmail("pudge01@dota.com");
        user.setPassword("baguvix");
        user.setAdmin(false);
        user.setActive(false);

        final var headers = new HttpHeaders();
        headers.set("Cookie", token);

        final var newRequest = new HttpEntity<>(user, headers);

        ResponseEntity<UserResponseDTO> userResponseEntity = testRestTemplate.postForEntity("/v1/user", newRequest, UserResponseDTO.class);

        User userFound = userGateway.findById(Objects.requireNonNull(userResponseEntity.getBody()).id());

        assertNotNull(userFound);
        assertEquals(user.getLogin(), userFound.getLogin());
    }

    @Order(2)
    @Test
    void shouldNotSaveUserIfUserIsNotAdmin() {
        userRepository.deleteAll();
        var roleAdmin = new User();
        roleAdmin.setName("Administrator");
        roleAdmin.setLogin("admin");
        roleAdmin.setAdmin(false);
        roleAdmin.setActive(true);
        roleAdmin.setPassword(new BCryptPasswordEncoder().encode("baguvix"));
        roleAdmin.setEmail("email@email.com");
        userRepository.save(roleAdmin);

        final var request = new HttpEntity<>(new LoginRequestDTO("admin", "baguvix"));
        ResponseEntity<Object> objectResponseEntity = testRestTemplate.postForEntity("/v1/auth/login", request, Object.class);
        String token = Objects.requireNonNull(objectResponseEntity.getHeaders().get("Set-Cookie")).get(0);

        final var user = new User();
        user.setName("Pudge silva");
        user.setLogin("pudge01");
        user.setEmail("pudge01@dota.com");
        user.setPassword("baguvix");
        user.setAdmin(false);
        user.setActive(false);

        final var headers = new HttpHeaders();
        headers.set("Cookie", token);

        final var newRequest = new HttpEntity<>(user, headers);

        ResponseEntity<Object> userResponse = testRestTemplate.postForEntity("/v1/user", newRequest, Object.class);
        assertEquals(403, userResponse.getStatusCodeValue());
    }

    @Test
    @Disabled(value = "todo fix")
    void shouldNotLoginIfUserAreInactive() {
        userRepository.deleteAll();
        var roleAdmin = new User();
        roleAdmin.setName("Administrator");
        roleAdmin.setLogin("admin");
        roleAdmin.setAdmin(true);
        roleAdmin.setActive(false);
        roleAdmin.setPassword(new BCryptPasswordEncoder().encode("baguvix"));
        roleAdmin.setEmail("email@email.com");
        userRepository.save(roleAdmin);

        final var request = new HttpEntity<>(new LoginRequestDTO("admin", "baguvix"));
        ResponseEntity<Object> objectResponseEntity = testRestTemplate.postForEntity("/v1/auth/login", request, Object.class);
        assertEquals(500, objectResponseEntity.getStatusCodeValue());
    }

    @Order(3)
    @Test
    void shouldNotFindUserById() {
        userRepository.deleteAll();
        var roleAdmin = new User();
        roleAdmin.setName("Administrator");
        roleAdmin.setLogin("admin");
        roleAdmin.setAdmin(true);
        roleAdmin.setActive(true);
        roleAdmin.setPassword(new BCryptPasswordEncoder().encode("baguvix"));
        roleAdmin.setEmail("email@email.com");
        User user = userRepository.save(roleAdmin);

        final var request = new HttpEntity<>(new LoginRequestDTO("admin", "baguvix"));
        ResponseEntity<Object> objectResponseEntity = testRestTemplate.postForEntity("/v1/auth/login", request, Object.class);
        String token = Objects.requireNonNull(objectResponseEntity.getHeaders().get("Set-Cookie")).get(0);


        final var headers = new HttpHeaders();
        headers.set("Cookie", token);

        final var newRequest = new HttpEntity<>(null, headers);

        ResponseEntity<ExceptionFilters> userResponseEntity = testRestTemplate.exchange("/v1/user/99", HttpMethod.GET, newRequest, ExceptionFilters.class);
        assertEquals(404, userResponseEntity.getStatusCodeValue());
        assertEquals("User not found!", Objects.requireNonNull(userResponseEntity.getBody()).getTitle());
    }

    @Order(4)
    @Test
    void shouldNotFindUserByLogin() {
        userRepository.deleteAll();
        var roleAdmin = new User();
        roleAdmin.setName("Administrator");
        roleAdmin.setLogin("admin");
        roleAdmin.setAdmin(true);
        roleAdmin.setActive(true);
        roleAdmin.setPassword(new BCryptPasswordEncoder().encode("baguvix"));
        roleAdmin.setEmail("email@email.com");
        userRepository.save(roleAdmin);

        final var request = new HttpEntity<>(new LoginRequestDTO("admin", "baguvix"));
        ResponseEntity<Object> objectResponseEntity = testRestTemplate.postForEntity("/v1/auth/login", request, Object.class);
        String token = Objects.requireNonNull(objectResponseEntity.getHeaders().get("Set-Cookie")).get(0);


        final var headers = new HttpHeaders();
        headers.set("Cookie", token);

        final var newRequest = new HttpEntity<>(null, headers);

        ResponseEntity<UserResponseDTO> userResponseEntity = testRestTemplate.exchange("/v1/user/login/admin", HttpMethod.GET, newRequest, UserResponseDTO.class);
        assertEquals(200, userResponseEntity.getStatusCodeValue());
    }

    @Order(5)
    @Test
    void shouldFindAllUsers() {
        userRepository.deleteAll();
        var roleAdmin = new User();
        roleAdmin.setName("Administrator");
        roleAdmin.setLogin("admin");
        roleAdmin.setAdmin(true);
        roleAdmin.setActive(true);
        roleAdmin.setPassword(new BCryptPasswordEncoder().encode("baguvix"));
        roleAdmin.setEmail("email@email.com");
        userRepository.save(roleAdmin);

        final var request = new HttpEntity<>(new LoginRequestDTO("admin", "baguvix"));
        ResponseEntity<Object> objectResponseEntity = testRestTemplate.postForEntity("/v1/auth/login", request, Object.class);
        String token = Objects.requireNonNull(objectResponseEntity.getHeaders().get("Set-Cookie")).get(0);


        final var headers = new HttpHeaders();
        headers.set("Cookie", token);

        final var newRequest = new HttpEntity<>(null, headers);

        var userResponseEntity = testRestTemplate.exchange("/v1/user", HttpMethod.GET, newRequest, String.class);
        assertEquals(200, userResponseEntity.getStatusCodeValue());
    }

    @Order(6)
    @Test
    void shouldDeleteAUserById() {
        userRepository.deleteAll();
        var roleAdmin = new User();
        roleAdmin.setName("Administrator");
        roleAdmin.setLogin("admin");
        roleAdmin.setAdmin(true);
        roleAdmin.setActive(true);
        roleAdmin.setPassword(new BCryptPasswordEncoder().encode("baguvix"));
        roleAdmin.setEmail("email02@email.com");
        userRepository.save(roleAdmin);

        var roleUser = new User();
        roleUser.setName("Maicom");
        roleUser.setLogin("maicom");
        roleUser.setAdmin(true);
        roleUser.setActive(true);
        roleUser.setPassword(new BCryptPasswordEncoder().encode("baguvix"));
        roleUser.setEmail("email01@email.com");
        User savedUser = userRepository.save(roleUser);

        final var request = new HttpEntity<>(new LoginRequestDTO("admin", "baguvix"));
        ResponseEntity<Object> objectResponseEntity = testRestTemplate.postForEntity("/v1/auth/login", request, Object.class);
        String token = Objects.requireNonNull(objectResponseEntity.getHeaders().get("Set-Cookie")).get(0);


        final var headers = new HttpHeaders();
        headers.set("Cookie", token);

        final var newRequest = new HttpEntity<>(null, headers);

        ResponseEntity<Void> userResponseEntity = testRestTemplate.exchange("/v1/user/" + savedUser.getId(), HttpMethod.DELETE, newRequest, Void.class);
        assertEquals(204, userResponseEntity.getStatusCodeValue());
    }

    @Order(7)
    @Test
    void shouldNotDeleteAUserIfUserAreDevelop() {
        userRepository.deleteAll();
        var roleAdmin = new User();
        roleAdmin.setName("Administrator");
        roleAdmin.setLogin("admin");
        roleAdmin.setAdmin(true);
        roleAdmin.setActive(true);
        roleAdmin.setPassword(new BCryptPasswordEncoder().encode("baguvix"));
        roleAdmin.setEmail("email02@email.com");
        User savedUser = userRepository.save(roleAdmin);


        final var request = new HttpEntity<>(new LoginRequestDTO("admin", "baguvix"));
        ResponseEntity<Object> objectResponseEntity = testRestTemplate.postForEntity("/v1/auth/login", request, Object.class);
        String token = Objects.requireNonNull(objectResponseEntity.getHeaders().get("Set-Cookie")).get(0);


        final var headers = new HttpHeaders();
        headers.set("Cookie", token);

        final var newRequest = new HttpEntity<>(null, headers);

        ResponseEntity<ExceptionFilters> userResponseEntity = testRestTemplate.exchange("/v1/user/" + savedUser.getId(), HttpMethod.DELETE, newRequest, ExceptionFilters.class);
        assertEquals(401, userResponseEntity.getStatusCodeValue());
        assertEquals("You cannot delete a developer user", Objects.requireNonNull(userResponseEntity.getBody()).getDetails());
    }

    @Order(8)
    @Test
    void shouldNotDeleteAUserIfUserAreOwnUser() {
        userRepository.deleteAll();
        var roleAdmin = new User();
        roleAdmin.setName("Administrator");
        roleAdmin.setLogin("jose");
        roleAdmin.setAdmin(true);
        roleAdmin.setActive(true);
        roleAdmin.setPassword(new BCryptPasswordEncoder().encode("baguvix"));
        roleAdmin.setEmail("email02@email.com");
        User savedUser = userRepository.save(roleAdmin);


        final var request = new HttpEntity<>(new LoginRequestDTO("jose", "baguvix"));
        ResponseEntity<Object> objectResponseEntity = testRestTemplate.postForEntity("/v1/auth/login", request, Object.class);
        String token = Objects.requireNonNull(objectResponseEntity.getHeaders().get("Set-Cookie")).get(0);


        final var headers = new HttpHeaders();
        headers.set("Cookie", token);

        final var newRequest = new HttpEntity<>(null, headers);

        ResponseEntity<ExceptionFilters> userResponseEntity = testRestTemplate.exchange("/v1/user/" + savedUser.getId(), HttpMethod.DELETE, newRequest, ExceptionFilters.class);
        assertEquals(401, userResponseEntity.getStatusCodeValue());
        assertEquals("You cannot delete your own user", Objects.requireNonNull(userResponseEntity.getBody()).getDetails());
    }

    @Order(9)
    @Test
    void shouldFindUserById() {
        userRepository.deleteAll();
        var roleAdmin = new User();
        roleAdmin.setName("Administrator");
        roleAdmin.setLogin("admin");
        roleAdmin.setAdmin(true);
        roleAdmin.setActive(true);
        roleAdmin.setPassword(new BCryptPasswordEncoder().encode("baguvix"));
        roleAdmin.setEmail("email@email.com");
        User user = userRepository.save(roleAdmin);

        final var request = new HttpEntity<>(new LoginRequestDTO("admin", "baguvix"));
        ResponseEntity<Object> objectResponseEntity = testRestTemplate.postForEntity("/v1/auth/login", request, Object.class);
        String token = Objects.requireNonNull(objectResponseEntity.getHeaders().get("Set-Cookie")).get(0);


        final var headers = new HttpHeaders();
        headers.set("Cookie", token);

        final var newRequest = new HttpEntity<>(null, headers);

        ResponseEntity<UserResponseDTO> userResponseEntity = testRestTemplate.exchange("/v1/user/" + user.getId(), HttpMethod.GET, newRequest, UserResponseDTO.class);
        assertEquals(roleAdmin.getName(), Objects.requireNonNull(userResponseEntity.getBody()).name());
        assertEquals(roleAdmin.getLogin(), userResponseEntity.getBody().login());
    }
}
