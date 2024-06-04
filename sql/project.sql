use prj2;
SELECT *
FROM board;
SELECT *
FROM member;

DESC board;
DESC member;

ALTER TABLE member
    DROP COLUMN writer;

ALTER TABLE member
    ADD COLUMN nick_name VARCHAR(20) NOT NULL UNIQUE after password;

ALTER TABLE member
    MODIFY id INT NOT NULL AUTO_INCREMENT;

