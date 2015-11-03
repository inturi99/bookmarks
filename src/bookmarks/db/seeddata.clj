(ns bookmarks.db.seeddata
  (:require [bookmarks.files :refer [get-words]]
            [bookmarks.db.core :as db]))

(defn gen-tags [file-path]
  (distinct (filter (fn [w] (>= (count w) 3))
                    (get-words file-path))))
(defn gen-urls [file-path]
  (map (fn [w] (str "http://www."w ".com"))
       (gen-tags file-path)))

(defn seed-data [file-path]
  (for [r (gen-tags file-path)
        :let [t (first (db/create-tag {:tagname r}))
              bm (first (db/create-bookmark 
                  {:title r :url (str "www."r".com")
                  :description r}))]]
    (db/create-bookmark-tag {:bookmarkid (:id bm) :tagid (:id t)})))

