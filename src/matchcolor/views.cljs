(ns matchcolor.views
  (:require [om.core :as om :include-macros true]
            [goog.events :as ev]
            [sablono.core :as html :refer-macros [html]]
            [clojure.string :as str])
  (:import goog.history.Html5History))

(def __dirname (if (exists? (js* "__dirname")) (js* "__dirname")))
; Define namespaced references to Node's externed globals:
(def node-require (if __dirname (js* "require")))
(def node-process (if __dirname (js* "process")))

(def app-state (atom {}))

(def browser? (exists? js/document))
(def site-name "MatchColor")
(def React (if browser? js/React (node-require "react")))

(declare client-load!)

(defn nav [{active :active}]
  (om/component
     (html
      [:ul.nav.masthead-nav
       [(if (= active "Home") :li.active :li)
        [:a {:href "/" :onClick client-load!} "Home"]]
       [(if (= active "About") :li.active :li)
        [:a {:href "/about" :onClick client-load!} "About"]]])))

(defn home []
  (om/component
   (html
    [:div.inner.cover
     [:h1.cover-heading "Pick a color."]
     [:form.form-inline
      {:role "form" :action "" :method "post"}
      [:div.form-group
       [:label.sr-only {:for "color"} "Color"]
       [:input#color
        {:class "form-control color" :type "text" :name "color"}]]
      [:button.btn.btn-default {:type "submit"} "Go"]]])))

(defn about []
  (om/component
   (html
    [:div.inner.cover
     [:h1.cover-heading "About"]
     [:p.lead
      "An experiment in building a server-side web application in ClojureScript that also does something relatively useful: Find the name of a hexcolor."]])))

(defn four-oh-four []
  (om/component
   (html
    [:div.inner.cover
     [:h1.cover-heading "Page not found"]
     [:p.lead
      "Didn't find that page. Sorry."]])))

(defn invalid-color []
  (om/component
   (html
    [:div.inner.cover
     [:h1.cover-heading "Not a valid color!"]
     [:p.lead
      "Try again!"]])))

(defn color [hsh]
  (om/component
    (html
     [:div.inner.cover [:h1.cover-heading "Found it!"]
      [:div.row
       [:div.col-md-4
        [:h2 {:style {"color" (get hsh :initcol)}}
         (str "Your Color: " (get hsh :initcol))]]
       [:div.col-md-4
        [:h2 {:style {"color" (get hsh :foundcol)}}
         (str "Closest Match: " (get hsh :name) " (" (get hsh :foundcol) ")")]]
       [:div.col-md-4
        [:h2
         (str "Color Distance: " (get hsh :colordistance))]]]])))

(defn layout [state]
  (om/component
     (html
      [:html
       {:lang "en"}
       [:head
        [:meta {:charSet "utf-8"}]
        [:meta {:content "IE=edge", :http-equiv "X-UA-Compatible"}]
        [:meta
         {:content "width=device-width, initial-scale=1", :name "viewport"}]
        [:meta {:content "", :name "description"}]
        [:meta {:content "", :name "author"}]
        [:link {:href "/img/favicon.ico", :rel "shortcut icon"}]
        [:title (str site-name " - " (:active state))]
        [:link
         {:rel "stylesheet", :href "/css/bootstrap.min.css"}]
        [:link {:rel "stylesheet", :href "/css/cover.css"}]]
       [:body
        [:a
         {:href "https://github.com/seabre/matchcolor"}
         [:img
          {:style {:position "absolute" :top 0 :left 0 :border 0}
           :src
           "https://s3.amazonaws.com/github/ribbons/forkme_left_gray_6d6d6d.png",
           :alt "Fork me on GitHub"}]]
        [:div.site-wrapper
         [:div.site-wrapper-inner
          [:div.cover-container
           [:div.masthead.clearfix
            [:div.inner
             [:h3.masthead-brand site-name]
             [:div#nav-wrapper
              (om/build nav state)]]]
           [:div#partial-wrapper
            (om/build (:partial state) state)]
           [:div.mastfoot
            [:div.inner
             [:p
              "ClojureScript goodness provided by "
              [:a {:href "http://about.me/seabre"} "seabre"]]]]]]]

        [:script {:src "https://code.jquery.com/jquery-1.10.2.min.js"}]
        [:script {:src "/js/jscolor.js"}]
        [:script {:src "/js/bootstrap.min.js"}]
        [:script {:src "/js/docs.min.js"}]
        [:script {:src "/js/matchcolor.js"}]]])))

;; render components to string. Used on the server
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn render-html [component state]
  (.renderComponentToString
   React
   (om/build component state)))

(defn layout-render [layout-partial state]
  (str "<!DOCTYPE html>"
       (render-html layout (assoc state :partial layout-partial))))

;; browser render and routing functions
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(def hist
  (if browser?
   (doto (Html5History.) (.setUseFragment false) (.setEnabled true))))

(defn strip-leading-slash [s] (if (= (str (first s)) "/")
                                (str/join (rest s)) s))


(defn client-load! [e] ;; e should be the click event of an <a>
  ;; perform the equivalent of a page load without using the network
  (do
    (.setToken hist
               (-> e .-target (.getAttribute "href") strip-leading-slash)
               (-> e .-target .-title))
    (.preventDefault e)))
