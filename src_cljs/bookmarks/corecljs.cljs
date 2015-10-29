(ns bookmarks.corecljs
  (:require-macros [secretary.core :refer [defroute]])
  (:require [goog.events :as events]
            [secretary.core :as secretary]
            [goog.net.XhrIo :as xhr]
            [reagent.core :as r]
            [bookmarks.helpers :as he]
            [bookmarks.createbookmarks :as cb]
            [cognitect.transit :as t]
            [goog.structs :as structs])
  (:import goog.History)
  )


(defn url-format [url title]
  [:a {:href url :class "btn btn-primary"} title])

(def w (t/writer :json-verbose))

(defn getdata [res]
  (.getResponseJson (.-target res)))

(defn http-get [url callback]
  (xhr/send url callback))

(defn http-post [url callback data]
  (xhr/send url callback "POST" data  (structs/Map. (clj->js {:Content-Type "application/json"}))))

(defn auto-complete
  [name  data]
  (when-not (empty? name)
    (let [pattern (js/RegExp. (str ".* " name ".*|" name ".*") "i")]
      (not-empty (set (keep #(re-matches pattern %) data))))))

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
              :class "btn btn-primary" :on-click search}] 
     (url-format "http://localhost:8090/#/bookmark" "Add")]]
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
        [:td (he/url-format (.-url bm) (.-title bm))]
        [:td (.-description bm)]])]]])

(defn row [label input]
  [:div.row
   [:div.col-md-2 [:label label]]
   [:div.col-md-5 input]])
(defn radio [label name value]
  [:div.radio
   [:label
    [:input {:field :radio :name name :value value}]
    label]])

(defn input [label type id]
  (row label [:input.form-control {:type type :id id}]))


(defn bookmark-template []
  [:div.form-group
   [:div (input "Title" "text" "title")]
   [:div  (input "URL" "text" "url")]
   [:div (input "Tags" "text" "tags")]
   [:div  (input "Description" "textarea" "desc")]])

(defn getinputvalue[id]
(.-value (.getElementById js/document id)))

(defn get-bookmark-formdata []
  { :title (getinputvalue "title")
   :url (getinputvalue "url")
   :tag (getinputvalue "tags")
   :description (getinputvalue "desc")
   } )



(defn save [event]
  (let [onres (fn[data] (set! (.-location js/window) "http://localhost:8090"))]
    (http-post "http://localhost:8090/bookmark" 
               onres  (JSON/stringify (clj->js (get-bookmark-formdata))))))

(defn page [body]
  (fn []
    [:div.container
     [:div.padding]
     [:div.page-header [:h1 "Book Marks"]]
     [bookmark-template]
     [:input {:type "button" :value "Save"
              :class "btn btn-primary" :on-click save}]
     [:div.padding]
     [:div.page-footer [:h6 "Copyright ©2015 A TechnoIdentity Creations — All Rights Reserved."]]
     ]))

(defroute home-path "/" []
  (let [onres (fn [json]
                (r/render [render-bookmarks (getdata json)]
                          (.-body js/document)))]
    (http-get "http://localhost:8090/bookmarks" onres)))

(defroute bookmark-path "/bookmark" []
  (r/render [page](.-body js/document)))

(defroute "*" []
  (js/alert "<h1>Not Found Page</h1>"))


(defn main
  []
  (secretary/set-config! :prefix "#")
  (let [history (History.)]
    (events/listen history "navigate"
                   (fn [event]
                     (secretary/dispatch! (.-token event))))
    (.setEnabled history true)))


(main)
