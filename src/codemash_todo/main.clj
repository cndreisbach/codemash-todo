(ns codemash-todo.main
  (:require [ring.middleware.reload :as reload]
            [org.httpkit.server :refer [run-server]]
            [compojure.handler :refer [site]]
            [compojure.core :refer [defroutes GET POST ANY]]
            [codemash-todo.resources :as r]
            [liberator.dev :refer [wrap-trace]]))

(def stop-server (atom nil))
(def in-dev? (atom false))

;; ROUTES
;; GET / -> Hoplon app
;; GET /list - show list (JSON)
;; POST /todo - add a todo and return id (JSON)
;; DELETE /todo/:id - remove todo (JSON)
;; PUT /todo/:id - update todo (JSON)

(defn app
  [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    "hello HTTP!!!!"})

(defroutes all-routes
  (GET "/" [] "handling-pages")
  (GET "/save" [] app)
  (ANY "/todo" [] r/list)
  (GET "/todo/:id" [id] (r/todo id)))

(defn start-server
  []
  (let [handler (if @in-dev?
                  (-> (site #'all-routes)
                      (reload/wrap-reload)
                      (wrap-trace :header :ui))
                  all-routes)]
    (when (not (nil? @stop-server))
      (@stop-server))
    (reset! stop-server (run-server handler {:port 3000}))))

(defn -main
  [& args]
  (when (System/getenv "DEV")
    (reset! in-dev? true))
  (start-server))
