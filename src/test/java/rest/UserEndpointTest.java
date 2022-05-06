package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.RequestDTO;
import dtos.UserDTO;
import entities.Request;
import entities.Role;
import entities.User;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;

//Disabled
public class UserEndpointTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    Request request1;
    Request request2;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();

        httpServer.shutdownNow();
    }

    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the EntityClass used below to use YOUR OWN (renamed) Entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            //Delete existing users and roles to get a "fresh" database
            em.createQuery("delete from User").executeUpdate();
            em.createQuery("delete from Role").executeUpdate();
            em.createQuery("delete from Request").executeUpdate();

            request1 = new Request(1, "Karl", "Larsson", "KL@mail.dk", "123111", "hej med dig");
            request2 = new Request(1, "Karl", "Larsson", "KL@mail.dk", "123111", "hej med dig");


            Role userRole = new Role("user");
            Role adminRole = new Role("coach");
            User user = new User("user", "test");
            user.setRole(userRole);
            User admin = new User("coach", "test");
            admin.setRole(adminRole);
            em.persist(userRole);
            em.persist(adminRole);
            em.persist(user);
            em.persist(admin);
            em.persist(request1);
            em.persist(request2);
            //System.out.println("Saved test data to database");
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    //This is how we hold on to the token after login, similar to that a client must store the token somewhere
    private static String securityToken;

    //Utility method to login and set the returned securityToken
    private static void login(String role, String password) {
        String json = String.format("{username: \"%s\", password: \"%s\"}", role, password);
        securityToken = given()
                .contentType("application/json")
                .body(json)
                //.when().post("/api/login")
                .when().post("/login")
                .then()
                .extract().path("token");
        //System.out.println("TOKEN ---> " + securityToken);
    }

    private void logOut() {
        securityToken = null;
    }

    @Test
    public void serverIsRunning() {
        given().when().get("/info").then().statusCode(200);
    }

    @Test
    public void testUserCreated() {
        User user = new User("testuser", "testuserpass", "testuser", "testuser", "testuser@mail.dk", "+4565432211", 3);
        UserDTO userDTO = new UserDTO(user);
        String requestBody = GSON.toJson(userDTO);

        login("coach", "test");
        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .and()
                .body(requestBody)
                .when()
                .post("/user")
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", notNullValue())
                .body("firstName", equalTo("testuser"))
                .body("lastName", equalTo("testuser"))
                .body("email", equalTo("testuser@mail.dk"));
    }

    @Test
    public void testCreateUserUsernameIsTaken() {
        User user = new User("user", "testuserpass", "testuser", "testuser", "testuser@mail.dk", "+4565432211", 3);
        UserDTO userDTO = new UserDTO(user);
        String requestBody = GSON.toJson(userDTO);

        login("coach", "test");
        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .and()
                .body(requestBody)
                .when()
                .post("/user")
                .then()
                .assertThat()
                .statusCode(409)
                .body("message", equalTo("Username is taken"));
    }

    @Test
    void GetCustomersByCoachIDTest() {
        login("coach", "test");
        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .when()
                .get("/user/customers/1").then()
                .statusCode(200)
                .extract().body().jsonPath().getList("",RequestDTO.class);

    }
}
