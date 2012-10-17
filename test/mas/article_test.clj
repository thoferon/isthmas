(ns mas.article-test
  (:use [midje.sweet]
        [mas.article]
        [mas.agent])
  (:require [mas.system :as sys]))

(def article-a
  (build-agent "doi:blah" :article nil))
(def article-b
  (build-agent "doi:bluh" :article nil))
(def article-c
  (build-agent "doi:bleh" :article nil))

(def system {:objects {"doi:blah" article-a "doi:bluh" article-b, "doi:bleh" article-c} :relations #{}})
(def system-atom (atom system))

(against-background
 [(before :facts (reset! system-atom system))]

 (fact "receive [:article :read] create a relation between the article and previously read ones"
       (do
         (receive nil system-atom article-a :read ["doi:bluh" "doi:bleh"])
         @system-atom)
       => (assoc @system-atom :relations #{["doi:blah" "doi:bluh" 1]}))

 (fact "receive [:article :read] increase the strength of the relations between the article and previously read ones"
       (do
         (receive nil system-atom article-a :read ["doi:bluh" "doi:bleh"])
         (receive nil system-atom article-a :read ["doi:bluh" "doi:bleh"])
         @system-atom)
       => (assoc @system-atom :relations #{["doi:blah" "doi:bluh" 2]})))