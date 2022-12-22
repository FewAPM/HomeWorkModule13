package hw13;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class HW13 {
    public static void main(String[] args) throws IOException, InterruptedException {

        HttpClient client = HttpClient.newBuilder().build();

        String myUserForJson = Files.readString
                (Path.of("/Users/yaroslavstarenkij/Desktop/GoIT/GIT/HomeWorkModule13/src/main/java/hw13/myUser.json"));

//      створення нового об'єкта Завдання 1
        createObject(client, myUserForJson);

//      оновлення об'єкту Завдання 1
        changeObject(client, myUserForJson);

//      видалення об'єкта Завдання 1
        delete(client);

//      отримання інформації про всіх користувачів Завдання 1
        getAllUsers(client);

//      отримання інформації про користувача за id Завдання 1
        getUserById(client, 6);

//      отримання інформації про користувача за username Завдання 1
        getUserByName(client, "Kurtis Weissnat");

//       Доповніть програму методом, що буде виводити всі коментарі до останнього поста певного користувача і записувати
//       їх у файл Завдання 2
        getComments(1, client);

//      Доповніть програму методом, що буде виводити всі відкриті задачі для користувача з ідентифікатором X Завдання 3
        getAllActiveTasks(client, 1);

    }

    private static void createObject(HttpClient client, String myUserForJson) throws IOException, InterruptedException {
        HttpRequest requestPost = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/users"))
                .POST(HttpRequest.BodyPublishers.ofString(myUserForJson))
                .header("Content-type", "application/json")
                .build();

        HttpResponse<String> responsePost = client.send(requestPost, HttpResponse.BodyHandlers.ofString());
        System.out.println("responsePost.statusCode() = " + responsePost.statusCode());
        String created = responsePost.body();
        System.out.println("created = " + created);
    }

    private static void changeObject(HttpClient client, String myUserForJson) throws IOException, InterruptedException {
        HttpRequest requestPut = HttpRequest.newBuilder()

                .uri(URI.create("https://jsonplaceholder.typicode.com/users/1"))
                .method("PUT", HttpRequest.BodyPublishers.ofString(myUserForJson))
                .header("Content-type", "application/json")
                .build();

        HttpResponse<String> responsePut = client.send(requestPut, HttpResponse.BodyHandlers.ofString());
        System.out.println("responsePut.statusCode() = " + responsePut.statusCode());
        String changedUser = responsePut.body();
        System.out.println("changedUser = " + changedUser);
    }

    private static void delete(HttpClient client) throws IOException, InterruptedException {
        HttpRequest requestDelete = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/users/posts/"))
                .DELETE()
                .build();
        System.out.println(client.send(requestDelete, HttpResponse.BodyHandlers.ofString()));
    }

    private static void getAllUsers(HttpClient client) throws IOException, InterruptedException {
        HttpRequest requestGet = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/users"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(requestGet, HttpResponse.BodyHandlers.ofString());
        String users = response.body();

        Type typeToken = TypeToken.getParameterized(List.class, User.class).getType();
        List<User> usersFromGson = new Gson().fromJson(users, typeToken);

        for (User user : usersFromGson) {
            System.out.println(user);
        }
    }

    public static void getUserById(HttpClient client, int id) throws IOException, InterruptedException {
        HttpRequest requestGet = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/users"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(requestGet, HttpResponse.BodyHandlers.ofString());
        String users = response.body();

        Type typeToken = TypeToken.getParameterized(List.class, User.class).getType();
        List<User> usersFromGson = new Gson().fromJson(users, typeToken);

        for (User user : usersFromGson) {
            if (user.getId() == id)
                System.out.println(user);
        }
    }

    public static void getUserByName(HttpClient client, String name) throws IOException, InterruptedException {
        HttpRequest requestGet = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/users"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(requestGet, HttpResponse.BodyHandlers.ofString());
        String users = response.body();

        Type typeToken = TypeToken.getParameterized(List.class, User.class).getType();
        List<User> usersFromGson = new Gson().fromJson(users, typeToken);

        for (User user : usersFromGson) {
            if (Objects.equals(user.getName(), name))
                System.out.println(user);
        }
    }

    private static void getAllActiveTasks(HttpClient client, int id) throws IOException, InterruptedException {
        HttpRequest requestGet = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/users/" + id + "/todos"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(requestGet, HttpResponse.BodyHandlers.ofString());
        String users = response.body();


        Type typeToken = TypeToken.getParameterized(List.class, Todos.class).getType();
        List<Todos> usersFromGson = new Gson().fromJson(users, typeToken);
        for (Todos todos : usersFromGson) {
            if (!todos.isCompleted())
                System.out.println(todos.getTitle());
        }
    }

    private static void getComments(int id, HttpClient client) throws IOException, InterruptedException {
        HttpRequest requestGet = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/users/" + id + "/posts"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(requestGet, HttpResponse.BodyHandlers.ofString());
        String posts = response.body();


        Type typeToken = TypeToken.getParameterized(List.class, Post.class).getType();
        List<Post> postsFromJson = new Gson().fromJson(posts, typeToken);

        Integer maxNumberId = postsFromJson.stream()
                .map(Post::getId)
                .skip(9)
                .collect(Collectors.toList())
                .get(0);
        System.out.println("maxNumberId = " + maxNumberId);

        HttpRequest requestGetComments = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/posts/" + maxNumberId + "/comments"))
                .GET()
                .build();
        HttpResponse<String> responseComments = client.send(requestGetComments, HttpResponse.BodyHandlers.ofString());
        String comments = responseComments.body();
        System.out.println("comments = " + comments);


        try (PrintStream out = new PrintStream(new FileOutputStream("user-" + id + "-post-" + maxNumberId + "-comments.json"))) {
            out.print(comments);
        }

    }
}
