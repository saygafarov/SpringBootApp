package com.edu.ulab.app.controller;

import static com.edu.ulab.app.web.constant.WebConstant.VERSION_URL;

import com.edu.ulab.app.config.ControllerConfig;
import com.edu.ulab.app.config.PostgreSqlContainerConfig;
import com.edu.ulab.app.facade.UserDataFacade;
import com.edu.ulab.app.web.UserController;
import com.edu.ulab.app.web.handler.ControllerExceptionHandler;
import com.edu.ulab.app.web.request.*;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import com.google.gson.Gson;
import org.springframework.validation.Validator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@DisplayName("User controller test")
@ContextConfiguration(classes = {PostgreSqlContainerConfig.class, ControllerConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class UserControllerTest {

    private static MockMvc mockMvc;

    private static Gson gson = new GsonBuilder().create();

    @BeforeAll
    public static void init(@Autowired UserDataFacade userDataFacade, @Autowired Validator validator) {
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userDataFacade))
                .setControllerAdvice(new ControllerExceptionHandler())
                .setValidator(validator)
                .build();
    }

    @Test
    @DisplayName("Create user with books")
    @Rollback
    void createUserWithBooksTest() {
        UserRequest userRequest= new UserRequest();
        userRequest.setFullName("Test name");
        userRequest.setTitle("Test title");
        userRequest.setAge(19);

        BookRequest bookRequest = new BookRequest();
        bookRequest.setAuthor("Author test");
        bookRequest.setTitle("Title test");
        bookRequest.setPageCount(1000);

        UserBookRequest userBookRequest = new UserBookRequest();
        userBookRequest.setUserRequest(userRequest);
        userBookRequest.setBookRequests(List.of(bookRequest));

        String json = gson.toJson(userBookRequest);

        try {
            mockMvc.perform(post(VERSION_URL + "/user/create")
                        .header("rqid", "request100")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.userId").exists())
                    .andExpect(jsonPath("$.booksIdList.length()").value(1));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Update user and books")
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    public void updateUserWithBooksTest() {
        UpdateUserRequest updateUserRequest= new UpdateUserRequest();
        updateUserRequest.setId(1001L);
        updateUserRequest.setFullName("Test name");
        updateUserRequest.setTitle("Test title");
        updateUserRequest.setAge(19);

        UpdateBookRequest updateBookRequest = new UpdateBookRequest();
        updateBookRequest.setId(2002L);
        updateBookRequest.setAuthor("Author test");
        updateBookRequest.setTitle("Title test");
        updateBookRequest.setPageCount(1000);

        UpdateUserBookRequest updateUserBookRequest = new UpdateUserBookRequest();
        updateUserBookRequest.setUserRequest(updateUserRequest);
        updateUserBookRequest.setBookRequests(List.of(updateBookRequest));

        String json = gson.toJson(updateUserBookRequest);

        try {
            mockMvc.perform(put(VERSION_URL + "/user/update")
                    .content(json)
                    .contentType(MediaType.APPLICATION_JSON)
            )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.userId").exists())
                    .andExpect(jsonPath("$.userId").value(1001L))
                    .andExpect(jsonPath("$.booksIdList.length()").value(1))
                    .andExpect(jsonPath("$.booksIdList[0]").value(2002L));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Get user and books")
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void getUserWithBooksTest() {
        try {
            mockMvc.perform(get(VERSION_URL + "/user/get/1001"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.userId").exists())
                    .andExpect(jsonPath("$.userId").value(1001L))
                    .andExpect(jsonPath("$.booksIdList.length()").value(2))
                    .andExpect(jsonPath("$.booksIdList[0]").value(2002L))
                    .andExpect(jsonPath("$.booksIdList[1]").value(3003L));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Delete user with books")
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void deleteUserWithBooksTest() {
        try {
            mockMvc.perform(delete(VERSION_URL + "/user/delete/1001"))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Get non existent user")
    public void getNotExistingUserTest() {
        try {
            mockMvc.perform(get(VERSION_URL + "/user/get/0"))
                    .andExpect(status().isBadRequest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Update non existent user")
    public void updateNotExistingUserTest() {
        try {
            mockMvc.perform(put(VERSION_URL + "/user/update"))
                    .andExpect(status().isBadRequest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Delete non existent user")
    public void deleteNotExistingUserTest() {
        try {
            mockMvc.perform(delete(VERSION_URL + "/user/delete/0"))
                    .andExpect(status().isBadRequest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Create non existent user")
    public void createNotValidUserTest() {
        UserRequest userRequest = new UserRequest();
        userRequest.setAge(-1);
        userRequest.setTitle("TEST");
        userRequest.setFullName("TEST TEST");

        UserBookRequest userBookRequest = new UserBookRequest();
        userBookRequest.setUserRequest(userRequest);
        userBookRequest.setBookRequests(List.of());

        String json = gson.toJson(userBookRequest);

        try {
            mockMvc
                    .perform(post(VERSION_URL + "/user/create")
                            .header("rqid", "testrequest")
                            .content(json)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}