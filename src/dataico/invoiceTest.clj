(ns dataico.invoiceTest
  (:require [clojure.test :refer :all]
            [dataico.invoice-item :refer :all]))

(deftest test-subtotal-returns-number
  (testing "Subtotal should return a number"
    (is (number? (subtotal {:invoice-item/precise-quantity 1
                            :invoice-item/precise-price 100
                            :invoice-item/discount-rate 10})))))

(deftest test-subtotal-with-discount
  (testing "Subtotal should correctly apply discount"
    (let [price 100
          quantity 2
          discount 10
          expected (* price quantity 0.9)]
      (is (= expected (subtotal {:invoice-item/precise-quantity quantity
                                 :invoice-item/precise-price price
                                 :invoice-item/discount-rate discount}))))))


(deftest test-subtotal-non-negative
  (testing "Subtotal should not be negative"
    (is (>= (subtotal {:invoice-item/precise-quantity 1
                       :invoice-item/precise-price 100
                       :invoice-item/discount-rate 10}) 0))
    (is (>= (subtotal {:invoice-item/precise-quantity -1
                       :invoice-item/precise-price -100
                       :invoice-item/discount-rate 10}) 0))))