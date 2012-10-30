(ns mas.agents.user
  (:use [mas.agent])
  (:require [mas.system :as sys]
            [mas.object :as obj]))

(defmethod receive [:user :visit]
  [{:keys [history] :as state} system-atom article _ id type]
  (let [visited (or
                 (sys/find-object @system-atom id)
                 (let [new-agent (build-agent id type {})]
                   (swap! system-atom #(sys/add-object % new-agent))
                   new-agent))]
    (sys/send-message system-atom id :read [history])
    (assoc state :history (conj history id))))
