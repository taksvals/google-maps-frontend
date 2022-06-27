(ns year-paper-frontend.store.subscribers
  (:require 
   [re-frame.core :as rf]))

(rf/reg-sub
  :current-route
  (fn [db]
    (:current-route db)))

(rf/reg-sub
  :current-user
  (fn [db]
    (:current-user db)))

(rf/reg-sub
  :markers
  (fn [db]
    (-> (:map db)
        (:markers))))