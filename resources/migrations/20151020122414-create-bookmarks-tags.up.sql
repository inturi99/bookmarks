CREATE TABLE IF NOT EXISTS bookmarks_tags (
bookmarks_id integer references bookmarks,
tags_id integer references tags,
PRIMARY KEY (bookmarks_id, tags_id)
);
