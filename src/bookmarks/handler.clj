(ns bookmarks.handler
  (:require
            [compojure.core :as cc]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.json :as ring-json]
            [ring.util.response	:as rr]))

(cc/defroutes app-routes
  (route/resources "/")
  (route/not-found "Not Found"))
(def app
  (handler/site app-routes))
