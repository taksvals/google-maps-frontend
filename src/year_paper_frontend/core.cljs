(ns year-paper-frontend.core
    (:require
     [reagent.dom :as rdom]
     [re-frame.core        :as rf]
     [year-paper-frontend.router :refer [init-routes!]]
     [year-paper-frontend.store.events]
     [year-paper-frontend.store.subscribers]))

(defn main-page
  []
  (let [current-route @(rf/subscribe [:current-route])]
    [:div
     (when current-route
       (if-let [view (-> current-route :data :view)]
         [view]
         [:h3 "No view for this screen"]))]))

(defn root []
  [:div#root
   [main-page]])

(defn ^:dev/after-load render
  "Render the toplevel component for this app."
  []
  (init-routes!)
  (rdom/render [root]
               (.getElementById js/document "app")))


(defn main []
  (js/console.log "MAIN FN Reloaded")
  (rf/dispatch-sync [:initialise-db])
  (render))