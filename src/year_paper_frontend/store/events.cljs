(ns year-paper-frontend.store.events
  (:require
   [re-frame.core :as rf]
   [reitit.frontend.easy :as rfe]
   [reitit.frontend.controllers :as rfc]
   [day8.re-frame.http-fx]
   [ajax.core :as ajax]
   [year-paper-frontend.store.db :refer [default-db]]))

(rf/reg-event-db
 :initialise-db 
 (fn [_ _]
   default-db))

(rf/reg-event-db
 :app-loading
 (fn [db [_ v]]
   (assoc-in db [:loading :app] v)))

(rf/reg-event-db
 :write-to
 [rf/trim-v]
 (fn [db [path value]]
   assoc-in db path value))

(rf/reg-event-fx
  :navigate
  (fn [_ [_ & route]]
    {:navigate! route}))

(rf/reg-event-fx
  :navigate-no-history
  (fn [_ [_ & route]]
    {:navigate-nh! route}))

(rf/reg-fx
 :navigate-nh!
 (fn [route] 
   (apply rfe/replace-state route)))

;; Triggering navigation from events.
(rf/reg-fx
 :navigate!
 (fn [route]
   (apply rfe/push-state route)))


(rf/reg-event-db
 :navigated
 [rf/trim-v]
 (fn [db [new-route]]
   (let [old-match   (:current-route db)
         controllers (rfc/apply-controllers (:controllers old-match) new-route)]
     (assoc db :current-route (into {} (assoc new-route :controllers controllers))))))

;;-------------------Requests----------------------------

(rf/reg-event-fx
 :register-user
 [rf/unwrap]
 (fn [_ params]
   {:http-xhrio {:uri             "http://localhost:5050/register"
                 :method          :post
                 :params          params
                 :format          (ajax/json-request-format)
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success      [:register-success]
                 :on-failure      [:register-failure]}}))

(rf/reg-event-fx
 :register-success
 (fn [db [_ response]]
   {:db       (assoc db :current-user (:message (js->clj response)))
    :dispatch [:navigate :map-main]}))

(rf/reg-event-db
 :register-failure
 (fn [db [_ response]]
   (js/console.log "register error: " response)
   (assoc db :errors {:register-error "Wrong data! Try again!"})
   db))

(rf/reg-event-fx
 :login-user
 [rf/unwrap]
 (fn [_ params]
   {:http-xhrio {:uri             "http://localhost:5050/login"
                 :method          :post
                 :params          params
                 :format          (ajax/json-request-format)
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success      [:login-success]
                 :on-failure      [:login-failure]}}))

(rf/reg-event-fx
 :login-success
 (fn [db [_ response]]
   {:db       (assoc db :current-user (:message (js->clj response)))
    :dispatch [:navigate :map-main]}))

(rf/reg-event-db
 :login-failure
 (fn [db [_ response]]
   (js/console.log "login error: " response)
   (assoc db :errors {:login-error "Invalid credentials! Try again!"})))

(rf/reg-event-fx
 :logout-user
 [rf/unwrap]
 (fn [{:keys [db]} _]
   {:db       (assoc db :current-user nil)
    :dispatch [:navigate :auth/sign-in]}))
