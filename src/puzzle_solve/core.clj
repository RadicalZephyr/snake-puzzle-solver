(ns puzzle-solve.core)

(def head "+")

(def puzzle [[" " " " "X" "X" "X"]
             [" " " " " " " " " "]
             [" " " " " " " " " "]
             [" " " " " " " " " "]
             ["X" "X" " " " " " "]])

(def *move-num* 1)

(defn solved? [board]
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

(defn in-bounds? [[x y]]
  (and (>= x 0)
       (< x 5)
       (>= y 0)
       (< y 5)))

(defn legal-move? [board pos]
  (and (in-bounds? pos)
       (= " " (get-pos board pos))))

(defn adj-squares [[x y]]
  (->> [[(inc x) y :right]
        [(dec x) y :left]
        [x (inc y) :down]
        [x (dec y) :up]]
       (filter in-bounds?)))

(defn get-move-fn [direction]
  (cond (= direction :right) (fn [[x y]] [(inc x) y])
        (= direction :left)  (fn [[x y]] [(dec x) y])
        (= direction :down)  (fn [[x y]] [x (inc y)])
        (= direction :up)    (fn [[x y]] [x (dec y)])))

(defn do-move [board [x y direction]]
  (let [next-move (get-move-fn direction)]
    (loop [board board
           pos [x y]
           next-pos (next-move pos)]
      (cond (and (legal-move? board next-pos)
                 (legal-move? board pos)) (recur (set-pos board pos *move-num*)
                                                 next-pos
                                                 (next-move next-pos))
            (legal-move? board pos) (set-pos board pos head)))))
