(ns mas.agent
	(:use [mas.object])
	(:require [mas.system :as sys]))

(deftype Agent [agent-id agent-type]
	Obj
	(id   [this] agent-id)
	(kind [this] agent-type))

(defmulti receive
	(fn [system-atom agent message-type & args]
		[(kind agent) message-type]))

(defmethod receive [:article :read]
	[system-atom article _ [prev & rest]]
	(swap! system-atom (fn [system]
		(let [following      (sys/find-following system (id article))
		      [_ _ strength] (-> (filter #(= (second %) prev) following) first)
				  new-strength   (if strength (inc strength) 1)]
		  (sys/add-relation system (id article) prev new-strength))
		)))