(ns mas.web.messaging
  (:use [mas.system :as sys]))

(defn handler [system-atom request]
  (let [qstr      (:query-string request)
        params    (apply hash-map (flatten (map #(vector (keyword (% 1)) (% 2)) (re-seq #"([^=|^&]+)=([^&]*)" qstr))))
        recipient (:recipient params)
        type      (:type params)]
    (sys/send-message system-atom recipient type ["doi:blah"])
    200))
