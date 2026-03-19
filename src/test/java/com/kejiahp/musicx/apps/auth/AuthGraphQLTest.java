package com.kejiahp.musicx.apps.auth;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.graphql.test.autoconfigure.GraphQlTest;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.kejiahp.musicx.apps.user.UserModel;
import com.kejiahp.musicx.apps.user.UserService;
import com.kejiahp.musicx.config.GlobalGraphQLExceptionHandler;
import com.kejiahp.musicx.config.GraphQLConfiguration;
import com.kejiahp.musicx.util.exceptions.validation.EmailAlreadyExistsException;
import com.kejiahp.musicx.util.exceptions.validation.InvalidCredentials;

@GraphQlTest(controllers = AuthController.class)
@Import({ GraphQLConfiguration.class, GlobalGraphQLExceptionHandler.class })
public class AuthGraphQLTest {

    @Autowired
    private GraphQlTester graphQlTester;

    @MockitoBean
    private UserService userService;

    @Test
    void signup_shouldCreateUser() {
        UserModel mockUser = new UserModel();
        mockUser.setId(UUID.randomUUID());
        mockUser.setName("james");
        mockUser.setEmail("james@example.com");
        mockUser.setPassword("lolcat");

        Mockito.when(userService.createUser("james", "james@example.com", "lolcat"))
                .thenReturn(mockUser);

        String mutation = """
                    mutation {
                      signup(name: "james", email: "james@example.com", password: "lolcat") {
                        success
                        message
                        data {
                          id
                          name
                          email
                        }
                      }
                    }
                """;

        graphQlTester.document(mutation)
                .execute()
                .path("signup.success").entity(Boolean.class).isEqualTo(true)
                .path("signup.message").entity(String.class).isEqualTo("User successfully created")
                .path("signup.data.name").entity(String.class).isEqualTo("james")
                .path("signup.data.email").entity(String.class).isEqualTo("james@example.com");
    }

    @Test
    void signup_shouldReturnError_whenEmailExists() {
        Mockito.when(userService.createUser("james", "james@example.com", "lolcat"))
                .thenThrow(new EmailAlreadyExistsException());

        String mutation = """
                    mutation {
                      signup(name: "james", email: "james@example.com", password: "lolcat") {
                        success
                        message
                        data {
                          id
                        }
                      }
                    }
                """;

        graphQlTester.document(mutation)
                .execute()
                .errors()
                .satisfy(errors -> {
                    assertFalse(errors.isEmpty());
                    assertTrue(errors.get(0).getMessage().contains("Email already exists"));
                })
                .path("signup").valueIsNull(); // GraphQL spec: field becomes null on error
    }

    @Test
    void login_shouldReturnToken() {
        Mockito.when(userService.authenticateUser("james@example.com", "lolcat"))
                .thenReturn("mock-jwt-token");

        String mutation = """
                    mutation {
                      login(email: "james@example.com", password: "lolcat") {
                        success
                        message
                        data {
                          token
                        }
                      }
                    }
                """;

        graphQlTester.document(mutation)
                .execute()
                .path("login.success").entity(Boolean.class).isEqualTo(true)
                .path("login.message").entity(String.class).isEqualTo("Login successful")
                .path("login.data.token").entity(String.class).isEqualTo("mock-jwt-token");
    }

    @Test
    void login_shouldReturnError_whenInvalidCredentials() {
        Mockito.when(userService.authenticateUser("james@example.com", "wrongpass"))
                .thenThrow(new InvalidCredentials());

        String mutation = """
                    mutation {
                      login(email: "james@example.com", password: "wrongpass") {
                        success
                        message
                        data {
                          token
                        }
                      }
                    }
                """;

        graphQlTester.document(mutation)
                .execute()
                .errors()
                .satisfy(errors -> {
                    assertFalse(errors.isEmpty());
                    assertTrue(errors.get(0).getMessage().contains("Invalid Credentials"));
                })
                .path("login").valueIsNull();
    }
}