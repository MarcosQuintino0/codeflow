package com.bugsolver.controller;

import com.bugsolver.entity.Reply;
import com.bugsolver.entity.User;
import com.bugsolver.exception.reply.NotReplyAuthorException;
import com.bugsolver.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reply")
public class ReplyController {

    private final ReplyService replyService;

    @DeleteMapping("/{replyId}")
    public ResponseEntity<Void> deleteAnswer(Principal principal,
                                             @PathVariable("replyId") Long replyId
    ){
        Reply reply = replyService.findById(replyId);

        if(!replyService.isReplyAuthor(replyId, principal.getName())){
            throw new NotReplyAuthorException();
        }

        replyService.delete(reply);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{replyId}")
    public ResponseEntity<Void> updateAnswer(Principal principal,
                                             @RequestBody @Valid Reply replyUpdated,
                                             @PathVariable("replyId") Long replyId
    ){
        if(!replyService.isReplyAuthor(replyId, principal.getName())){
            throw new NotReplyAuthorException();
        }

        replyService.update(replyId, replyUpdated);
        return ResponseEntity.noContent().build();
    }
}
