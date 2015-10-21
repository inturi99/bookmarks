(defproject bookmarks "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [compojure "1.3.4"]
                 [migratus "0.8.6"]
                 [org.postgresql/postgresql "9.4-1203-jdbc42"]
                 [org.clojure/java.jdbc "0.4.2"]
                 [ring/ring-defaults "0.1.2"]
                 [ring/ring-jetty-adapter "1.4.0"]
                 [korma "0.4.0"]
                 [ring/ring-json "0.4.0"]
                 [org.clojure/clojurescript "0.0-2843"]
                 [secretary "1.2.3"]
                 [reagent "0.5.1"]
                 [yesql "0.5.1"]]

  :plugins [[lein-ring "0.8.13"]
            [migratus-lein "0.1.7"]
            [lein-cljsbuild "1.0.4"]]
  :main bookmarks.core
  :ring {:handler bookmarks.handler/app}
  :migratus {:store :database
             :migration-dir "migrations/"
             :db {:classname "org.postgresql.Driver"
                  :subprotocol "postgresql"
                  :subname "//localhost:5432/bookmarks"
                  :user "postgres"
                  :password "Design_20"}}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]]}}
  :cljsbuild {:builds
              [{:id "app"
                :source-paths ["src"]
                :compiler {:output-to "resources/public/js/app.js"
                           :output-dir "resources/public/js/out"
                           :source-map true
                           :optimizations :none
                           :asset-path "/static/js/out"
                           :main "bookmarks.scljs.core"
                           :pretty-print true}}]})
