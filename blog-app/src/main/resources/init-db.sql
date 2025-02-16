CREATE TABLE IF NOT EXISTS tbl_posts (
       id SERIAL PRIMARY KEY,
       title VARCHAR(255) NOT NULL,
       content TEXT NOT NULL,
       slug VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS tbl_comments (
       id SERIAL PRIMARY KEY,
       content TEXT NOT NULL,
       post_id INT NOT NULL,
       CONSTRAINT fk_post FOREIGN KEY (post_id) REFERENCES tbl_posts(id) ON DELETE CASCADE
);


INSERT INTO tbl_posts (title, content, slug) VALUES
                                                 ('First Post', 'This is the content of the first post.', 'first-post'),
                                                 ('Second Post', 'This is the content of the second post.', 'second-post');

INSERT INTO tbl_comments (content, post_id) VALUES
                                                ('Great post!', 1),
                                                ('Very informative.', 1),
                                                ('Looking forward to more!', 2);
