(ns mas.web.core
  (:require [ring.util.response :as ring-resp]
            [mas.web.messaging  :as msg]))

(defn build-handler [system-atom]
  (fn [request]
    (case (:uri request)
      "/" {:status  200
           :headers {"Content-Type" "text/html"}
           :body    "<html><head></head><body><h1>Hooray, I'm alive !</h1><p>Have a look at my (still missing) documentation to learn about the API you can use to talk to me. You can't really do anything here, sorry.</p></body></html>"}

      "/message" (msg/handler system-atom request)
      "/recommendation" {:status 501 :body "501 Not Implemented Yet"}

      (ring-resp/not-found "404 Not Found"))))