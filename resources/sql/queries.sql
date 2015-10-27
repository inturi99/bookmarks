-- name: get-bookmarks
-- get all book marks.
SELECT * from bookmarks

-- name: get-bookmarks-by-title
-- search by title
SELECT * from bookmarks
WHERE title = :title
