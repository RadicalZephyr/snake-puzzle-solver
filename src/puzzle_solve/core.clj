(ns puzzle-solve.core)

(def puzzle [[" " " " "X" "X" "X"]
             [" " " " " " " " " "]
             [" " " " " " " " " "]
             [" " " " " " " " " "]
             ["X" "X" " " " " " "]])

(defn won? [puzzle]
  (not (some #{" "}
             (map #(some #{" "} %) puzzle))))
