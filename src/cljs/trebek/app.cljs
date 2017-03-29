(ns trebek.app
  (:require [reagent.core :as reagent :refer [atom]]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]])
    (:require-macros [cljs.core.async.macros :refer [go]]))

(def app (atom {:play {
                       :questions [{:question ""
                                    :answer ""}]
                       :index 0
                       :search ""
                       :out-of-results? false
                       }}))

(defn search
  []
  (get-in @app [:play :search]))

(defn update-search
  [s]
  (swap! app assoc-in [:play :search] s))

(defn index
  []
  (get-in @app [:play :index]))

(defn reset-index
  []
  (swap! app assoc-in [:play :index] 0))

(defn questions
  []
  (get-in @app [:play :questions]))

(defn reset-questions
  []
  (swap! app assoc-in [:play :questions] [{:question ""
                                           :answer ""}]))

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

(defn out-of-results?
  []
  (get-in @app [:play :out-of-results?]))

(defn retrieve-question-set
  []
  (go (let [endpoint (if (= (search) "")
                       "http://localhost:3000/api/v1.0/question/1"
                       "http://localhost:3000/api/v1.0/search")
            response (<! (http/get endpoint
                                   {:query-params {:q (search) :from (index)}}))]
        ;; TODO: Look into a cleaner way to update the atom.
        (if (empty? (:body response))
          (swap! app assoc-in [:play :out-of-results?] true)
          (do
            (swap! app update-in [:play :questions]
                   #(into % (:body response)))
            (swap! app assoc-in [:play :out-of-results?] false)
            (when (< 1 (count (next-questions)))
              (swap! app update-in [:play :index] inc)))))))

(defn next-question
  []
  (let [i (count (next-questions))]
    (if (< 1 i)
      (swap! app update-in [:play :index] inc)
      (retrieve-question-set))))

(defn previous-question
  []
  (when-not (and (empty? (previous-questions)) (<= 0 (index)))
    (swap! app update-in [:play :index] dec)))
