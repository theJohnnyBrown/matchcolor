(ns matchcolor.browser
  (:require [om.core :as om :include-macros true]
            [goog.events :as ev]
            [matchcolor.views :as views]
            [secretary.core :as secretary])
  (:import goog.history.EventType))

(def browser? (exists? js/document))

(defn render-root [component target]
  (om/root component {} {:target target}))

(defn render-partials [partial active]
  (render-root partial (.getElementById js/document "partial-wrapper"))
  (render-root (views/nav active) (.getElementById js/document "nav-wrapper")))

;; try making active an atom

;; setup navigation. See http://closure-library.googlecode.com/git-history/6b23d1e05147f2e80b0d619c5ff78319ab59fd10/closure/goog/demos/html5history.html
(defn setup-app [component active]
  (do
    (render-partials component active)
    (ev/listen ;; when token changes, update view
     views/hist (.-NAVIGATE EventType)
     #(let [{new-template :template new-active :active}
            (secretary/dispatch! (str "/" (.-token %)))]
        (.log js/console (str "navigate template " new-template))
        (render-partials new-template new-active)))))
