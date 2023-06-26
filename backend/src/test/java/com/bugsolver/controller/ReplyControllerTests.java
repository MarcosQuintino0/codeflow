package com.bugsolver.controller;

import com.bugsolver.entity.Reply;
import com.bugsolver.entity.User;
import com.bugsolver.exception.reply.ReplyNotFoundException;
import com.bugsolver.service.ReplyService;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class ReplyControllerTests extends BaseControllerTests{

    private static final String BASE_URL = "/reply";

    @MockBean
    private ReplyService replyService;

    @DisplayName("WHEN delete a reply with existent id and same user owner SHOULD delete and return status 204 NO CONTENT")
    @Test
    public void deleteReplyWithValidIdAndSameUserOwner() throws Exception{
        User user = new User();
        user.setId(1L);
        user.setUsername(LOGGED_USERNAME);

        Reply replyToDelete = new Reply();
        replyToDelete.setId(1L);
        replyToDelete.setUser(user);

        when(replyService.findById(anyLong())).thenReturn(replyToDelete);
        when(replyService.isReplyAuthor(anyLong(), anyString())).thenReturn(true);
        doNothing().when(replyService).delete(any(Reply.class));

        mockMvc.perform(delete(BASE_URL + "/" + replyToDelete.getId())
                        .header(AUTHORIZATION, getAuthToken()))
                .andExpect(status().isNoContent());
    }

    @DisplayName("WHEN delete a reply with not the same user owner SHOULD throw NotReplyAuthorException")
    @Test
    public void deleteReplyWithValidIdAndNotSameUserOwner() throws Exception{
        User user = new User();
        user.setId(1L);
        user.setUsername("Other user");

        Reply replyToDelete = new Reply();
        replyToDelete.setId(1L);
        replyToDelete.setUser(user);

        when(replyService.findById(anyLong())).thenReturn(replyToDelete);
        when(replyService.isReplyAuthor(anyLong(), anyString())).thenReturn(false);

        mockMvc.perform(delete(BASE_URL + "/" + replyToDelete.getId())
                        .header(AUTHORIZATION, getAuthToken()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.path").value(BASE_URL + "/" + replyToDelete.getId()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(getMessage("reply.not-author")));
    }

    @DisplayName("WHEN delete a reply with invalid SHOULD throw ReplyNotFoundException")
    @Test
    public void deleteReplyWithInvalidIdAndSameUserOwner() throws Exception{
        Reply notExistentReply = new Reply();
        notExistentReply.setId(1L);

        when(replyService.findById(anyLong())).thenThrow(new ReplyNotFoundException());

        mockMvc.perform(delete(BASE_URL + "/" + notExistentReply.getId())
                        .header(AUTHORIZATION, getAuthToken()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.path").value(BASE_URL + "/" + notExistentReply.getId()))
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value(getMessage("reply.not-found")));
    }

    @DisplayName("WHEN update a reply with valid id and description SHOULD update with status 204 NO CONTENT")
    @Test
    public void updateReplyWithValidIdAndValidDescription() throws Exception{
        User user = new User();
        user.setId(1L);
        user.setUsername(LOGGED_USERNAME);

        Reply replyToUpdate = new Reply();
        replyToUpdate.setId(1L);
        replyToUpdate.setUser(user);
        replyToUpdate.setDescription("Updated description");

        when(replyService.findById(anyLong())).thenReturn(replyToUpdate);
        when(replyService.isReplyAuthor(anyLong(), anyString())).thenReturn(true);
        when(replyService.update(anyLong(), any(Reply.class))).thenReturn(replyToUpdate);

        mockMvc.perform(put(BASE_URL + "/" + replyToUpdate.getId())
                        .header(AUTHORIZATION, getAuthToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJson(replyToUpdate))
                )
                .andExpect(status().isNoContent());
    }

    @DisplayName("WHEN update a reply not being user owner SHOULD throw NotReplyAuthorException")
    @Test
    public void updateReplyNotBeingReplyAuthor() throws Exception{
        User user = new User();
        user.setId(1L);
        user.setUsername("Other user");

        Reply replyToUpdate = new Reply();
        replyToUpdate.setId(1L);
        replyToUpdate.setUser(user);
        replyToUpdate.setDescription("Updated description");

        when(replyService.isReplyAuthor(anyLong(), anyString())).thenReturn(false);

        mockMvc.perform(put(BASE_URL + "/" + replyToUpdate.getId())
                        .header(AUTHORIZATION, getAuthToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJson(replyToUpdate)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.path").value(BASE_URL + "/" + replyToUpdate.getId()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(getMessage("reply.not-author")));
    }

    @DisplayName("WHEN update a reply with invalid id SHOULD throw ReplyNotFoundException")
    @Test
    public void updateReplyWithInvalidId() throws Exception{
        Reply notExistentReply = new Reply();
        notExistentReply.setId(1L);
        notExistentReply.setDescription("Description");

        when(replyService.isReplyAuthor(anyLong(), anyString())).thenReturn(true);
        when(replyService.update(anyLong(),any(Reply.class))).thenThrow(new ReplyNotFoundException());

        mockMvc.perform(put(BASE_URL + "/" + notExistentReply.getId())
                        .header(AUTHORIZATION, getAuthToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJson(notExistentReply))
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.path").value(BASE_URL + "/" + notExistentReply.getId()))
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value(getMessage("reply.not-found")));
    }

    @DisplayName("WHEN update a reply with empty description SHOULD return BAD REQUET 400")
    @Test
    public void updateReplyWithEmptyDescription() throws Exception{
        User user = new User();
        user.setId(1L);
        user.setUsername(LOGGED_USERNAME);

        Reply replyToUpdate = new Reply();
        replyToUpdate.setId(1L);
        replyToUpdate.setUser(user);

        mockMvc.perform(put(BASE_URL + "/" + replyToUpdate.getId())
                        .header(AUTHORIZATION, getAuthToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJson(replyToUpdate)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.path").value(BASE_URL + "/" + replyToUpdate.getId()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("BAD REQUEST 400"))
                .andExpect(jsonPath("$.errors[0]").value(getMessage("reply.description.not-blank")));
    }

    private String convertToJson(Reply reply) throws JSONException {
        return new JSONObject()
                .put("description", reply.getDescription())
                .toString();
    }
}
