(ns trebek.app
  (:require [reagent.core :as reagent :refer [atom]]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]])
    (:require-macros [cljs.core.async.macros :refer [go]]))

(def app (atom {:play {
                       :questions [{:question "Loading..."
                                    :answer "Loading..."}]
                       :index 0}
                }))

(defn index
  []
  (get-in @app [:play :index]))

(defn questions
  []
  (get-in @app [:play :questions]))

(defn next-questions
  "First question in vector is most recent."
  []
  (second (split-at (index) (questions))))

(defn previous-questions
  "Last question in vector is most recent."
  []
  (first (split-at (index) (questions))))

(defn current-question
  []
  ((questions) (index)))    

(defn retrieve-question-set
  []
  (go (let [response (<! (http/get "http://localhost:3000/api/v1.0/question/2"))]
        ;; TODO: Look into a cleaner way to update the atom.
        (swap! app update-in [:play :questions] #(into % (:body response)))
        (swap! app update-in [:play :index] inc))))

(defn next-question
  []
  (let [i (count (next-questions))]
    (if (<= 8 i)
      (swap! app update-in [:play :index] inc)
      (retrieve-question-set))))

(defn previous-question
  []
  (when-not (empty? (previous-questions))
    (swap! app update-in [:play :index] dec)))
