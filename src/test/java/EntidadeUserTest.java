import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import com.google.gson.Gson;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // Ativa a ordenação
public class EntidadeUserTest {

    static String ct = "application/json"; // Tipo de conteúdo
    static String uriUser = "https://petstore.swagger.io/v2/user/"; // URL da API
    int id = 5; // ID do usuário
    String username = "juca"; // Username do usuário
    String firstName = "joao"; // Nome do usuário
    String lastName = "Silva"; // Sobrenome do usuário
    String email = "juca@email.com"; // Email do usuário

    // Função de leitura de Json
    public static String lerArquivoJson(String arquivoJson) throws IOException {
        return new String(Files.readAllBytes(Paths.get(arquivoJson)));
}

    @Order(1) // Define a ordem de execução
    @Test
    public void testPostUser() throws IOException {
        String jsonBody = lerArquivoJson("src/test/resources/json/user.json"); // Lê o arquivo JSON

        // Executando o teste POST para criar o usuário
        given()
                .contentType(ct) // Definindo o tipo de conteúdo
                .log().all() // Exibindo os logs da requisição
                .body(jsonBody) // Enviando o corpo com os dados do usuário
            .when()
                .post(uriUser) // Fazendo a requisição POST
            .then()
                .log().all() // Exibindo os logs da resposta
                .statusCode(200) // Verificando o status de resposta 200 (OK)
                .body("code", is(200)) // Verificando se o código da resposta é 200
                .body("type", is("unknown")) // Verificando se o tipo da resposta é desconhecido
                .body("message", is("5"));// Verificando se a mensagem contém o username
                
        // Exibindo a resposta (opcional)
        System.out.println("Resposta do POST: " + jsonBody);
    }

    
    @Order(2)
    @Test
    public void testGetUser() {


    // Nome do usuário que será obtido
    String username = "juca"; // Altere para o username que você criou com o POST

    // Executando o teste GET para buscar o usuário
    given()
            .contentType(ct) // Definindo o tipo de conteúdo
            .log().all() // Exibindo os logs da requisição
        .when()
            .get(uriUser + username) // Fazendo a requisição GET com o username
        .then()
            .log().all() // Exibindo os logs da resposta
            .statusCode(200) // Verificando o status de resposta 200 (OK)
            .body("username", is("juca")) // Verificando se o campo username na resposta é o correto
            .body("firstName", is("joao")) // Verificando se o nome está correto
            .body("lastName", is("Silva")) // Verificando se o sobrenome está correto
            .body("email", is("juca@email.com")) // Verificando se o email está correto
            .body("phone", is("999999999")) // Verificando se o telefone está correto
            .body("userStatus", is(1));  // Verificando se o status do usuário está correto
   
}

    @Order(3)
    @Test
    public void testPutUser() throws IOException {
                
        // Executando o teste PUT para alterar o email do usuário
        
        // Lê o JSON atualizado do arquivo
        String jsonBody = lerArquivoJson("src/test/resources/json/user2.json");

        String username = "juca"; // Altere para o username que você criou com o POST

        given()
                .contentType(ct) // Definindo o tipo de conteúdo
                .body(jsonBody) // Enviando o corpo com os dados do usuário
                .log().all() // Exibindo os logs da requisição
            .when()
                .put(uriUser + username) // Fazendo a requisição PUT com o username
            .then()
                .log().all() // Exibindo os logs da resposta
                .statusCode(200) // Verificando o status de resposta 200 (OK)
                .body("code", is(200)) // Verificando se o código da resposta é 200
                .body("type", is("unknown")) // Verificando se o tipo da resposta é desconhecido
                .body("message", equalTo(String.valueOf(id))); // Comparando dinamicamente com o ID do JSON
                



                
                // Executando o teste GET para verificar se o email foi alterado
            given()
                .contentType(ct) // Definindo o tipo de conteúdo
                .log().all() // Exibindo os logs da requisição  
            .when()
                .get(uriUser + username) // Fazendo a requisição GET com o username
            .then()
                .log().all() // Exibindo os logs da resposta
                .statusCode(200) // Verificando o status de resposta 200 (OK                
                .body("id", is(5)) // Verificando se o ID está correto
                .body("username", is("juca")) // Verificando se o username está correto
                .body("firstName", is("fazer")) // Verificando se o nome está correto
                .body("lastName", is("Silva")) // Verificando se o sobrenome está correto
                .body(    "email", is( "josep@email.com")) // Verificando se o email foi alterado
                .body("phone", is("999999999")) // Verificando se o telefone está correto   
                .body("userStatus", is(1)); // Verificando se o status do usuário está correto
        
              

    }   

    
    @Order(4)
    @Test
    public void testDeleteUser() {
        
        // Definindo o username a ser excluído
        String username = "juca"; // Altere para o username do usuário que você criou

        // Executando o teste DELETE para excluir o usuário
        given()
                .contentType(ct) // Definindo o tipo de conteúdo
                .log().all() // Exibindo os logs da requisição
            .when()
                .delete(uriUser + username) // Fazendo a requisição DELETE com o username
            .then()
                .log().all() // Exibindo os logs da resposta
                .statusCode(200) // Verificando o status de resposta 200 (OK)
                .body("code", is(200)) // Verificando se o código da resposta é 200
                .body("message", is("juca")); // Verificando se a mensagem contém o username

       
    }

 
    // Data Driven Testing (DDT) / Teste Direcionado por Dados / Teste com Massa
    // Teste com Json parametrizado

    @ParameterizedTest
    @Order(5)
    @CsvFileSource(resources = "/csv/UserMassa.csv", numLinesToSkip = 1)   
  
    public void testPostUserDDT(String id, String username, String firstName, String lastName, String email, String password, String phone, String userStatus) {
        // Criando o objeto Gson
        Gson gson = new Gson();

        // Criando o objeto User
        User user = new User(Integer.parseInt(id), username, firstName, lastName, email, password, phone, Integer.parseInt(userStatus));

        // Convertendo o objeto User para Json
        String userJson = gson.toJson(user);

        // Executando o teste POST para criar o usuário
        Response response = given()
                .contentType(ct) // Definindo o tipo de conteúdo
                .body(userJson) // Enviando o corpo com os dados do usuário
                .log().all() // Exibindo os logs da requisição
                .when()
                .post(uriUser) // Fazendo a requisição POST
                .then()
                .log().all() // Exibindo os logs da resposta
                .statusCode(200) // Verificando o status de resposta 200 (OK)
                .body("code", is(200)) // Verificando se o código da resposta é 200
                .body("message", equalTo(String.valueOf(user.getId()))) // Verificando se a mensagem contém o ID do usuário
                .extract() // Extraindo a resposta
                .response();

        // Exibindo o conteúdo da resposta
        System.out.println("Resposta do POST: " + response.asString());
    }
}