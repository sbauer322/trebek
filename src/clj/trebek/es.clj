(ns trebek.es
  (:require [clj-http.client :as client]
            [cheshire.core :as cheshire]))

(def es-address
  "http://localhost:9200/")

(defn rando-seed
  []
  (System/currentTimeMillis))

(defn sample-search-body
  []
  (str "{
    \"query\": {
        \"function_score\": {
         \"functions\": [
            {
               \"random_score\": {
                  \"seed\": \"" (rando-seed) "\"
               }
            }
         ]
      }
    }
   }"))

(defn tap-log
  [x]
  (println "logging: " x)
  x)

(defn get-question
  []
  ;; To generate a random question: `http://stackoverflow.com/questions/25887850/random-document-in-elasticsearch`
  (let [hits (-> (client/post (str es-address "trivia/jeopardy/_search")
                              {:accept :json
                               :content-type :json
                               :body (sample-search-body)})
                 :body
                 (cheshire/parse-string true)
                 :hits
                 :hits
                 ;tap-log
                 )
        result (mapv #(get % :_source) hits)]
    result))
