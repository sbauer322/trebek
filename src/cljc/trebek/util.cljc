(ns trebek.util)

#?(:cljs
   (defn by-id [id]
     (.getElementById js/document id)))
