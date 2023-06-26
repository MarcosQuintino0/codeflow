CREATE TABLE Bug_category
(
    id SERIAL PRIMARY KEY NOT NULL,
    category_id INT NOT NULL,
    bug_id INT NOT NULL,
    CONSTRAINT bug_fk FOREIGN KEY (bug_id)
        REFERENCES Bugs (id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT category_fk FOREIGN KEY (category_id)
        REFERENCES Category (id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);