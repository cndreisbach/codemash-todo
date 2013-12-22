(ns codemash-todo.main
  (:use [org.httpkit.server :only [run-server]])
  (:require [ring.middleware.reload :as reload]
            [tailrecursion.boot.core :refer [deftask]]))

(def in-dev? (atom false))

(defn app
  [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    "hello HTTP!"})

(deftask server
  [boot]
  (fn [continue]
    (fn [event]
      (let [app (if @in-dev?
                  (reload/wrap-reload app)
                  app)]
        (run-server app {:port 3000})))))

(deftask dev-server
  [boot]
  (reset! in-dev? true)
  (server boot))
