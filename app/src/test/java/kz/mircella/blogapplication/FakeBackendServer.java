package kz.mircella.blogapplication;

import android.support.v7.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import okhttp3.MediaType;
import okhttp3.ResponseBody;

public class FakeBackendServer {

    private static List ALL_BLOGS = Arrays.asList(
            ResponseBody.create(MediaType.get("application/json"), "{\"title\":\"Book review:Harry Potter 1\",\"createdAt\":\"1998-12-01\",\"authorId\":\"Rowling\"}"),
            ResponseBody.create(MediaType.get("application/json"), "{\"title\":\"Book review:Harry Potter 2\",\"createdAt\":\"1999-12-01\",\"authorId\":\"Scott\"}"),
            ResponseBody.create(MediaType.get("application/json"), "{\"title\":\"Book review:Harry Potter 3\",\"createdAt\":\"2000-12-01\",\"authorId\":\"Balsac\"}"),
            ResponseBody.create(MediaType.get("application/json"), "{\"title\":\"Book review:Harry Potter 4\",\"createdAt\":\"2001-12-01\",\"authorId\":\"Shakespeare\"}"));

    private static List ALL_USERS = Arrays.asList(
            new User("Firstname1", "Surname1", 1),
            new User("Firstname2", "Surname2", 2),
            new User("Firstname3", "Surname3", 3),
            new User("Firstname4", "Surname4", 4),
            new User("Firstname5", "Surname5", 5),
            new User("Firstname6", "Surname6", 6),
            new User("Firstname7", "Surname7", 7));

    public Observable<ResponseBody> getAllBlogs() {
        return Observable.<ResponseBody>fromIterable(ALL_BLOGS);
    }

    public Observable<User> getAllUsers() {
        return Observable.<User>fromIterable(ALL_USERS);
    }


    static class User {
        String firstname;
        String surname;
        int age;

        User(String firstname, String surname, int age) {
            this.age = age;
            this.surname = surname;
            this.firstname = firstname;
        }

        @Override
        public String toString() {
            return "UserDto: " + "firstname: " + firstname + ", surname: " + surname + ", age: " + age;
        }

    }

    static class UserDto {
        String username;
        int age;

        public UserDto(String username, int age) {
            this.age = age;
            this.username = username;
        }


        @Override
        public String toString() {
            return "UserDto: " + "username: " + username + ", age: " + age;
        }
    }
}
