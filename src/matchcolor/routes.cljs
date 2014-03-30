(ns matchcolor.routes
  (:require [secretary.core :as secretary
             :include-macros true :refer [defroute]]
            [garden.color :as color :refer [hsl rgb]]
            [matchcolor.views :as views]
            [matchcolor.colorsimilarity :as colorsimilarity]))

(defroute "/" [] {:template views/home :active "Home"})

(defroute "/about" [] {:template views/about :active "About"})

(defroute "/:hexcolor" [hexcolor]
  (if (color/hex? hexcolor)
    {:template (views/color (colorsimilarity/likely-color hexcolor))
     :active "Color"}
    {:template views/invalid-color :active "Home"}))
