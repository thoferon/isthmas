(defproject mas "0.1.0-SNAPSHOT"
  :description "TODO"
  :url "http://todo.com"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [midje "1.4.0"]
                 [ring "1.1.6"]]
  :profiles {:dev {:plugins [[lein-midje   "2.0.0-SNAPSHOT"]
                             [jonase/kibit "0.0.4"]
                             [lein-ring    "0.7.5"]]}}
  :main mas.core
  :ring {:handler mas.web.core/ring-handler}
  :war-resources-path "resources")
