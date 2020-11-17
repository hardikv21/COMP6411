(ns core1.core
  (:gen-class))

(def cust (clojure.string/split-lines (slurp "cust.txt")))

(def custName '())
(def custID {})
(def custIDInfo {})


(loop [x 0]
  (when (not-empty (get cust x))
    (def y (clojure.string/split (get cust x) #"\|"))
    (def custName (conj custName (get y 1)))
    (def custID (assoc custID (get y 0) (get y 1)))
    (def custInfo (str ":[\" "(get y 1)" \", \" "(get y 2)" \", \" "(get y 3)" \"]"))
    (def custIDInfo (assoc custIDInfo (get y 0) custInfo))
    (def sortCustIDInfo (into (sorted-map) custIDInfo))
    (def sortCustID (into (sorted-map) custID))
    (recur (+ x 1))))
;(println custName)
;(println custID)
;(println custIDInfo)

(def prod (clojure.string/split-lines (slurp "prod.txt")))

(def prodItemID {})
(def prodCostID {})
(def prodName '())


(loop [x 0]
  (when (not-empty (get prod x))
    (def y (clojure.string/split (get prod x) #"\|"))
    (def prodName (conj prodName (get y 1)))
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

(println prodItemID)
(println salesCustID )
(println salesProdID )
(println salesItemCount)
(println prodName)



(def  totalCountForProduct{})

(def valMap (vals salesProdID))
(def valMapcount (vals salesItemCount))
(def ncount (count valMap))


(def totalCountForProduct (assoc totalCountForProduct  (nth valMap 0)  (Integer. (nth valMapcount 0))   ))


(loop [x 1]
  (when (< x ncount)

        (def valTemp (keys totalCountForProduct))

        (if (= (some #(= (nth valMap x) %) valTemp) true)
          (do


            (def totalCount (+ (Integer. (nth valMapcount x))  (Integer. (get totalCountForProduct (nth valMap x)))  ))

            (def totalCountForProduct (assoc totalCountForProduct   (nth valMap x)   (Integer. totalCount) ))

            true)
          (do

            (def totalCountForProduct (assoc totalCountForProduct  (nth valMap x)  (Integer. (nth valMapcount x))   ))

            )
          )

        (println totalCountForProduct)
        (recur (+ x 1))))




(def  totalSalesForCustomer{})

(def keysMap (keys salesCustID))
(def valMap (vals salesCustID))
(def ncount (count keysMap))

(def inDex  (+ 1 (.indexOf (vals sortProdItemID) (get salesProdID (str 1)))))

(def totalSalesForCustomer (assoc totalSalesForCustomer  (nth valMap 0)  (format "%.2f" (*  (float (get salesItemCount (str 1))) (float (get sortProdCostID (str inDex)))))   ))


(loop [x 2]
  (when (<= x ncount)

        (def valTemp (keys totalSalesForCustomer))

        (if (= (some #(= (nth valMap (- x 1)) %) valTemp) true)
          (do

            (def inDex  (+ 1 (.indexOf (vals sortProdItemID) (get salesProdID (str x)))))

            (def totalCostTemp   (+  (Float/parseFloat (format "%.2f" (*  (float (get salesItemCount (str x))) (float (get sortProdCostID (str inDex))))))  (Float/parseFloat (get totalSalesForCustomer (str (nth valMap (- x 1)))))  )   )
            (def totalSalesForCustomer (assoc totalSalesForCustomer   (str (nth valMap (- x 1)))  (format "%.2f" totalCostTemp) ))

            true)
          (do
            (def inDex  (+ 1 (.indexOf (vals sortProdItemID) (get salesProdID (str x)))))

            (def totalSalesForCustomer (assoc totalSalesForCustomer  (str (nth valMap (- x 1))) (format "%.2f" (*  (float (get salesItemCount (str x))) (float (get sortProdCostID (str inDex)))))   ))

            )
          )

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

(defn CustTotal []
  (println "Enter customer name.")
  (def custName111 (read-line))
  (def total 0)

  (if   (= (some #(= custName111 %) custName) true);(contains? custName custName111)
    (do

      (def keysMap (keys totalSalesForCustomer))
      (def valMap (vals totalSalesForCustomer))
      (println )

      (if   (= (some #(= custName111 %) keysMap) true)
      (println (str custName111 ": $"(get totalSalesForCustomer custName111)))
        (println (str  custName111 ": $0") )
        )

      true)
    (do (println "Customer not found."))
    )

  )






(defn ProdTotal []
  (println "Enter prduct name.")
  (def prodName111 (read-line))
  (def total 0)

  (if   (= (some #(= prodName111 %) prodName) true);(contains? custName custName111)
    (do

      (def keysMap (keys totalCountForProduct))
      (def valMap (vals totalCountForProduct))
      (println )

      (if   (= (some #(= prodName111 %) keysMap) true)
        (println (str prodName111 ": "(get totalCountForProduct prodName111)))
        (println (str  prodName111 ": 0") )
        )

      true)
    (do (println "Product not found."))
    )

  )


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