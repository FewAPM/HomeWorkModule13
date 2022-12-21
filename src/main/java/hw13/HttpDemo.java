package hw13;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class HttpDemo {
    public static void main(String[] args) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().build();

        /////////  Get
        HttpRequest requestGet = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/users"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(requestGet, HttpResponse.BodyHandlers.ofString());
        String body = response.body();

////////// Робимо список з Юзерів
        Type typeToken = TypeToken.getParameterized(List.class, User.class).getType();
        List<User> usersFromGson = new Gson().fromJson(body, typeToken);

        for (User user : usersFromGson) {
//            System.out.println(user);
        }
  ////////// Добавляємо Юзера на сервер

        String forJson = "{\n" +
                "  \"id\": 1,\n" +
                "  \"name\": \"Yaroslav Starenkyi\",\n" +
                "  \"username\": \"FewAPM\",\n" +
                "  \"email\": \"Yarik@gmail.com\",\n" +
                "  \"address\": {\n" +
                "    \"street\": \"Petrova\",\n" +
                "    \"suite\": \"48\",\n" +
                "    \"city\": \"Kyiv\",\n" +
                "    \"zipcode\": \"07352\"\n" +
                "  },\n" +
                "  \"geo\": {\n" +
                "    \"lat\": \"12345\",\n" +
                "    \"lng\": \"5678910\"\n" +
                "  },\n" +
                "  \"phone\": \")67242717\",\n" +
                "  \"website\": \"website\",\n" +
                "  \"company\": {\n" +
                "    \"name\": \"Forbaykery\",\n" +
                "    \"catchPhrase\": \"Devide and conquire\",\n" +
                "    \"bs\": \"Trade\"\n" +
                "  }\n" +
                "}";
        User userFromJson = new Gson().fromJson(forJson, User.class);

        String user1 = new Gson().toJson(userFromJson);


        HttpRequest requestPost = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/users"))
                .POST(HttpRequest.BodyPublishers.ofString(user1))
                .header("Content-type", "application/json")
                .build();

        HttpResponse<String> responsePost = client.send(requestPost, HttpResponse.BodyHandlers.ofString());
        System.out.println("responsePost.statusCode() = " + responsePost.statusCode());
        String created = responsePost.body();
        System.out.println("created = " + created);
        // мій новий юзер що я створив на сервері
        User createdUserFromJson = new Gson().fromJson(created, User.class);



/////////////////////////// Видалення теж працює
        HttpRequest requestDelete = HttpRequest.newBuilder()

                .uri(URI.create("https://jsonplaceholder.typicode.com/users/posts/"))
//                .method("DELETE", HttpRequest.BodyPublishers.ofString(created))
                .DELETE()
                .build();

        System.out.println(client.send(requestDelete, HttpResponse.BodyHandlers.ofString()));
/////////////////////// Метод Пут працює
        HttpRequest requestPut = HttpRequest.newBuilder()

                .uri(URI.create("https://jsonplaceholder.typicode.com/users/1"))
                .method("PUT", HttpRequest.BodyPublishers.ofString(user1))
                .header("Content-type", "application/json")
                .build();

        HttpResponse<String> body1Put = client.send(requestPut, HttpResponse.BodyHandlers.ofString());
        System.out.println("body1Put.statusCode() = " + body1Put.statusCode());
        String body1 = body1Put.body();
        System.out.println("body1 = " + body1);

    }
}
