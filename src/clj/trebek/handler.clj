(ns trebek.handler
  (:require [compojure.core :refer [GET defroutes context]]
            [compojure.route :refer [not-found resources]]
            [ring.middleware.json :refer [wrap-json-response]]
            [ring.util.response :as r :refer [response]]
            [hiccup.page :refer [include-js include-css html5]]
            [trebek.middleware :refer [wrap-middleware]]
            [trebek.es :as es]
            [config.core :refer [env]]))

(def dev-css-styles
  (include-css
   "/css/site.css"
   "/css/play.css"
   "/css/navigation.css"))

(def prod-css-styles
  (include-css
   "/css/site.min.css"
   "/css/play.min.css"
   "/css/navigation.min.css"))

(def mount-target
  [:div#app
      [:h3 "ClojureScript has not been compiled!"]
      [:p "please run "
       [:b "lein figwheel"]
       " in order to start the compiler"]])

(defn head []
  [:head
   [:meta {:charset "utf-8"}]
   [:meta {:name "viewport"
           :content "width=device-width, initial-scale=1"}]
   (if (env :dev)
     dev-css-styles
     prod-css-styles)])

(defn loading-page []
  (html5
    (head)
    [:body {:class "body-container"}
     mount-target
     (include-js "/js/app.js")]))

                                        ; Successful POST - 201
                                        ; PUT - No Content - 204
                                        ; Put - Success - 200
                                        ; GET - Success - 200
                                        ; DELETE - Item marked for deletion - 202
                                        ; DELETE - No Content - 204


(defn tap-log
  [x]
  (println "logging: " x)
  x)

(defroutes api-routes
  (wrap-json-response
   (context "/api/v1.0" []
            (GET "/question/:id" [id]
                 ;; Need to figure out better response rendering?
                 (tap-log (response (es/random))))
            (GET "/search" [q from]
                 (tap-log q)
                 ;; Need to figure out better response rendering?
                 (tap-log (response (es/search q from)))))))

(defroutes site-routes
  (GET "/" [] (loading-page))
  (GET "/about" [] (loading-page)))

(defroutes routes
  site-routes
  api-routes
  (resources "/")
  (not-found "Not Found"))

(def app
  (wrap-middleware #'routes))
