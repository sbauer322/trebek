(ns trebek.core
    (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [secretary.core :as secretary :include-macros true]
              [accountant.core :as accountant]
              [trebek.pages.play :as play]
              [trebek.app :as a]))

;; -------------------------
;; Views

(defn header []
  [:nav#navbar
    [:ul.navbar
     [:li [:a {:href "/"} "Trebek"]]
     [:li [:a {:href "/about"} "About"]]
     [:li [:a {:href "https://www.paypal.me/sbauer322"} "Donate"]]]])

(defn footer []
  [:div#footer
    [:p ""]])

(defn home-page []
  [:div
   (header)
   [:div#body
    [:div#qa.play
     (play/search)
     (play/question)
     (play/answer)
     (play/nav)]]
   (footer)])

(defn about-page []
  [:div
   (header)
   [:div#body
    [:p "Trebek is here to help you study and hone your Jeopardy knowledge. A powerful search engine lets you test yourself against categories and relevant questions. Alternatively, if you leave the search field blank then Trebek will provide you with a random stream of questions."]
    [:p "All question data has been gathered from J-Archive, a fantastic resource."]
    [:p "There are a number of features still to be implemented for Trebek. Highlights include:"]
    [:ul
     [:li "User accounts"]
     [:li "Score keeping"]
     [:li "Analysis to help identify weak areas"]
     [:li "Links to corresponding Wikipedia pages for answers"]
     [:li "Shareable user-defined lists of questions"]]
    [:p "If you find Trebek useful, please consider donating. This site is ad-free to provide a pleasant experience yet there are still servers to pay for. Even a small donation goes a long way!"]]
   (footer)])

(defn current-page []
  [:div [(session/get :current-page)]])

;; -------------------------
;; Routes

(secretary/defroute "/" []
  (session/put! :current-page #'home-page))

(secretary/defroute "/about" []
  (session/put! :current-page #'about-page))

;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app"))
  ;; Populate questions on load.
  (a/retrieve-question-set))

(defn init! []
  (accountant/configure-navigation!
    {:nav-handler
     (fn [path]
       (secretary/dispatch! path))
     :path-exists?
     (fn [path]
       (secretary/locate-route path))})
  (accountant/dispatch-current!)
  (mount-root))
