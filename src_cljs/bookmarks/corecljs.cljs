(ns bookmarks.corecljs
  (:require-macros [secretary.core :refer [defroute]])
  (:require [goog.events :as events]
            [secretary.core :as secretary]
            [goog.net.XhrIo :as xhr]
            [reagent.core :as r])
  (:import goog.History))


(defn url-format [url title]
  [:a {:href url :class "btn btn-primary"} title])


(defn getdata [res]
  (.getResponseJson (.-target res)))

(defn http-get [url callback]
  (xhr/send url callback))


(defn search [event]
  (let [stext (.-value (.getElementById js/document "sText"))
        onres (fn [json]
                (r/render [render-bookmarks (getdata json)]
                          (.-body js/document)))]
    (http-get (str "http://localhost:8090/bookmarks/" stext) onres)))


(defn render-bookmarks [bookmarks]
  [:br [:br]
   [:h1.text-center "List of Bookmarks"]
   [:br]
   [:div
    [:div.form-group
     [:div.col-sm-2 [:input.form-control {:id "sText" :type "text"
                                          :placeholder "search by title"}]]
     [:input {:type "button" :value "Search"
              :class "btn btn-primary" :on-click search}]]]
   [:table {:class "table table-striped table-bordered"}
    [:thead
     [:tr
      [:th "title"]
      [:th "url"]
      [:th "description"]]]
    [:tbody
     (for [bm bookmarks]
       [:tr
        [:td (.-title bm)]
        [:td (url-format (.-url bm) (.-title bm))]
        [:td (.-description bm)]])]]])


(defroute home-path "/" []
  (let [onres (fn [json]
                (r/render [render-bookmarks (getdata json)]
                          (.-body js/document)))]
    (http-get "http://localhost:8090/bookmarks" onres)))

(defroute "*" []
  (js/alert "<h1>Not Found</h1>"))


(defn main
  []
  (secretary/set-config! :prefix "#")
  (let [history (History.)]
    (events/listen history "navigate"
                   (fn [event]
                     (secretary/dispatch! (.-token event))))
    (.setEnabled history true)))


(main)
