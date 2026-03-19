package com.kejiahp.musicx.apps.user;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.graphql.test.autoconfigure.GraphQlTest;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import com.kejiahp.musicx.config.GlobalGraphQLExceptionHandler;
import com.kejiahp.musicx.config.GraphQLConfiguration;

@GraphQlTest(controllers = UserController.class)
@Import({ GraphQLConfiguration.class, GlobalGraphQLExceptionHandler.class })
public class UserGraphQLTest {
    @Autowired
    private GraphQlTester graphQlTester;

    // private static final Logger log =
    // LoggerFactory.getLogger(SongGraphQLTest.class);

    @Test
    void me_shouldReturnAuthenticatedUser() {
        // Arrange: create a mock authenticated user
        UserModel mockUser = new UserModel();
        mockUser.setName("james");
        mockUser.setEmail("james@example.com");
        mockUser.setPassword("lolcat");

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(mockUser, null, List.of());

        SecurityContextHolder.getContext().setAuthentication(auth);

        // GraphQL query
        String query = """
                    query {
                      me {
                        success
                        message
                        data {
                          name
                          email
                        }
                      }
                    }
                """;

        graphQlTester.document(query)
                .execute()
                .path("me.success").entity(Boolean.class).isEqualTo(true)
                .path("me.message").entity(String.class).isEqualTo("Authenticated User")
                .path("me.data.name").entity(String.class).isEqualTo("james")
                .path("me.data.email").entity(String.class).isEqualTo("james@example.com");
    }

    @Test
    void me_shouldFailToReturnAuthenticatedUser() {
        String query = """
                    query {
                      me {
                        success
                        message
                        data {
                          name
                          email
                        }
                      }
                    }
                """;

        var response = graphQlTester.document(query).execute();

        // The JWT Filter doesnt run so
        // `SecurityContextHolder.getContext().setAuthentication(auth)` can't be null
        response.errors().satisfy(errorList -> {
            assertTrue(errorList.size() > 0);
            assertTrue(errorList.get(0).getMessage()
                    .contains("Insufficient authentication, please reauthenticate and try again"));
        });
    }
}
