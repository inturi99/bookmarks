(ns bookmarks.core
  (:require
   [compojure.core :refer :all]
   [compojure.route :as route]
   [ring.adapter.jetty :as jetty]
   [ring.middleware.json :as ring-json]
   [ring.util.response	:as rr]
   [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
   [bookmarks.db.core :as db])
  (:gen-class))


(defroutes app-routes
  (GET "/" [] "welcome to home page")
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
