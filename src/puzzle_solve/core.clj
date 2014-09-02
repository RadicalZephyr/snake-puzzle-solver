(ns puzzle-solve.core)

(def head "+")

(def puzzle [[" " " " "X" "X" "X"]
             [" " " " " " " " " "]
             [" " " " " " " " " "]
             [" " " " " " " " " "]
             ["X" "X" " " " " " "]])

(def ^:dynamic *move-num* 0)

(defn get-pos
  "Retrieve the value of an [x y] coordinate."
  [board [x y]]
  (get (get board y) x))

(defn set-pos
  "Set the value of an [x y] coordinate."
  [board [x y] val]
  (assoc board y (assoc (get board y) x val)))

(defn x-limit
  "Return the maximum x index of this board.

  Note: this assumes that the board is rectangular and complete."
  [board]
  (count (get board 0)))

(defn y-limit
  "Return the maximum y index of this board.

  Note: this assumes that the board is rectangular and complete."
  [board]
  (count board))

(defn get-head
  "Returns the [x y] coordinate of the head position.

  Note: this won't fail on a board with more than one head character
  in it, it will only ever return the position of the first head
  position encountered."
  [board]
  (first
   (for [x (range (x-limit board))
         y (range (y-limit board))
         :when (= head (get-pos board [x y]))]
     [x y])))

(defn get-board-size
  "Returns the size of the board as [x-len y-len]."
  [board]
  [(x-limit board) (y-limit board)])

(defn in-bounds? [board [x y]]
  (and (>= x 0)
       (< x (x-limit board))
       (>= y 0)
       (< y (y-limit board))))

(defn legal-move?
  "A move is legal if it is currently blank, and if it is within the
  bounds of the board."
  [board pos]
  (and (in-bounds? board pos)
       (= " " (get-pos board pos))))

(defn adj-squares
  "Generate a list of the adjacent squares that are also legal moves."
  [board [x y]]
  (->> [[(inc x) y :right]
        [(dec x) y :left]
        [x (inc y) :down]
        [x (dec y) :up]]
       (filter (partial legal-move? board))))

(defn solved?
  "Check if the board is solved.

  By definition, the board is solved when there are no empty spaces
  remaining."
  [board]
  (not (some #{" "}
             (map #(some #{" "} %) board))))

(defn stuck?
  "Check if the board is in a \"stuck\" position: where there are no
  legal moves, but the whole board hasn't been solved."
  [board]
  (let [head-pos (get-head board)]
    (some (complement (partial legal-move? board))
          (adj-squares board head-pos))))

(defn get-move-fn
  "Return a function that will return the appropriate next square for
  the direction given."
  [direction]
  (cond (= direction :right) (fn [[x y]] [(inc x) y])
        (= direction :left)  (fn [[x y]] [(dec x) y])
        (= direction :down)  (fn [[x y]] [x (inc y)])
        (= direction :up)    (fn [[x y]] [x (dec y)])))

(defn do-move
  "Write the current move number in all the empty spaces in the given
  direction starting from the given [x y] coordinates."
  [board [x y direction]]
  (let [next-move (get-move-fn direction)]
    (loop [board (set-pos board (get-head board) *move-num*)
           pos [x y]
           next-pos (next-move pos)]
      (cond (and (legal-move? board next-pos)
                 (legal-move? board pos)) (recur (set-pos board pos *move-num*)
                 next-pos
                 (next-move next-pos))
                 (legal-move? board pos) (set-pos board pos head)))))

(defn solve
  "Recursively try all possible legal moves from the current location
  until the board is either solved or stuck."
  [board]
  (cond (solved? board) board
        (stuck? board) nil
        :else (let [pos (get-head board)]
                (binding [*move-num* (inc *move-num*)]
                  (some solve (map (partial do-move board)
                                   (adj-squares board pos)))))))

(defn try-all
  "Run solve from every blank square on the board."
  [board]
  (let [[x-limit y-limit] (get-board-size board)]
    (for [x (range x-limit)
          y (range y-limit)
          :when (legal-move? puzzle [x y])]
      (solve (set-pos puzzle [x y] head)))))

(defn -main
  "Solve the default puzzle.  All puzzles are assumed to be square,
  and complete (i.e. no missing squares in any of the rows)."
  [& args]
  (clojure.pprint/pprint
   (some identity (try-all puzzle))))
