CREATE TABLE Bugs (
    id SERIAL PRIMARY KEY NOT NULL,
    title VARCHAR NOT NULL,
    code VARCHAR NOT NULL,
    description VARCHAR NOT NULL,
    user_id INT,
    CONSTRAINT user_bugs_fk FOREIGN KEY (user_id)
                  REFERENCES Users(id)
                  ON UPDATE CASCADE
                  ON DELETE CASCADE
);