(ns puzzle-solve.core)

(def head "+")

(def puzzle [[" " " " "X" "X" "X"]
             [" " " " " " " " " "]
             [" " " " " " " " " "]
             [" " " " " " " " " "]
             ["X" "X" " " " " " "]])

(def ^:dynamic *move-num* 0)

(defn get-pos [board [x y]]
  (get (get board y) x))

(defn set-pos [board [x y] val]
  (assoc board y (assoc (get board y) x val)))

(defn x-limit [board]
  (count (get board 0)))

(defn y-limit [board]
  (count board))

(defn get-head [board]
  (first
   (for [x (range (x-limit board))
         y (range (y-limit board))
         :when (= head (get-pos board [x y]))]
     [x y])))

(defn get-board-size [board]
  [(x-limit board) (y-limit board)])

(defn in-bounds? [board [x y]]
  (and (>= x 0)
       (< x (x-limit board))
       (>= y 0)
       (< y (y-limit board))))

(defn legal-move? [board pos]
  (and (in-bounds? board pos)
       (= " " (get-pos board pos))))

(defn adj-squares [board [x y]]
  (->> [[(inc x) y :right]
        [(dec x) y :left]
        [x (inc y) :down]
        [x (dec y) :up]]
       (filter (partial legal-move? board))))

(defn solved? [board]
  (not (some #{" "}
             (map #(some #{" "} %) board))))

(defn stuck? [board]
  (let [head-pos (get-head board)]
    (some (complement (partial legal-move? board))
          (adj-squares board head-pos))))

(defn get-move-fn [direction]
  (cond (= direction :right) (fn [[x y]] [(inc x) y])
        (= direction :left)  (fn [[x y]] [(dec x) y])
        (= direction :down)  (fn [[x y]] [x (inc y)])
        (= direction :up)    (fn [[x y]] [x (dec y)])))

(defn do-move [board [x y direction]]
  (let [next-move (get-move-fn direction)]
    (loop [board (set-pos board (get-head board) *move-num*)
           pos [x y]
           next-pos (next-move pos)]
      (cond (and (legal-move? board next-pos)
                 (legal-move? board pos)) (recur (set-pos board pos *move-num*)
                                                 next-pos
                                                 (next-move next-pos))
                 (legal-move? board pos) (set-pos board pos head)))))

(defn solve [board]
  (cond (solved? board) board
        (stuck? board) nil
        :else (let [pos (get-head board)]
                (binding [*move-num* (inc *move-num*)]
                  (some solve (map (partial do-move board)
                                   (adj-squares board pos)))))))

(defn try-all [board]
  (let [[x-limit y-limit] (get-board-size board)]
    (for [x (range x-limit)
          y (range y-limit)
          :when (legal-move? puzzle [x y])]
      (solve (set-pos puzzle [x y] head)))))

(defn -main [& args]
  (clojure.pprint/pprint
   (some identity (try-all puzzle))))
