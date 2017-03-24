(ns trebek.pages.play
  (:require [reagent.core :as r :refer [atom]]
            [trebek.app :as a :refer [app]]
            [trebek.util :as u]))

(defn toggle-answer [state]
  (aset (u/by-id "answer") "style" "visibility" state))

(defn reset-play []
  (toggle-answer "hidden"))

(defn reveal-answer []
   [:input.flex-item.play-nav-item {:type "button" :value "Reveal"
            :on-click #(toggle-answer "visible")}])

(defn next-button []
  [:input.flex-item.play-nav-item {:type "button" :value "Next"
           :on-click (fn []
                       (reset-play)
                       (a/next-question))}])

(defn previous-button []
  [:input.flex-item.play-nav-item {:type "button" :value "Previous"
           :on-click (fn []
                       (reset-play)
                       (a/previous-question))}])

(defn user-correct []
  [:div    
   [:input#uc {:type "button" :value "Got it right?"
               :on-click #(.log js/console "Stubbed!")}]])

(defn answer-wrong []
  [:div
   [:input#aw {:type "button" :value "Answer wrong?"
               :on-click #(.log js/console "Stubbed!")}]])

(defn category [s]
  [:li.flex-item.category-item s])

(defn info [s]
  [:li.flex-item.info-item s])

(defn question []
  [:div#question
   [:div.heading
    [:div.flex-container
     [:ul.flex-container.category-container
      (category (:category (a/current-question)))]
     [:ul.flex-container.info-container
      (info (:round (a/current-question)))
      (info (:value (a/current-question)))
      (info (:air_date (a/current-question)))
      (info (str "#" (:show_number (a/current-question))))]]]
   [:div#question-text
    [:p (:question (a/current-question))]]])

(defn answer []
   [:div#answer
    [:div.heading
     ;; (user-correct)
     ;;(answer-wrong)
     ]
    [:div.text
     [:p (:answer (a/current-question))]]])

(defn nav []
  [:div#play-nav.flex-container.play-nav-container
   (previous-button)
   (reveal-answer)
   (next-button)])
