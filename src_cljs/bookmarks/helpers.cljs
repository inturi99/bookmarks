(ns bookmarks.helpers)
(defn url-format [url title]
  [:a {:href url :class "btn btn-primary"} title])
