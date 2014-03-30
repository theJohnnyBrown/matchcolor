(ns matchcolor.routes
  (:require [secretary.core :as secretary
             :include-macros true :refer [defroute]]
            [garden.color :as color :refer [hsl rgb]]
            [matchcolor.views :as views]
            [matchcolor.colorsimilarity :as colorsimilarity]))

(defroute "/" [] {:template views/home :state {:active "Home"}})

(defroute "/about" [] {:template views/about :state {:active "About"}})

(defroute "/:hexcolor" [hexcolor]
  (if (color/hex? hexcolor)
    {:template views/color
     :state (merge {:active "Color"}
                   (colorsimilarity/likely-color hexcolor))}
    {:template views/invalid-color :state {:active "Home"}}))
