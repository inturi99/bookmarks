-- name: get-bookmarks
-- get all book marks.
 SELECT id,title,url,description,(select t.tagname from bookmarks_tags bmt
 INNER JOIN tags t ON t.id = bmt.tags_id
 WHERE bookmarks_id = b.id) AS tag
 FROM bookmarks b

-- name: get-bookmarks-by-title
-- search by title
SELECT *,(select t.tagname from bookmarks_tags bmt
INNER JOIN tags t ON t.id = bmt.tags_id
WHERE bookmarks_id = b.id) AS tag from bookmarks b
WHERE title = :title

-- name: create-bookmark
InSERT INTO bookmarks
    (title,url,description)
    VALUES (:title,:url,:description)
    RETURNING id,title,url,description

-- name: get-tag-by-name
SELECT * from tags
WHERE tagname = :tagname

--name: search-tag
SELECT * from tags
WHERE lower(tagname) LIKE ('%' || lower(:tagname) || '%')

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
