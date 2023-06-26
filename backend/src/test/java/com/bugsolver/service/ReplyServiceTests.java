package com.bugsolver.service;

import com.bugsolver.entity.Bug;
import com.bugsolver.entity.Reply;
import com.bugsolver.entity.User;
import com.bugsolver.exception.bug.BugNotFoundException;
import com.bugsolver.exception.reply.ReplyNotFoundException;
import com.bugsolver.repository.ReplyRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ReplyServiceTests {

    @Autowired
    private ReplyService replyService;
    @MockBean
    private BugService bugService;
    @MockBean
    private ReplyRepository replyRepository;

    @DisplayName("WHEN save reply with valid data and valid bug SHOULD save and return created reply")
    @Test
    public void saveReplyWithValidDataAndValidBug(){
        Bug bug = new Bug();
        bug.setId(1L);

        Reply newReply = new Reply();
        newReply.setId(1L);
        newReply.setBug(bug);

        when(bugService.existsById(anyLong())).thenReturn(true);
        when(replyRepository.save(any(Reply.class))).thenReturn(newReply);

        assertThat(replyService.save(newReply)).isEqualTo(newReply);

    }

    @DisplayName("WHEN save reply with valid data and invalid bug SHOULD save and return created reply")
    @Test
    public void saveReplyWithValidDataAndInvalidBug(){
        Bug bug = new Bug();
        bug.setId(1L);

        Reply newReply = new Reply();
        newReply.setId(1L);
        newReply.setBug(bug);

        when(bugService.existsById(anyLong())).thenReturn(false);
        when(replyRepository.save(any(Reply.class))).thenReturn(newReply);

        Assertions.assertThrows(BugNotFoundException.class,
                () -> replyService.save(newReply)
        );

    }

    @DisplayName("WHEN update reply with valid id SHOULD update and return reply")
    @Test
    public void updateReplyWithValidId(){
        Reply oldReply = new Reply();
        oldReply.setId(1L);

        Reply newReply = new Reply();
        newReply.setId(1L);
        newReply.setDescription("Teste");

        when(replyRepository.findById(anyLong())).thenReturn(Optional.of(oldReply));
        when(replyRepository.save(any(Reply.class))).thenReturn(newReply);

        assertThat(replyService.update(oldReply.getId(), newReply)).isEqualTo(newReply);

    }

    @DisplayName("WHEN update reply with invalid id SHOULD throw ReplyNotFoundException")
    @Test
    public void updateReplyWithInvalidId(){

        Reply newReply = new Reply();
        newReply.setId(1L);
        newReply.setDescription("Teste");

        when(replyRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(replyRepository.save(any(Reply.class))).thenThrow(new ReplyNotFoundException());

        Assertions.assertThrows(ReplyNotFoundException.class,
                () -> replyService.update(anyLong(), newReply)
        );
    }

    @DisplayName("WHEN update bug best answer with valid reply and bug id SHOULD update reply")
    @Test
    public void updateBestAnswerWithValidIdAndValidBugId(){
        Bug bug = new Bug();
        bug.setId(1L);

        Reply reply = new Reply();
        reply.setId(1L);

        when(bugService.existsById(anyLong())).thenReturn(true);
        when(replyRepository.existsByIdAndUser_Username(anyLong(), anyString())).thenReturn(true);
        when(replyRepository.findById(anyLong())).thenReturn(Optional.of(reply));
        when(replyRepository.save(any(Reply.class))).thenReturn(reply);

        replyService.updateBestAnswer(bug.getId(),reply.getId());
        verify(replyRepository, times(1)).save(any(Reply.class));
    }

    @DisplayName("WHEN update bug best answer with invalid bug id SHOULD throw BugNotFoundException")
    @Test
    public void updateBestAnswerWithValidIdAndInvalidBugId(){

        Reply reply = new Reply();
        reply.setId(1L);

        when(bugService.existsById(anyLong())).thenReturn(false);
        when(replyRepository.existsByIdAndUser_Username(anyLong(), anyString())).thenReturn(true);
        when(replyRepository.findById(anyLong())).thenReturn(Optional.of(reply));
        when(replyRepository.save(any(Reply.class))).thenReturn(reply);

        Assertions.assertThrows(BugNotFoundException.class,
                () -> replyService.updateBestAnswer(anyLong(), reply.getId())
        );
    }

    @DisplayName("WHEN update bug best answer with invalid reply id SHOULD throw ReplyNotFoundException")
    @Test
    public void updateBestAnswerWithInvalidIdAndValidBugId(){
        Reply reply = new Reply();
        reply.setId(1L);

        Bug bug = new Bug();
        bug.setId(1L);

        when(bugService.existsById(anyLong())).thenReturn(true);
        when(replyRepository.existsByIdAndUser_Username(anyLong(), anyString())).thenReturn(false);
        when(replyRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(replyRepository.save(any(Reply.class))).thenReturn(reply);

        Assertions.assertThrows(ReplyNotFoundException.class,
                () -> replyService.updateBestAnswer(bug.getId(),anyLong())
        );
    }

    @DisplayName("WHEN find reply with valid id SHOULD return reply")
    @Test
    public void findReplyWithValiId(){

        Reply reply = new Reply();
        reply.setId(1L);

        when(replyRepository.findById(anyLong())).thenReturn(Optional.of(reply));
        assertThat(replyService.findById(reply.getId())).isEqualTo(reply);
    }

    @DisplayName("WHEN delete reply SHOULD delete reply")
    @Test
    public void deleteReply(){
        Reply reply = new Reply();

        doNothing().when(replyRepository).delete(any(Reply.class));
        replyService.delete(reply);
        verify(replyRepository, times(1)).delete(reply);
    }

    @DisplayName("WHEN check if is reply author with user being the reply author SHOULD return true")
    @Test
    public void checkIfIsReplyOwnerWithUserBeingOwner(){
        User user = new User();
        user.setId(1L);
        user.setUsername("Michael");

        Reply reply = new Reply();
        reply.setId(1L);
        reply.setUser(user);

        when(replyRepository.existsByIdAndUser_Username(anyLong(), anyString())).thenReturn(true);
        assertThat(replyService.isReplyAuthor(reply.getId(), user.getUsername())).isTrue();
    }

    @DisplayName("WHEN check if is reply author with user not being the reply author SHOULD return true")
    @Test
    public void checkIfIsReplyOwnerWithUserNotBeingOwner(){
        User user = new User();
        user.setId(1L);
        user.setUsername("Michael");

        Reply reply = new Reply();
        reply.setId(1L);

        when(replyRepository.existsByIdAndUser_Username(anyLong(), anyString())).thenReturn(false);
        assertThat(replyService.isReplyAuthor(reply.getId(), user.getUsername())).isFalse();
    }


}
