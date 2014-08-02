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

(defn get-pos [board [x y]]
  (get (get board y) x))

(defn set-pos [board [x y] val]
  (assoc board y (assoc (get board y) x val)))

(defn adj-squares [[x y]]
  (->> [[(inc x) y :right]
        [(dec x) y :left]
        [x (inc y) :down]
        [x (dec y) :up]]
       (filter (fn [el] (and (not (some #(and (integer? %)
                                              (> % 4)) el))
                            (not (some #(and (integer? %)
                                             (neg? %)) el)))))))

(defn get-move-fn [direction]
  (cond (= direction :right) (fn [[x y]] [(inc x) y])
        (= direction :left)  (fn [[x y]] [(dec x) y])
        (= direction :down)  (fn [[x y]] [x (inc y)])
        (= direction :up)    (fn [[x y]] [x (dec y)])))
