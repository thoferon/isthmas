(ns mas.agents.article
  (:use [mas.agent])
  (:require [mas.system :as sys]
            [mas.object :as obj]))

(defmethod receive [:article :read]
  [old-value system-atom article _ [prev & rest]]
  (do
    (swap! system-atom (fn [system]
                         (let [following    (sys/find-following system (obj/id article))
                               [_ strength] (first (filter #(= (first %) prev) following))
                               new-strength (if strength (inc strength) 1)]
                           (sys/add-relation system (obj/id article) prev new-strength))))
    old-value))
