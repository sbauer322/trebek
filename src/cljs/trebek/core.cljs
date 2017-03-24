(ns trebek.core
    (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [secretary.core :as secretary :include-macros true]
              [accountant.core :as accountant]
              [trebek.pages.play :as play]
              [trebek.app :as a]))

;; -------------------------
;; Views

(defn home-page []
  [:div
   [:nav#navbar
    [:ul.navbar
     [:li [:a {:href "/"} "Trebek"]]
     [:li [:a {:href "/about"} "About"]]]]
   [:div#body
    [:div#qa.play
     (play/question)
     (play/answer)
     (play/nav)]]
   [:div#footer
    [:p ""]]])

(defn about-page []
  [:div [:h2 "About trebek"]
   [:div [:a {:href "/"} "go to the home page"]]])

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
