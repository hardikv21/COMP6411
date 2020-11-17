(ns sale
  (:require [clojure.set :as set])
  (:gen-class))

(def cust (clojure.string/split-lines (slurp "cust.txt")))

(def custID {})
(def custIDInfo {})

(loop [x 0]
  (when (not-empty (get cust x))
    (def y (clojure.string/split (get cust x) #"\|"))
    (def custID (assoc custID (get y 0) (get y 1)))
    (def custInfo (str ":[\" "(get y 1)" \", \" "(get y 2)" \", \" "(get y 3)" \"]"))
    (def custIDInfo (assoc custIDInfo (get y 0) custInfo))
    (def sortCustIDInfo (into (sorted-map) custIDInfo))
    (def sortCustID (into (sorted-map) custID))
    (recur (+ x 1))))

(def prod (clojure.string/split-lines (slurp "prod.txt")))

(def prodItemID {})
(def prodCostID {})

(loop [x 0]
  (when (not-empty (get prod x))
    (def y (clojure.string/split (get prod x) #"\|"))
    (def prodItemID (assoc prodItemID (get y 0) (get y 1)))
    (def prodCostID (assoc prodCostID (get y 0) (read-string (get y 2))))
    (def sortProdItemID (into (sorted-map) prodItemID))
    (def sortProdCostID (into (sorted-map) prodCostID))
    (recur (+ x 1))))

(def sale (clojure.string/split-lines (slurp "sales.txt")))

(def salesCustID {})
(def salesProdID {})
(def salesItemCount {})

(loop [x 0]
  (when (not-empty (get sale x))
    (def y (clojure.string/split (get sale x) #"\|"))
    (def salesCustID (assoc salesCustID (get y 0) (get custID (get y 1))))
    (def salesProdID (assoc salesProdID (get y 0) (get prodItemID (get y 2))))
    (def salesItemCount (assoc salesItemCount (get y 0) (read-string (get y 3))))
    (def sortSalesCustID (into (sorted-map) salesCustID))
    (def sortSalesProdID (into (sorted-map) salesProdID))
    (def sortSalesItemCount (into (sorted-map) salesItemCount))
    (recur (+ x 1))))

(defn MenuDisplay []
  (println)
  (println "*** Sales Menu ***")
  (println "_ _ _ _ _ _ _ _ _ _")
  (println)
  (println "1. Display Customer Table")
  (println "2. Display Product Table")
  (println "3. Display Sales Table")
  (println "4. Total Sales for Customer")
  (println "5. Total Count for Product")
  (println "6. Exit")
  (println)
  (println "Enter an option?"))

(defn CustDisplay []
  (def keysMap (keys sortCustIDInfo))
  (def valMap (vals sortCustIDInfo))
  (def ncount (count keysMap))
  (loop [x 0]
    (when (< x ncount)
      (println (nth keysMap x)(nth valMap x))
      (recur (+ x 1)))))

(defn ProdDisplay []
  (def keysMap (keys sortProdItemID))
  (def valMap (vals sortProdItemID))
  (def val2Map (vals sortProdCostID))
  (def ncount (count keysMap))
  (loop [x 0]
    (when (< x ncount)
      (println (nth keysMap x)":[\""(nth valMap x)"\", \""(nth val2Map x)"\"]")
      (recur (+ x 1)))))

(defn SaleDisplay []
  (def keysMap (keys sortSalesCustID))
  (def valMap (vals sortSalesCustID))
  (def val2Map (vals sortSalesProdID))
  (def val3Map (vals sortSalesItemCount))
  (def ncount (count keysMap))
  (loop [x 0]
    (when (< x ncount)
      (println (nth keysMap x)" :[\" "(nth valMap x)" \", \" "(nth val2Map x)" \", \" "(nth val3Map x)" \"]")
      (recur (+ x 1)))))

(defn CustTotalDisplay [custName]
  (def totalBill 0)
  (def valMap (vals sortSalesCustID))
  (def val2Map (vals sortSalesProdID))
  (def val3Map (vals sortSalesItemCount))
  (def revProd (set/map-invert sortProdItemID))
  (def ncount (count valMap))
  (loop [x 0]
    (when (< x ncount)
      (if (== (compare (nth valMap x) custName) 0)
        (def totalBill (+ totalBill (* (get sortProdCostID (get revProd (nth val2Map x))) (nth val3Map x)))))
      (recur (+ x 1))))
  (println custName":"totalBill))

(defn CustTotal []
  (def custName (read-line))
  (def revCust (set/map-invert custID))
  (if (nil? (find revCust custName))
    (println "Customer not found")
    (CustTotalDisplay custName)))

(defn ProdTotalDisplay [prodName]
  (def totalBill 0)
  (def val2Map (vals sortSalesProdID))
  (def val3Map (vals sortSalesItemCount))
  (def ncount (count val2Map))
  (loop [x 0]
    (when (< x ncount)
      (if (== (compare (nth val2Map x) prodName) 0)
        (def totalBill (+ totalBill (* (get sortProdCostID (get revProd (nth val2Map x))) (nth val3Map x)))))
      (recur (+ x 1))))
  (println prodName":"totalBill))

(defn ProdTotal []
  (def prodName (read-line))
  (def revProd (set/map-invert sortProdItemID))
  (if (nil? (find revProd prodName))
    (println "Product not found")
    (ProdTotalDisplay prodName)))

(loop [x 0]
  (MenuDisplay)
  (def y (read-line))
  (when (not= y "6")
    (case y "1" (CustDisplay)
            "2" (ProdDisplay)
            "3" (SaleDisplay)
            "4" (CustTotal)
            "5" (ProdTotal)
            (println "Wrong Choice"))
    (recur (+ x 0))))

(println "Good Bye")