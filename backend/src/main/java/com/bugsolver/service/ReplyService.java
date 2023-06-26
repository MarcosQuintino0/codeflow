package com.bugsolver.service;

import com.bugsolver.entity.Reply;
import com.bugsolver.exception.bug.BugNotFoundException;
import com.bugsolver.exception.reply.ReplyNotFoundException;
import com.bugsolver.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final BugService bugService;

    public Reply save(Reply reply){
        if(!bugService.existsById(reply.getBug().getId())){
            throw new BugNotFoundException();
        }

        return replyRepository.save(reply);

    }

    public Reply update(Long id, Reply reply){
        Reply oldReply = findById(id);
        oldReply.setDescription(reply.getDescription());

        return replyRepository.save(oldReply);
    }

    @Transactional
    public void updateBestAnswer(Long bugId, Long replyId){
        if(!bugService.existsById(bugId)){
            throw new BugNotFoundException();
        }

        replyRepository.removeActualBestAnswer(bugId);

        Reply reply = findById(replyId);
        reply.setBestAnswer(true);
        replyRepository.save(reply);
    }

    public Reply findById(Long id){
        return replyRepository.findById(id).orElseThrow(
                () -> new ReplyNotFoundException()
        );
    }

    public Page<Reply> findAllByBugId(Pageable pageable, Long bugId){
        return replyRepository.findAllByBugId(pageable, bugId);
    }

    public void delete(Reply reply){
        replyRepository.delete(reply);
    }

    public boolean isReplyAuthor(Long replyId, String author){
        return replyRepository.existsByIdAndUser_Username(replyId, author);
    }
}
