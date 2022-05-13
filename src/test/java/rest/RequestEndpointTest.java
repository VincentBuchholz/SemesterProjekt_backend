package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.RequestDTO;
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
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

//Disabled
public class RequestEndpointTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    Request request1;
    Request request2;
    User coach;

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
            em.createQuery("delete from Request").executeUpdate();
            em.createQuery("delete from UserWeighIn").executeUpdate();
            em.createQuery("delete from UserNutrition").executeUpdate();
            em.createQuery("delete from MealPlan ").executeUpdate();
            em.createQuery("delete from User").executeUpdate();
            em.createQuery("delete from Role").executeUpdate();
            coach = new User("coach1","test","test","test","test@test.dk","test");
            em.getTransaction().commit();
            em.getTransaction().begin();
            em.persist(coach);
            em.getTransaction().commit();
            em.getTransaction().begin();

            request1 = new Request(coach,"Karl","Larsson","KL@mail.dk","123111","hej med dig");
            request2 = new Request(coach,"Karl","Larsson","KL@mail.dk","123111","hej med dig");



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
    public void testRequestSent() {

        RequestDTO requestDTO = new RequestDTO(coach.getId(),"karl","fisker","karlfisker@mail.dk","726151441","hello there I wanna get big");
        String requestBody = GSON.toJson(requestDTO);

        given()
                .header("Content-type", ContentType.JSON)
                .and()
                .body(requestBody)
                .when()
                .post("/request")
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", notNullValue())
                .body("firstName", equalTo("karl"))
                .body("lastName", equalTo("fisker"))
                .body("email", equalTo("karlfisker@mail.dk"))
                .body("phone",equalTo("726151441"))
                .body("desc", equalTo("hello there I wanna get big"));
    }

    @Test
    void GetRequestsByCoachIDTest() {
        login("coach", "test");

        List<RequestDTO> requestDTOS;

        requestDTOS = given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .when()
                .get("/request/coach/"+coach.getId()).then()
                .statusCode(200)
                .extract().body().jsonPath().getList("",RequestDTO.class);
        RequestDTO requestDTO1 = new RequestDTO(request1);
        RequestDTO requestDTO2 = new RequestDTO(request2);
        assertThat(requestDTOS,containsInAnyOrder(requestDTO1,requestDTO2));
    }

    @Test
    void GetRequestByRequestID() {
        login("coach", "test");

        RequestDTO requestDTO;

        requestDTO = given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .when()
                .get("/request/"+request1.getId()).then()
                .statusCode(200)
                .extract().body().jsonPath().getObject("",RequestDTO.class);
        RequestDTO requestDTO1 = new RequestDTO(request1);

        assertThat(requestDTO,equalTo(requestDTO1));
    }
}
