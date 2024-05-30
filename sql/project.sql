create schema prj3;
use prj2;

CREATE TABLE board
(
    id       INT PRIMARY KEY AUTO_INCREMENT,
    title    VARCHAR(100)  NOT NULL,
    content  VARCHAR(1000) NOT NULL,
    writer   VARCHAR(100)  NOT NULL,
    inserted DATETIME      NOT NULL DEFAULT NOW()
);

CREATE TABLE member
(
    id        INT PRIMARY KEY AUTO_INCREMENT,
    email     VARCHAR(100) NOT NULL UNIQUE,
    password  VARCHAR(100) NOT NULL,
    nick_name VARCHAR(100) NOT NULL UNIQUE,
    inserted  DATETIME     NOT NULL DEFAULT NOW()
);

SELECT *
FROM board;
desc board;
SELECT *
FROM member;
desc member;

# board 테이블 수정
# writer column 지우기
# member_id column reference member(id)

ALTER TABLE board
    DROP COLUMN writer;
ALTER TABLE board
    ADD COLUMN member_id INT REFERENCES member (id) AFTER content;
UPDATE board
SET member_id = (SELECT id FROM member ORDER BY id DESC LIMIT 1)
WHERE id > 0;
ALTER TABLE board
    MODIFY COLUMN member_id INT NOT NULL;
###################################
DESC board;
SELECT *
FROM board
ORDER BY id DESC;

SELECT *
FROM member
WHERE email = '456@456';

DELETE
FROM board
WHERE member_id = 37;
DELETE
FROM member
WHERE email = '456@456';

# 권한 테이블
CREATE TABLE authority
(
    member_id INT         NOT NULL REFERENCES member (id),
    name      VARCHAR(20) NOT NULL,
    PRIMARY KEY (member_id, name)
);

INSERT INTO authority (member_id, name)
VALUES (39, 'admin');

INSERT INTO board (title, content, member_id)
SELECT title, content, member_id
FROM board;

INSERT INTO board (title, content, member_id)
VALUES ('안녕하세요', '게시물', 39);

DELETE
FROM member;
SELECT *
FROM member;
SELECT *
FROM board;
SELECT *
FROM authority;
DELETE
FROM authority;

UPDATE board
SET title='thank you'
WHERE id % 3 = 0;
UPDATE board
SET title='Lucky!'
WHERE id % 3 = 1;
UPDATE board
SET title='Love you~'
WHERE id % 3 = 2;


CREATE TABLE board_file
(
    board_id INT          NOT NULL REFERENCES board (id),
    name     VARCHAR(500) NOT NULL,
    PRIMARY KEY (board_id, name)
);

# board_like 만들기
CREATE TABLE board_like
(
    board_id  INT NOT NULL REFERENCES board (id),
    member_id INT NOT NULL REFERENCES member (id),
    PRIMARY KEY (board_id, member_id)
);

SELECT b.id, COUNT(DISTINCT f.name), COUNT(l.member_id)
FROM board b
         JOIN member m ON b.member_id = m.id
         LEFT JOIN board_file f ON b.id = f.board_id
         LEFT JOIN board_like l ON b.id = l.board_id
WHERE b.id = 6;