ALTER TABLE Answer RENAME TO Reply;
ALTER TABLE Reply RENAME CONSTRAINT bugs_answer_fk TO bugs_reply_fk;
ALTER TABLE Reply RENAME CONSTRAINT user_answer_fk TO user_reply_fk;