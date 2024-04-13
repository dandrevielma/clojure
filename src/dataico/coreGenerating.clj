(ns dataico.coreGenerating
  (:require [clojure.java.io :as io]
            [cheshire.core :as json]
            [clojure.spec.alpha :as s]
            [clojure.string :as str]
            [dataico.invoice-spec :as inv-spec]))

(defn parse-date [date-str]
  (if date-str
    (let [fmt (java.text.SimpleDateFormat. "dd/MM/yyyy")]
    (.parse fmt date-str))
    nil))

(defn transform-invoice [json-invoice]
  (let [transformed-data {:invoice/issue-date (parse-date (get-in json-invoice [:issue_date]))
                          :invoice/customer {:customer/name (get-in json-invoice [:customer :company_name])
                                             :customer/email (get-in json-invoice [:customer :email])}
                          :invoice/items (vec (map (fn [item]
                                                {:invoice-item/price (get-in item [:price])
                                                 :invoice-item/quantity (get-in item [:quantity])
                                                 :invoice-item/sku (get-in item [:sku])
                                                 :invoice-item/taxes (vec (map (fn [tax]
                                                                            {:tax/category (keyword (str/lower-case (get-in tax [:tax_category])))
                                                                             :tax/rate     (double (get-in tax [:tax_rate]))})
                                                                          (get-in item [:taxes])))})
                                              (get-in json-invoice [:items])))
                          }]
    transformed-data))

(defn generate-invoice [filename]
  (let [file-contents (slurp (io/file filename))
        json-data (json/parse-string file-contents true)
        invoice-map (transform-invoice (get-in json-data [:invoice]))]
    (if (s/valid? :dataico.invoice-spec/invoice invoice-map)
      invoice-map
      (let [explanation (s/explain-str :dataico.invoice-spec/invoice invoice-map)]
        (println "Generated invoice does not have the details required")
        (println explanation)
        (throw (Exception. explanation))))))
