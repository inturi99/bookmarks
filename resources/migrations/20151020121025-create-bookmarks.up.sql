CREATE TABLE IF NOT EXISTS  bookmarks
(
id serial NOT NULL  PRIMARY KEY,
title character(100) NOT NULL,
url character(100) NOT NULL,
description text NOT NULL
);
