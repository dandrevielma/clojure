(ns dataico.threadLastOperator)

(defn valid-tax [item tax-rate]
  (some (fn [tax] (= tax-rate (:tax/rate tax))) (:taxable/taxes item)))

(defn valid-retention [item retention-rate]
  (some (fn [ret] (= retention-rate (:retention/rate ret))) (:retentionable/retentions item)))

(defn valid-item [item]
  (or (and (valid-tax item 19) (not (valid-retention item 1)))
      (and (valid-retention item 1) (not (valid-tax item 19)))))

(defn get-valid-items [items]
  (->> items
       (filter valid-item)
       (vec)))