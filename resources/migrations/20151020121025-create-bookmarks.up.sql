CREATE TABLE IF NOT EXISTS  bookmarks
(
id serial NOT NULL  PRIMARY KEY,
title text  NOT NULL,
url text  NOT NULL,
description text NOT NULL
);
