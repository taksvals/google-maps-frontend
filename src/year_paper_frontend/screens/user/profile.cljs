(ns year-paper-frontend.screens.user.profile
  (:require 
   [re-frame.core :as rf]))

(defn profile []
  (let [current-user @(rf/subscribe [:current-user])]
    (fn []
      [:div {:style {:background-color "black"}}
       [:input#active {:type "checkbox"}]
       [:label.menu-btn {:for "active"} [:span]]
       [:label.close {:for "active"}]
       [:div.wrapper
        [:ul
         [:li
          [:a {:on-click #(rf/dispatch [:navigate :map-main])}
           "Map"]]
         [:li
          [:a {:on-click #(rf/dispatch [:navigate :profile])}
           "Profile"]]
         [:li
          [:a {:on-click #(rf/dispatch [:logout-user])}
           "Logout"]]]]
       [:div.profile-wrapper.content
        [:div.profile
         [:div.overlay
          [:div {:class "about d-flex flex-column"}
           [:h4 (:username current-user)]
           [:h5 (:email current-user)]]]]]])
    ))