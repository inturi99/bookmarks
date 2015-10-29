-- name: get-bookmarks
-- get all book marks.
SELECT * from bookmarks

-- name: get-bookmarks-by-title
-- search by title
SELECT * from bookmarks
WHERE title = :title

-- name: create-bookmark
InSERT INTO bookmarks
    (title,url,description)
    VALUES (:title,:url,:description)
    RETURNING id,title,url,description

-- name: get-tag-by-name
SELECT * from tags
WHERE tagname = :tagname

-- name: create-tag
INSERT INTO tags
    (tagname)
    VALUES(:tagname)
    RETURNING id

--name: create-bookmark-tag
INSERT INTO bookmarks_tags
    (bookmarks_id,tags_id)
    VALUES (:bookmarkid,:tagid)
    RETURNING bookmarks_id,tags_id
