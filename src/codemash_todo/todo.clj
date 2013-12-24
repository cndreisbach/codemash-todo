(ns codemash-todo.todo)

(defn new-list
  []
  (vector))

(defn new-task
  [task]
  (let [task (if (map? task)
               task
               {:desc task})]
    (merge {:done false} task)))

(defn add-task
  [list desc]
  (conj list (new-task desc)))

(defn todo-tasks
  [list]
  (filter (comp not :done) list))

(defn done-tasks
  [list]
  (filter :done list))
