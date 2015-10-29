(ns bookmarks.core
  (:require
   [compojure.core :refer :all]
   [bookmarks.db.core :as db]
   [compojure.route :as route]
   [ring.adapter.jetty :as jetty]
   [ring.middleware.json :as ring-json]
   [ring.util.response	:as rr]
   [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
   [compojure.response :refer [render]]
   [clojure.java.io :as io])
  (:gen-class))


(defn home
  ""
  [req]
  (render (io/resource "index.html") req))


(defn get-tag [tagname]
  (db/get-tag-by-name {:tagname tagname}))


(defn insert-tag [tag]
  (db/create-tag tag))

(defn insert-bookmark [bookmark tagname]
  (let [tag (first (get-tag tagname))
        tagid (cond (nil? tag) (:id (first (insert-tag {:tagname tagname})))
                    :else (:id tag))
        dbbm (first (db/create-bookmark bookmark))]
    (db/create-bookmark-tag {:bookmarkid (:id dbbm)
                             :tagid tagid})
    dbbm))


(defroutes app-routes
  (GET "/" [] home)
  (GET "/bookmarks" [] (rr/content-type
                        (rr/response  (db/get-bookmarks))
                        "application/json; charset=utf-8"))
  (GET "/bookmarks/:title" [title]
       (rr/content-type
        (rr/response  (db/get-bookmarks-by-title {:title title}))
        "application/json; charset=utf-8"))

  (POST "/bookmark" {body :body}
        (let [{t "title" u "url"
               d "description"
               ta "tag"} body]
          (rr/content-type
           (rr/response (assoc (insert-bookmark
                                {:title t :url u
                                 :description d} ta)
                               :tag ta))
           "application/json; charset=utf-8")))
  (route/resources "/static")
  (route/not-found "<h1>Page not found</h1>"))

(def app
 (-> app-routes
      (wrap-defaults (assoc-in site-defaults [:security :anti-forgery] false))
      (ring-json/wrap-json-body)
      (ring-json/wrap-json-response)))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (jetty/run-jetty app {:port 8090
                        :join? false}))
