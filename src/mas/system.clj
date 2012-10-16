(ns mas.system
  (:require [mas.object :as obj]))

(def main (atom {:objects   {}
                 :relations #{}}))

(defn add-agent [system new-agent]
  (let [objs (:objects system)
        id   (obj/id new-agent)]
    (assoc system :objects (assoc objs id new-agent))))

(defn add-relation [system from to strength]
  (let [rels		(:relations system)
        {:keys [other]} (group-by #(if (and (= from (get % 0)) (= to (get % 1))) :found :other) rels)
        new-relation    [from to strength]]
    (assoc system :relations (set (conj other new-relation)))))

(defn find-agent [system id]
  (get (:objects system) id))

(defn find-following [system id]
  (let [rels       (:relations system)
        followings (filter #(= (first %) id) rels)]
    (zipmap (map second followings) (map last followings))))

(defn find-followers [system id]
  (let [rels      (:relations system)
        followers (filter #(= (second %) id) rels)]
    (zipmap (map first followers) (map last followers))))
