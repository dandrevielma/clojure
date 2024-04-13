(ns dataico.core)

(require '[clojure.edn :as edn]
         '[dataico.threadLastOperator :refer [get-valid-items]]
         '[dataico.coreGenerating :refer [generate-invoice]]
         )

(def invoice (edn/read-string (slurp "invoice.edn")))

(def items (invoice :invoice/items))

(def problem1 (get-valid-items items))
(def problem2 (generate-invoice "invoice.json"))

(println "Problem 1: ")
(println problem1)

(println "Problem 2: ")
(println problem2)

(println "Problem 3: Run invoiceTest.clj")