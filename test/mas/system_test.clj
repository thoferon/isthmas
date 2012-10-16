(ns mas.system-test
  (:use [midje.sweet]
        [mas.system])
  (:require [mas.object :as obj]))

(def empty-system {:objects   {}
                   :relations []})

(def agent-a (agent {}))
(def agent-b (agent {}))
(def agent-c (agent {}))

(def system-a
  (assoc empty-system
    :objects   {"doi:10.1038/27136"   agent-a
                "doi:10.1038/490161a" agent-b
                "doi:10.1038/654987"  agent-c }
    :relations #{["doi:10.1038/27136" "doi:10.1038/490161a" 10]
                 ["doi:10.1038/27136" "doi:10.1038/654987"  5]}))

(fact "add-agent adds a new agent to the system"
      (:objects (add-agent empty-system agent-a)) => {"agent-a-id" agent-a}
      (provided
       (obj/id agent-a) => "agent-a-id"))

(fact "add-relation adds a new relation to the system"
      (:relations (add-relation empty-system "agent-a-id" "agent-b-id" 10)) => #{["agent-a-id" "agent-b-id" 10]})

(fact "add-relation changes the strength if the relation already exists"
      (:relations (add-relation system-a "doi:10.1038/27136" "doi:10.1038/490161a" 12)) 
      => #{["doi:10.1038/27136" "doi:10.1038/490161a" 12] ["doi:10.1038/27136" "doi:10.1038/654987" 5]})

(fact "find-agent find an agent into system"
      (find-agent system-a "doi:10.1038/490161a") => agent-b)

(fact "find-following given an agent, find everybody it follows"
      (find-following system-a "doi:10.1038/27136") => {"doi:10.1038/490161a" 10 "doi:10.1038/654987" 5})

(fact "find-followers given an agent, find everybody who follows it"
      (find-followers system-a "doi:10.1038/490161a") => {"doi:10.1038/27136" 10})
