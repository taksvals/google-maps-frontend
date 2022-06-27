(ns year-paper-frontend.router
  (:require
   [re-frame.core        :as rf]
   [reitit.frontend      :as rt]
   [reitit.frontend.easy :as rfe]
   [reitit.coercion.spec :as rss]
   [year-paper-frontend.store.subscribers]
   [year-paper-frontend.screens.auth.login :refer [login]]
   [year-paper-frontend.screens.auth.register :refer [register]]
   [year-paper-frontend.screens.main.map-main :refer [map-main]]
   [year-paper-frontend.screens.user.profile  :refer [profile]]))

(defn- not-found []
  [:div "404 Not Found"])

(def default-route :map-main)

(defn- root-dispatcher [_ current-user]
  (if (true? (:active current-user))
    default-route
    :auth/sign-in))

(def routes
  ["/"
   [""
    {:name       :root
     :dispatcher root-dispatcher
     :protected? true}]
   ["not-found"
    {:name :not-found
     :view not-found}]
   [""
    {:protected? true}
    ["map-main"
     {:name :map-main
      :view map-main}]
    ["profile"
     {:name :profile
      :view profile}]]
   ; /logout
   ["auth"
    {:unathorized? true}
    ["/sign-in"
     {:name :auth/sign-in
      :view login}]
    ["/sign-up"
     {:name :auth/sign-up
      :view register}]]])

(def router
  (rt/router routes {:data {:coercion rss/coercion}}))

(defn- protected-dispatcher [route current-user]
  (if current-user
    (let [dispatcher (-> route :data :dispatcher)]
      (when dispatcher
        (dispatcher route current-user)))
    :auth/sign-in))

(defn dispatcher [route]
  (let [current-user @(rf/subscribe [:current-user])]
    (cond
      (-> route :data :protected?)   (protected-dispatcher route current-user)
      (-> route :data :unathorized?) (when (:active current-user) :root)
      :else nil)))

(defn- on-navigate [new-match] 
  (if new-match
    (if-let [new-route (dispatcher new-match)]
      (rf/dispatch [:navigate-no-history new-route])
      (rf/dispatch [:navigated new-match]))
    nil))

(defn init-routes! []
  (rfe/start!
   router
   on-navigate
   {:use-fragment false}))

