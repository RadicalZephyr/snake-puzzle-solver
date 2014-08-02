(ns puzzle-solve.core-test
  (:require [clojure.test :refer :all]
            [puzzle-solve.core :refer :all]))

(def solved-puzzle [["x" "x" "X" "X" "X"]
             ["x" "x" "x" "x" "x"]
             ["x" "x" "x" "x" "x"]
             ["x" "x" "x" "x" "x"]
             ["X" "X" "x" "x" "x"]])

(deftest won-test
  (testing "won? function"
    (is (= false (won? puzzle))
        (= true (won? solved-puzzle)))))
