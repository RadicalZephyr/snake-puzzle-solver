(ns puzzle-solve.core)

(def head "+")

(def puzzle [[" " " " "X" "X" "X"]
             [" " " " " " " " " "]
             [" " " " " " " " " "]
             [" " " " " " " " " "]
             ["X" "X" " " " " " "]])

(defn won? [board]
  (not (some #{" "}
             (map #(some #{" "} %) board))))

(defn get-head [board]
  (get (for [x (range 5)
             y (range 5)
             :when (= head (get (get board y) x))]
         [x y])
       0))

(defn set [board x y val]
  (assoc board y (assoc (get board y) x val)))
