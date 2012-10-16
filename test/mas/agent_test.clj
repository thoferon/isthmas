(ns mas.agent-test
  (:use [midje.sweet]
        [mas.agent])
  (:require [mas.system :as sys]))

(def article-a
  (->Agent "doi:blah" :article))
(def article-b
  (->Agent "doi:bluh" :article))
(def article-c
  (->Agent "doi:bleh" :article))	

(def system {:objects {"doi:blah" article-a "doi:bluh" article-b, "doi:bleh" article-c} :relations #{}})

(def system-atom (atom system))

(against-background
 [(before :facts (reset! system-atom system))]

 (fact "receive [:article :read] create a relation between the article and previously read ones"
       (do
         (receive system-atom article-a :read ["doi:bluh" "doi:bleh"])
         @system-atom)
       => (assoc @system-atom :relations #{["doi:blah" "doi:bluh" 1]}))

 (fact "receive [:article :read] increase the strength of the relations between the article and previously read ones"
       (do
         (receive system-atom article-a :read ["doi:bluh" "doi:bleh"])
         (receive system-atom article-a :read ["doi:bluh" "doi:bleh"])
         @system-atom)
       => (assoc @system-atom :relations #{["doi:blah" "doi:bluh" 2]})))