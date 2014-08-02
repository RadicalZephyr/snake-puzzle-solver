(ns puzzle-solve.core)

(def head "+")

(def puzzle [[" " " " "X" "X" "X"]
             [" " " " " " " " " "]
             [" " " " " " " " " "]
             [" " " " " " " " " "]
             ["X" "X" " " " " " "]])

(defn won? [puzzle]
  (not (some #{" "}
             (map #(some #{" "} %) puzzle))))

(defn get-head [puzzle]
  (get (for [x (range 5)
             y (range 5)
             :when (= head (get (get puzzle y) x))]
         [x y])
       0))

(defn set [puzzle x y val]
  (assoc puzzle y (assoc (get puzzle y) x val)))
