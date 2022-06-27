(ns year-paper-frontend.screens.auth.login
  (:require
   [reagent.core  :as r]
   [re-frame.core :as rf]))

(defn login []
  (let [email    (r/atom "")
        password (r/atom "")]
    (fn []
      [:div.auth
       [:form {:on-submit (fn [e]
                            (.preventDefault e)
                            (rf/dispatch [:login-user {:email    @email
                                                       :password @password}]))}
        [:h3 "Sign in to your account!"]
        [:label
         {:for "email"} "Email"]
        [:input#email
         {:type          "email"
          :default-value @email
          :placeholder   "Email"
          :on-change     (fn [event]
                           (reset! email (-> event .-target .-value)))}]
        [:label
         {:for "password"} "Password"]
        [:input#password
         {:type          "password"
          :default-value @password
          :placeholder   "Password"
          :on-change     (fn [event]
                           (reset! password (-> event .-target .-value)))}]
        [:button {:type "submit"}
         "Sign in"]
        [:span "Don't have an account? "
         [:a {:on-click #(rf/dispatch [:navigate :auth/sign-up])} "Sign Up"]]]])))