(ns trebek.pages.play
  (:require [reagent.core :as r :refer [atom]]
            [trebek.app :as a :refer [app]]
            [trebek.util :as u]))

(defn toggle-answer [state]
  (aset (u/by-id "answer") "style" "visibility" state))

(defn reset-answer []
  (toggle-answer "hidden"))

(defn reset-state []
  (reset-answer)
  (a/reset-index)
  (a/reset-questions)
  (a/next-question))

(defn reveal-answer []
   [:input.flex-item.play-nav-item.button {:type "button" :value "Reveal"
            :on-click #(toggle-answer "visible")}])

(defn next-button []
  [:input.flex-item.play-nav-item.button {:type "button" :value "Next"
           :on-click (fn []
                       (reset-answer)
                       (a/next-question))}])

(defn previous-button []
  [:input.flex-item.play-nav-item.button {:type "button" :value "Previous"
           :on-click (fn []
                       (reset-answer)
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

(defn search []
  [:div
   [:input {:type "search" :placeholder "Leave blank for random questions"
            :on-change (fn [evt]
                         (let [value (-> evt .-target .-value)]
                           (a/update-search value)))}]
   [:input {:type "button" :value "Search"
             :on-click #(reset-state)}]])

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
      (info (:show_number (a/current-question)))]]]
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
  [:div
   [:div#play-nav.flex-container.play-nav-container
    [:p (when (a/out-of-results?)
          "Out of results. Please search again.")]]
   [:div#play-nav.flex-container.play-nav-container
    (previous-button)
    (reveal-answer)
    (next-button)]])
