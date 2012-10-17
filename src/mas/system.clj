(ns mas.system
  (:require [mas.object :as obj]
            [mas.agent  :as ag]))

(defn add-object
  "returns a new system with the object added or replaced if there already was an agent with this id"
  [system new-object]
  (let [objs (:objects system)
        id   (obj/id new-object)]
    (assoc system :objects (assoc objs id new-object))))

(defn add-relation
  "returns a new system with a new relation or replaces the old one if it was already there"
  [system from to strength]
  (let [rels		(:relations system)
        {:keys [other]} (group-by #(if (and (= from (get % 0)) (= to (get % 1))) :found :other) rels)
        new-relation    [from to strength]]
    (assoc system :relations (set (conj other new-relation)))))

(defn find-object
  "returns the object or nil"
  [system id]
  (get (:objects system) id))

(defn find-following
  "finds the following of an object and returns a map with ids as keys and strength as values"
  [system id]
  (let [rels       (:relations system)
        followings (filter #(= (first %) id) rels)]
    (zipmap (map second followings) (map last followings))))

(defn find-followers [system id]
  "finds the followers of an object and returns a map with ids as keys and strength as values"
  (let [rels      (:relations system)
        followers (filter #(= (second %) id) rels)]
    (zipmap (map first followers) (map last followers))))

(defn send-message
  "finds an object and make its clojure agent `receive` the message"
  [system-atom recipient-id message-type & args]
  (let [agent (find-object @system-atom recipient-id)]
    (apply (partial send (ag/clj-agent agent) ag/receive system-atom agent message-type) args)))