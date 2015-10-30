(ns bookmarks.files
  (:require [clojure.string :refer [lower-case split
                                    blank? split-lines]]
            [clojure.java.io :refer [file]]))


(defn split-line [line]
  (vec (split (clojure.string/replace
               line #"[.,()]" "") #"\s+")))

(defn get-lines [file-path]
  (remove blank? (split-lines (slurp
                               (file file-path)))))
(defn get-words [file-path]
  (mapcat split-line (get-lines file-path)))

(defn word-count [filename]
  (count (get-words filename)))

(defn word-frequency [word filename]
  (let [w (lower-case word)]
    (count (filter (fn [w1] (= w (lower-case w1)))
                   (get-words filename)))))

(defn isword-contains? [word line]
  (let [w (lower-case word) coll (split-line line) ]
    (loop [c coll]
      (cond (empty? c) false
            (= w (first c)) true
            :else (recur (rest c))))))


(defn grep1 [word filename]
  (let [w (lower-case word)]
    (into {} (filter (fn [[k v]] (isword-contains? w (lower-case v)))
                     (zipmap (range) (get-lines filename))))))

(defn index [filename]
  (reduce (fn [x w] ( conj x [w (keys (grep1 w filename))]))
          {} (get-words filename)))
