(ns codemash-todo.resources
  (:require [codemash-todo.todo :as todo]
            [liberator.core :refer [defresource]]
            [clojure.string :as str]))

(defonce todo-list (atom (todo/new-list)))
(defonce used-ids (atom #{}))

(defn new-id
  []
  (let [id (str/upper-case (subs (str (java.util.UUID/randomUUID)) 0 5))]
    (dosync
     (if (contains? @used-ids id)
       (recur)
       (do
         (swap! used-ids conj id)
         id)))))

(defn add-todo
  [desc]
  (swap! todo-list todo/add-task {:id (new-id) :desc desc}))

(defresource list
  :allowed-methods [:get :post]
  :available-media-types ["application/edn"]
  :handle-ok (fn [ctx] @todo-list)
  :post! (fn [ctx]
           (let [body (slurp (get-in ctx [:request :body]))
                 body (str/trim body)]
             (add-todo body))))

(defresource todo [id]
  :allowed-methods [:get :put :delete]
  :available-media-types ["application/json"]
  :exists? (fn [ctx]
             (if-let [todo (first (filter #(= (:id %) id) @todo-list))]
               {:todo todo}))
  :handle-ok (fn [{:keys [todo] :as ctx}]
               todo))
