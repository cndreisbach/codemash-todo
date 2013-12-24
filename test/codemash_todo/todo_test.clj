(ns codemash-todo.todo-test
  (:require [clojure.test :refer :all]
            [codemash-todo.todo :refer :all]))

(def task-list
  (-> (new-list)
      (add-task "Clean the tub")
      (add-task "Eat cake")
      (add-task "Wash the dog")
      (assoc-in [0 :done] true)))

(deftest test-new-list
  (is (= (new-list) [])))

(deftest test-new-task
  (testing "it turns the task into a map"
    (is (= (new-task "Clean the tub")
           {:desc "Clean the tub"
            :done false})))
  (testing "it preserves the task if it is a map"
    (is (= (new-task {:desc "Clean the tub" :id "AAAAA"})
           {:desc "Clean the tub"
            :id "AAAAA"
            :done false})))
  (testing "it preserves the :done key if it exists"
    (is (= (new-task {:desc "Clean the tub" :id "AAAAA" :done true})
           {:desc "Clean the tub"
            :id "AAAAA"
            :done true}))))

(deftest test-add-task
  (is (= (add-task (new-list) "Clean the tub")
         (vector {:desc "Clean the tub"
                  :done false}))))

(deftest test-todo-tasks
  (is (= (map :desc (todo-tasks task-list))
         ["Eat cake" "Wash the dog"])))

(deftest test-done-tasks
  (is (= (map :desc (done-tasks task-list))
         ["Clean the tub"])))

;; (run-tests)
