(ns mas.web.messaging
  (:use [mas.article])
  (:require [mas.system :as sys]
            [ring.util.codec :as codec]))

(defn handler [system-atom request]
  (try
    (let [qstr      (:query-string request)
          params    (apply hash-map (flatten (map #(vector (keyword (% 1)) (% 2)) (re-seq #"([^=|^&]+)=([^&]*)" qstr))))
          recipient (-> params :recipient codec/url-decode)
          type      (-> params :type codec/url-decode keyword)
          args      (-> params :args codec/url-decode read-string)]
      (if (sys/send-message system-atom recipient type args)
        200
        404))
    (catch Exception e {:status 500 :body (pr-str e)})))
