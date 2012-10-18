(ns mas.core
  (:use [ring.adapter.jetty]
        [mas.web.core]))

(defn -main [& args]
  (let [system-atom (atom {:objects {} :relations #{}})]
    (run-jetty (build-handler system-atom) {:port 8888})))
