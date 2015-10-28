(ns bookmarks.createbookmarks)
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
  (row label [:input.form-control {:field type :id id}]))


(defn bookmark-template []
  [:div.form-group
   [:div (input "Title" :text :title)]
   [:div  (input "URL" :text :url)]
   [:div (input "Tags" :text :tags)]
   [:div  (input "Description":text :description)]
   ])

(defn page [body]
  (fn []
    [:div
     [:div.padding]
     [:div.page-header [:h1 "Book Marks"]]
     [bookmark-template]
     [:input {:type "button" :value "Save"
              :class "btn btn-primary" }]
     [:div.padding]
     [:div.page-footer [:h6 "Copyright ©2015 A TechnoIdentity Creations — All Rights Reserved."]]
     ]))
