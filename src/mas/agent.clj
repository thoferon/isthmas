(ns mas.agent
  (:use mas.object))

(defprotocol AgentProtocol
  (clj-agent [this] "returns the clojure agent of this agent"))

(deftype Agent [agent-id agent-kind agent-clj-agent]
  ObjectProtocol
  (id   [this] agent-id)
  (kind [this] agent-kind)
  AgentProtocol
  (clj-agent [this] agent-clj-agent))

(defn build-agent [id kind value]
  (->Agent id kind (agent value)))

(defmulti receive
  (fn [old-value system-atom agent message-type & args]
    [(kind agent) message-type]))

(defmethod receive [:article :debug]
  [& args]
  2)
