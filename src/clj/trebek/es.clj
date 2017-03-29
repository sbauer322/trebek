(ns trebek.es
  (:require [clj-http.client :as client]
            [cheshire.core :as cheshire]))

(def es-address
  "http://localhost:9200/")

(defn rando-seed
  []
  (System/currentTimeMillis))

(defn simple-search
  [s]
  (cheshire/generate-string
   {:query
    {:simple_query_string
     {:query s
      :fields ["category" "question"]}}}))

(defn random-search
  []
  (cheshire/generate-string
   {:query
    {:function_score
     {:functions [
                  {:random_score
                   {:seed (str (rando-seed))}}]}}}))

(defn process-hit
  [hit]
  (conj (select-keys hit [:_id]) (:_source hit)))

(defn random
  []
  ;; To generate a random question: `http://stackoverflow.com/questions/25887850/random-document-in-elasticsearch`
  (let [hits (-> (client/post (str es-address "trivia/jeopardy/_search")
                              {:accept :json
                               :content-type :json
                               :body (random-search)})
                 :body
                 (cheshire/parse-string true)
                 :hits
                 :hits)
        results (mapv #(process-hit %) hits)]
    results))

(defn search
  ([q]
  (search q 0))
  ([q from]
   (println (str "query: " "   and from: " from))
   (let [hits (-> (client/post (str es-address "trivia/jeopardy/_search?size=10" "&from=" from)
                               {:accept :json
                                :content-type :json
                                :body (simple-search q)})
                  :body
                  (cheshire/parse-string true)
                  :hits
                  :hits)
         results (mapv #(process-hit %) hits)]
     results)))
