(ns year-paper-frontend.screens.main.map-main
  (:require
   [reagent.core  :as r]
   [re-frame.core :as rf]))

(declare ^:dynamic *map*)

(defn create-lat-lng [lat lng]
  (js/google.maps.LatLng. lat lng))

(defn create-map [lat lng]
  (let [map-opts (clj->js {"center" (create-lat-lng lat lng) "zoom" 10 "mapId" "7c371ae2772f2444" "mapsApiKey" "AIzaSyBJj03oAQ3ivtgDtO9dK9bSr4_WNNDCiXQ"})]
    (set! *map* (js/google.maps.Map. (.getElementById js/document "map-canvas") map-opts))))

(defn create-infowindow [id marker]
  (let [info-window (js/google.maps.InfoWindow. (clj->js {"content" (str "<div id='info-" id "'>Some test text</div>")}))]
    (.open info-window clj->js {"anchor"      marker
                                "map"         *map*
                                "shouldFocus" false})))

(defn create-marker [position map]
  (let [marker (js/google.maps.Marker. (clj->js {"position" position "map" map}))]
    (js/google.maps.event.addListener
     marker
     "click"
     (fn [e]
       (js/console.log "marker click: " e)
       (create-infowindow (.-_id e) marker)))))

(defn map-component-render []
  [:div#map-canvas.content])

(defn map-component-did-mount []
  (create-map 49.83826 24.02324)
  (js/google.maps.event.addListener
   *map*
   "click"
   (fn [e]
     (create-marker (.-latLng e) *map*)))
  (js/console.log "did-mount" *map*))

(defn map-component []
  (r/create-class {:reagent-render map-component-render
                   :component-did-mount map-component-did-mount}))

(defn map-main []
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
     [map-component]]))