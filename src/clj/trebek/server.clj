(ns trebek.server
  (:require [trebek.handler :refer [app]]
            [config.core :refer [env]]
            ; [ring.adapter.jetty :refer [run-jetty]]
            [org.httpkit.server :refer [run-server]])
  (:gen-class))

(defonce server (atom nil))

(defn stop-server []
  (when-not (nil? @server)
    ;; graceful shutdown: wait 100ms for existing requests to be finished
    ;; :timeout is optional, when no timeout, stop immediately
    (@server :timeout 100)
    (reset! server nil)))

(defn -main [& args]
  (let [port (Integer/parseInt (:port env "3000"))]
    (run-server #'app {:port port})
    (println "Server started at port " port)))
