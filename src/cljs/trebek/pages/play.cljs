(ns trebek.pages.play
  (:require [reagent.core :as r :refer [atom]]
            [trebek.app :as a :refer [app]]))

(defn reveal-answer []
  [:div
   [:input {:type "button" :value "Reveal"
            :on-click #(.log js/console "Stubbed!")}]])

(defn next-button []
  [:input {:type "button" :value "Next"
           :on-click #(a/next-question)}])

(defn user-correct []
  [:div    
   [:input#uc {:type "button" :value "Got it right?"
               :on-click #(.log js/console "Stubbed!")}]])

(defn answer-wrong []
  [:div
   [:input#aw {:type "button" :value "Answer wrong?"
               :on-click #(.log js/console "Stubbed!")}]])

(defn question []
  [:div#question
   [:div.heading
    [:h3 "Question:"]]
    [:div.info
      [:p (str (:category (a/current-question)) ", "
               (:value (a/current-question)) ", "
               (:air_date (a/current-question)))]]
     [:div.text
      [:p (:question (a/current-question))]]])

(defn answer []
  [:div#answer
   [:div.heading
    [:h3 "Answer:"]
    (reveal-answer)
    (user-correct)
    (answer-wrong)]
   [:div.text
    [:p (:answer (a/current-question))]]
   ;[:div#learn
   ; [:p "This would be the expandable 'Learn More' section."]]
   ])

(defn nav []
  [:div#nav
   (next-button)])
