(ns trebek.app
  (:require [reagent.core :as reagent :refer [atom]]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]])
    (:require-macros [cljs.core.async.macros :refer [go]]))

(def app (atom {:play
                {:set [{:question "Loading..."
                        :answer "Loading..."}]
                 :i 0}
                 :current {}
                }))

(defn current-question
  []
  (get-in @app [:play :current]))

(defn- update-current
  []
  (let [i (get-in @app [:play :i])
        questions (get-in @app [:play :set])]
    (swap! app assoc-in [:play :current] (get questions i))))

(defn retrieve-question-set []
  (go (let [response (<! (http/get "http://localhost:3000/api/v1.0/question/2"
                                   ))]
        ;; TODO: Look into a cleaner way to update the atom.
        (swap! app assoc-in [:play :set] (:body response))
        (swap! app assoc-in [:play :i] 0)
        (update-current))))

(defn next-question
  []
  (let [i (get-in @app [:play :i])]
    (if (<= i 8)
      (do
        (swap! app update-in [:play :i] inc)
        (update-current))
      (retrieve-question-set))))
