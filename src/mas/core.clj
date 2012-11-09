(ns mas.core
  (:use [ring.adapter.jetty]
        [mas.web.core]))

(defn -main [& args]
  (run-jetty ring-handler {:port 8888}))
