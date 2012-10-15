(ns mas.object)

(defprotocol Obj
	(id   [this] "returns the identifier of the object")
	(kind [this] "returns the type of the object"))

