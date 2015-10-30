(ns bookmarks.db.seeddata
  (:require [bookmarks.files :refer [get-words]]))

(defn gen-tags [file-path]
  (distinct (filter (fn [w] (>= (count w) 3))
                    (get-words file-path))))
(defn gen-urls [file-path]
  (map (fn [w] (str "http://www."w ".com"))
       (gen-tags file-path)))
