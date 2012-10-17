(ns mas.system-test
  (:use [midje.sweet]
        [mas.system])
  (:require [mas.object :as obj]
            [mas.agent  :as ag]))

(def empty-system {:objects   {}
                   :relations []})

(def agent-a (ag/build-agent "doi:10.1038/27136"   :article {}))
(def agent-b (ag/build-agent "doi:10.1038/490161a" :article {}))
(def agent-c (ag/build-agent "doi:10.1038/654987"  :article {}))

(def system-a
  (assoc empty-system
    :objects   {"doi:10.1038/27136"   agent-a
                "doi:10.1038/490161a" agent-b
                "doi:10.1038/654987"  agent-c }
    :relations #{["doi:10.1038/27136" "doi:10.1038/490161a" 10]
                 ["doi:10.1038/27136" "doi:10.1038/654987"  5]}))

(def system-atom (atom system-a))

(defmethod ag/receive [:article :system-test] [& args] :executed)

(fact "add-agent adds a new agent to the system"
      (:objects (add-agent empty-system agent-a)) => {"doi:10.1038/27136" agent-a})

(fact "add-relation adds a new relation to the system"
      (:relations (add-relation empty-system "agent-a-id" "agent-b-id" 10)) => #{["agent-a-id" "agent-b-id" 10]})

(fact "add-relation changes the strength if the relation already exists"
      (:relations (add-relation system-a "doi:10.1038/27136" "doi:10.1038/490161a" 12)) 
      => #{["doi:10.1038/27136" "doi:10.1038/490161a" 12] ["doi:10.1038/27136" "doi:10.1038/654987" 5]})

(fact "find-agent finds an agent into system"
      (find-agent system-a "doi:10.1038/490161a") => agent-b)

(fact "find-following given an agent, finds everybody it follows"
      (find-following system-a "doi:10.1038/27136") => {"doi:10.1038/490161a" 10 "doi:10.1038/654987" 5})

(fact "find-followers given an agent, finds everybody who follows it"
      (find-followers system-a "doi:10.1038/490161a") => {"doi:10.1038/27136" 10})

(against-background
 [(before :facts (reset! system-atom system-a))]
 (fact "send-message makes the cloj-ajent of an agent `receive` it"
       (do
         (send-message system-atom "doi:10.1038/27136" :system-test)
         (. Thread sleep 10)
         @(ag/clj-agent agent-a)) => :executed))
